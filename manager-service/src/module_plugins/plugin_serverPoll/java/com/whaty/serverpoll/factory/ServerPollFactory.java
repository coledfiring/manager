package com.whaty.serverpoll.factory;

import com.whaty.notice.exception.NoticeServerPollException;
import com.whaty.serverpoll.server.AbstractServerPollServer;

import java.util.Map;

/**
 * 服务器推工厂接口，是各类服务器推服务端工厂的超类，用于规范实例返回的类型超类
 * @author weipengsen
 */
public interface ServerPollFactory<T extends AbstractServerPollServer> {

    /**
     * 获取实例方法
     * @param args 创建实例使用的参数
     * @return T为服务器推服务端SendPollServer的子类
     * @throws NoticeServerPollException
     */
    T newInstance(Map<String, Object> args) throws NoticeServerPollException;

}
