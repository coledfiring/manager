package com.whaty.schedule.designer.deal;

/**
 * 守护线程处理接口
 * @author weipengsen
 */
public interface AbstractDaemonThreadDeal {

    /**
     * 开启守护线程
     */
    default void start() {
        //启动守护线程
        Thread daemonThread = new Thread(() -> {
            this.doBeforeLoop();
            while(true) {
                this.deal();
            }
        });
        //设置线程名称
        daemonThread.setName(this.getThreadName());
        //设置线程为守护线程
        daemonThread.setDaemon(true);
        this.doBeforeCreateThread(daemonThread);
        //启动守护线程
        daemonThread.start();
    }

    /**
     * 循环开始前执行
     */
    void doBeforeLoop();

    /**
     * 线程创建前执行
     * @param daemonThread
     */
    void doBeforeCreateThread(Thread daemonThread);

    /**
     * 获取线程名，用于给守护线程命名
     * @return
     */
    String getThreadName();

    /**
     * 守护线程中的处理方法
     */
    void deal();

}
