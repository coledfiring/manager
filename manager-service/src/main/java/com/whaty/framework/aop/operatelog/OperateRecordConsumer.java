package com.whaty.framework.aop.operatelog;

import com.whaty.designer.consumer.AbstractConsumer;
import com.whaty.designer.deal.AbstractDaemonThreadDeal;
import com.whaty.framework.aop.operatelog.constant.OperateRecordConstant;
import com.whaty.framework.aop.operatelog.domain.AbstractAsynchronousRecord;
import com.whaty.framework.aop.operatelog.resource.OperateRecordStore;
import com.whaty.util.CommonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.concurrent.BlockingQueue;

/**
 * 操作日志消费者
 *
 * @author weipengsen
 */
@Component(OperateRecordConstant.BEAN_NAME_OPERATE_RECORD_CONSUMER)
public class OperateRecordConsumer extends AbstractConsumer<AbstractAsynchronousRecord>
        implements AbstractDaemonThreadDeal, InitializingBean {

    private final static Logger logger = LoggerFactory.getLogger(OperateRecordConsumer.class);

    @Override
    protected void consumeResource(AbstractAsynchronousRecord resource) {
        resource.record();
    }

    @Override
    protected BlockingQueue<AbstractAsynchronousRecord> getQueueStore() {
        return OperateRecordStore.OPERATE_RECORD_QUEUE;
    }

    @Override
    public void doBeforeLoop() {}

    @Override
    public void doBeforeCreateThread(Thread daemonThread) {}

    @Override
    public String getThreadName() {
        try {
            return OperateRecordConstant.CONSUMER_THREAD_NAME + CommonUtils.getServerIp();
        } catch (SocketException | UnknownHostException e) {
            if (logger.isErrorEnabled()) {
                logger.error("create new collect consumer thread name error", e);
            }
            return OperateRecordConstant.CONSUMER_THREAD_NAME;
        }
    }

    @Override
    public void deal() {
        try {
            this.consume();
        } catch (InterruptedException e) {
            if (logger.isErrorEnabled()) {
                logger.error("the consumer for operate collect is interrupted", e);
            }
        } catch (Exception e) {
            if (logger.isErrorEnabled()) {
                logger.error("error in the consumer for operate collect", e);
            }
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.start();
    }
}
