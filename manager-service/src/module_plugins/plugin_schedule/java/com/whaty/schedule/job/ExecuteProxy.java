package com.whaty.schedule.job;

import com.whaty.schedule.bean.PeScheduleJob;
import com.whaty.schedule.bean.PeScheduleJobRecord;
import com.whaty.schedule.bean.PeScheduleTrigger;
import com.whaty.schedule.constants.QuartzConstant;
import com.whaty.schedule.util.QuartzUtils;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import javax.management.MalformedObjectNameException;
import java.lang.reflect.Method;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.FutureTask;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static com.whaty.schedule.constants.QuartzConstant.JOB_STATUS_FAILURE;
import static com.whaty.schedule.constants.QuartzConstant.JOB_STATUS_SUCCESS;


/**
 * 执行代理
 *
 * @author weipengsen
 */
public class ExecuteProxy implements MethodInterceptor {

    private final JobExecutionContext jobExecutionContext;

    private final PeScheduleJob job;

    private final PeScheduleTrigger trigger;

    private PeScheduleJobRecord record;

    private static ExecutorService executorService;

    private final static Logger logger = LoggerFactory.getLogger(ExecuteProxy.class);

    static {
        ThreadFactory namedThreadFactory = new ThreadFactory() {

            private long index = 1;

            @Override
            public Thread newThread(Runnable r) {
                Thread thread = new Thread(r);
                thread.setName(QuartzConstant.INVOKE_THREAD_NAME + (this.index ++));
                return thread;
            }
        };
        executorService = new ThreadPoolExecutor(2, 10, 60, TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(1024), namedThreadFactory);
    }

    /**
     * 创建动态代理并执行调用
     * @param job
     * @param trigger
     * @param jobExecutionContext
     */
    public static void execute(PeScheduleJob job, PeScheduleTrigger trigger, JobExecutionContext jobExecutionContext)
            throws Exception {
        // 获取调用对象
        Object target = getTarget(job);
        // 创建代理
        Object proxy = newInstance(job, trigger, target, jobExecutionContext);
        // 获取目标方法
        Method targetMethod = QuartzUtils.getValidMethod(target.getClass(), job.getMethodName());
        if (targetMethod == null) {
            throw new NoSuchMethodException();
        }
        // 执行
        if (targetMethod.getParameterCount() == 0) {
            targetMethod.invoke(proxy);
        } else {
            Class clazz = targetMethod.getParameterTypes()[0];
            Object param;
            if (Collection.class.isAssignableFrom(clazz)) {
                param = JSONArray.toCollection(JSONArray.fromObject(trigger.getData()), clazz);
            } else if (clazz.isArray()) {
                param = JSONArray.toArray(JSONArray.fromObject(trigger.getData()), clazz);
            } else if (Map.class.isAssignableFrom(clazz)) {
                param = JSONObject.fromObject(trigger.getData());
            } else {
                throw new NoSuchMethodException();
            }
            targetMethod.invoke(proxy, StringUtils.isNotBlank(trigger.getData()) ? param : null);
        }
    }

    /**
     * 根据调度器配置获取调用目标对象
     * @param job
     * @return
     */
    private static Object getTarget(PeScheduleJob job) throws Exception {
        if (StringUtils.isNotBlank(job.getSpringId())) {
            return QuartzUtils.getSpringTarget(job.getSpringId());
        }
        return QuartzUtils.getTargetByReflect(job.getBeanClass());
    }

    /**
     * 初始化代理
     * @param <T>
     * @param job
     * @param trigger
     * @param target
     * @param jobExecutionContext
     * @return
     * @throws IllegalArgumentException
     */
    private static <T> T newInstance(PeScheduleJob job, PeScheduleTrigger trigger, T target,
                                     JobExecutionContext jobExecutionContext) throws IllegalAccessException {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(target.getClass());
        enhancer.setCallback(new ExecuteProxy(job, trigger, jobExecutionContext));
        T proxy = (T) enhancer.create();
        QuartzUtils.copyMember(target, proxy);
        return proxy;
    }

    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        Object result = null;
        if (StringUtils.isBlank(this.job.getMethodName())
                || method.getName().equals(this.job.getMethodName())) {
            if (this.tryGenerateRecord()) {
                try {
                    result = this.execute(methodProxy, o, objects);
                    if (result != null && result instanceof Throwable) {
                        this.finalFailure((Throwable) result);
                    } else {
                        this.finalSuccess();
                    }
                } catch (Throwable t) {
                    logger.error("schedule execute error", t);
                    this.finalFailure((Throwable) result);
                }
            }
        }
        return result;
    }

    /**
     * 使用future模式执行任务
     *
     * 使用future模式与声明式事务配合进行事务隔离
     * @param methodProxy
     * @param o
     * @param objects
     * @return
     * @throws ExecutionException
     * @throws InterruptedException
     */
    private Object execute(MethodProxy methodProxy, Object o, Object[] objects)
            throws ExecutionException, InterruptedException {
        FutureTask<?> futureTask = new FutureTask<>(() -> {
            try {
                return methodProxy.invokeSuper(o, objects);
            } catch (Throwable t) {
                return t;
            }
        });
        executorService.execute(futureTask);
        return futureTask.get();
    }

    /**
     * 尝试创建记录并返回结果
     * @return
     */
    private boolean tryGenerateRecord() throws SocketException, UnknownHostException, MalformedObjectNameException {
        if (!this.job.isSingleton()) {
            return true;
        }
        this.record = PeScheduleJobRecord.tryRecord(this.job, this.trigger, this.jobExecutionContext.getFireTime());
        return this.record != null;
    }

    /**
     * 成功完成
     */
    private void finalSuccess() {
        this.finalJob(JOB_STATUS_SUCCESS, null);
    }

    /**
     * 执行失败
     * @param t
     */
    private void finalFailure(Throwable t) {
        this.finalJob(JOB_STATUS_FAILURE, t);
    }

    /**
     * 完成任务
     * @param code
     * @param t
     */
    private void finalJob(String code, Throwable t) {
        if (this.record != null) {
            this.record.finalJob(code, t, this.jobExecutionContext.getNextFireTime());
        }
    }

    private ExecuteProxy(PeScheduleJob job, PeScheduleTrigger trigger, JobExecutionContext jobExecutionContext) {
        this.job = job;
        this.trigger = trigger;
        this.jobExecutionContext = jobExecutionContext;
    }

}
