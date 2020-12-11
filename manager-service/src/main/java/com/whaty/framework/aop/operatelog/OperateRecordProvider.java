package com.whaty.framework.aop.operatelog;

import com.whaty.designer.provider.AbstractProvider;
import com.whaty.framework.aop.operatelog.constant.OperateRecordConstant;
import com.whaty.framework.aop.operatelog.domain.AbstractAsynchronousRecord;
import com.whaty.framework.aop.operatelog.resource.OperateRecordStore;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.concurrent.BlockingQueue;

/**
 * 操作日志生产者
 * @author weipengsen
 */
@Lazy
@Component(OperateRecordConstant.BEAN_NAME_OPERATE_RECORD_PROVIDER)
public class OperateRecordProvider extends AbstractProvider<AbstractAsynchronousRecord, AbstractAsynchronousRecord> {

    @Override
    protected BlockingQueue<AbstractAsynchronousRecord> getQueueStore() {
        return OperateRecordStore.OPERATE_RECORD_QUEUE;
    }

    @Override
    protected AbstractAsynchronousRecord generateResource(AbstractAsynchronousRecord args) {
        return args;
    }

}
