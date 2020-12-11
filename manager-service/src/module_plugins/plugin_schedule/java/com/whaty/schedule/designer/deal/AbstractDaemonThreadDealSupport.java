package com.whaty.schedule.designer.deal;

/**
 * 守护线程调度的服务提供类
 *
 * @author weipengsen
 */
public abstract class AbstractDaemonThreadDealSupport implements AbstractDaemonThreadDeal {

    /**
     * 循环开始前执行
     */
    @Override
    public void doBeforeLoop() {}

    /**
     * 线程创建前执行
     * @param daemonThread
     */
    @Override
    public void doBeforeCreateThread(Thread daemonThread) {}

}
