package com.whaty.framework.aop.operatelog.domain;

/**
 * 可记录行为接口
 *
 * @author weipengsen
 */
public interface ICanRecord {

    /**
     * 记录
     */
    void record();

    /**
     * 推送数据到生产者
     */
    void offerToProvider();
}
