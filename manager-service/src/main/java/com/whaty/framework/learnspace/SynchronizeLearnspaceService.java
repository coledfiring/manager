package com.whaty.framework.learnspace;

import java.util.List;
import java.util.Map;

/**
 * 课程空间同步操作工具类
 * @author suoqiangqiang
 */
public interface SynchronizeLearnspaceService {

    /**
     * 批量更新管理员
     * @param peManagerList
     * @param defaultPwd
     * @param learnSpaceSiteCode
     */
    void updateLearnSpaceManager(List<Map<String, Object>> peManagerList, String defaultPwd, String learnSpaceSiteCode);
}
