package com.whaty.products.service.user;

import java.util.List;

/**
 * 用户数据操作服务类
 * @author suoqiangqiang
 */
public interface UserDataOperateService {

    /**
     * 清除用户缓存
     * @param loginIdList
     * @param serverName
     */
    void removeUserCache(List<String> loginIdList,String serverName);
}
