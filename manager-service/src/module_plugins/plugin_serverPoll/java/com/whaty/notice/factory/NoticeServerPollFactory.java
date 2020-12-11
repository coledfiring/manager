package com.whaty.notice.factory;

import com.whaty.cache.service.RedisCacheService;
import com.whaty.framework.common.spring.SpringUtil;
import com.whaty.framework.config.util.SiteUtil;
import com.whaty.notice.constant.BeanNamesConstant;
import com.whaty.notice.constant.NoticeServerPollConstant;
import com.whaty.notice.exception.NoticeServerPollException;
import com.whaty.notice.server.NoticeServerPollServer;
import com.whaty.notice.util.NoticeServerPollUtils;
import com.whaty.serverpoll.factory.ServerPollFactory;
import com.whaty.serverpoll.plugins.check.AbstractActiveAndIdlePool;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * 个人中心信息服务器端,用于个人中心的信息推送
 * @author weipengsen
 */
@Lazy
@Component("noticeServerPollFactory")
public class NoticeServerPollFactory extends AbstractActiveAndIdlePool
        implements ServerPollFactory<NoticeServerPollServer>, InitializingBean {

    /**
     * 配置文件位置
     */
    private static final String NOTICE_CONFIG_PATH = "notice/notice.properties";
    /**
     * 生成实例的初始化请求超时时间
     */
    private long waitTimeout;
    /**
     *  回收周期时间
     */
    private long checkDestroyTime;
    /**
     * 生成实例的初始化循环检查标志时间
     */
    private long checkFlagTime;
    /**
     * 一个用户允许同时开启的最大服务端数
     */
    private int userServerNum;
    /**
     * 定时检测删除超时通知信息的配置小时数,0-24对应每天0-24时
     */
    private int delHour;

    /**
     * 无效站点
     */
    private String disable;
    /**
     * 上次检查的小时
     */
    private int hour;

    @Autowired
    private RedisCacheService redisCacheService;

    @Override
    public String getThreadName() {
        return NoticeServerPollConstant.NOTICE_DESTROY_THREAD_NAME;
    }

    @Override
    public void deal() {
        super.deal();
        //检测并且删除通知信息
        checkAndDelNotice();
        //经历过一次检查后进行睡眠
        try {
            Thread.sleep(checkDestroyTime);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 定时并删除超过限制的通知信息
     */
    private void checkAndDelNotice() {
        try {
            //拿到当前的小时数
            int nowHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
            if (nowHour != this.hour) {
                this.hour = nowHour;
                //判断是否到达检测时间
                if (this.hour == this.delHour) {
                    NoticeServerPollUtils.delTimeoutNotice();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Properties properties = new Properties();
        // 加载配置文件
        properties.load(Thread.currentThread().getContextClassLoader()
                .getResourceAsStream(NOTICE_CONFIG_PATH));
        this.loadConfig(properties);
        // 启动销毁线程
        this.start();
    }

    /**
     * 加载启动配置
     * @param properties
     */
    private void loadConfig(Properties properties) {
        //将参数放到对象中
        this.idleTimeout = Long.parseLong(properties
                .getProperty(NoticeServerPollConstant.NOTICE_PROPERTIES_IDLE_TIMEOUT));
        this.checkDestroyTime = Long.parseLong(properties
                .getProperty(NoticeServerPollConstant.NOTICE_PROPERTIES_CHECK_DESTROY_TIME));
        this.waitTimeout = Long.parseLong(properties
                .getProperty(NoticeServerPollConstant.NOTICE_PROPERTIES_WAIT_TIME_OUT));
        this.checkFlagTime = Long.parseLong(properties
                .getProperty(NoticeServerPollConstant.NOTICE_PROPERTIES_CHECK_FLAG_TIME));
        this.idleMaxQueueSize = Long.parseLong(properties
                .getProperty(NoticeServerPollConstant.NOTICE_PROPERTIES_IDLE_MAX_QUEUE_SIZE));
        this.userServerNum = Integer.parseInt(properties
                .getProperty(NoticeServerPollConstant.NOTICE_PROPERTIES_USER_SERVER_NUM));
        this.delHour = Integer.parseInt(properties
                .getProperty(NoticeServerPollConstant.NOTICE_PROPERTIES_DEL_HOUR));
    }

    @Override
    public NoticeServerPollServer newInstance(Map<String, Object> args) throws NoticeServerPollException {
        //拿出用户id与注册id
        String userId = (String)args.get(NoticeServerPollConstant.NOTICE_PARAM_USER_ID);
        if(StringUtils.isBlank((String)args.get(NoticeServerPollConstant.NOTICE_PARAM_REGISTER_ID))
                || StringUtils.isBlank((String) args.get(NoticeServerPollConstant.NOTICE_PARAM_CACHE_KEY))
                || args.get(NoticeServerPollConstant.NOTICE_PARAM_IS_NEW) == null) {
            throw new NoticeServerPollException(NoticeServerPollConstant.NOTICE_INFO_ARGS_ERROR);
        }
        String cacheKey = (String) args.get(NoticeServerPollConstant.NOTICE_PARAM_CACHE_KEY);
        String registerId = (String)args.get(NoticeServerPollConstant.NOTICE_PARAM_REGISTER_ID);
        boolean isNew = Boolean.parseBoolean((String) args.get(NoticeServerPollConstant.NOTICE_PARAM_IS_NEW));
        //使用用户id与注册id读取缓存，查找是否有对应通知服务端对象
        NoticeServerPollServer server;
        if(!isNew) {
            //如果不是第一次进入，先查询缓存
            synchronized (registerId.intern()) {
                //从map中拿出设置为using的过程和销毁进程中的销毁过程应该是互斥的
                server = (NoticeServerPollServer) activeQueue.get(registerId);
                if (server != null) {
                    //设置服务端为正在使用
                    server.setUsing(true);
                    return server;
                }
            }
        }
        //缓存为空或是新的注册客户端
        //判断是否超过了用户同时开启服务端的次数
        if(!this.checkUserServerNumLimit(cacheKey)) {
            throw new NoticeServerPollException(NoticeServerPollConstant.NOTICE_INFO_USER_SERVER_NUM);
        }
        //不存在对应对象,查询空闲队列是否为空
        if(idlePool.size() != 0) {
            //队列不为空,从空闲队列队首拿出一个服务端对象
            server = (NoticeServerPollServer) idlePool.poll();
            //重新初始化服务端
            server.reInitServer(userId, registerId, cacheKey);
        } else {
            //新建对象，使用配置初始化对象数据并返回
            server = (NoticeServerPollServer) SpringUtil.getBean(BeanNamesConstant.NOTICE_SERVICE_BEAN_NAME);
            server.initServer(userId, registerId, this.waitTimeout,
                    this.checkFlagTime, cacheKey);
        }
        //将新对象放入活动map
        activeQueue.put(registerId, server);
        //设置服务端为正在使用
        server.setUsing(true);
        return server;
    }

    /**
     * 检测用户当前
     * @param cacheKey
     * @return
     */
    private boolean checkUserServerNumLimit(String cacheKey) {
        if(MapUtils.isNotEmpty(activeQueue)) {
            Map<String, Object> map = this.redisCacheService.getMap(cacheKey);
            if(MapUtils.isNotEmpty(map)) {
                Set<String> registerSet = map.keySet();
                int usingNum = 0;
                for (String register : registerSet) {
                    if (activeQueue.get(register) != null
                            && activeQueue.get(register).isUsing()) {
                        usingNum++;
                    }
                }
                if (usingNum > userServerNum) {
                    return false;
                }
            }
        }
        return true;
    }

}
