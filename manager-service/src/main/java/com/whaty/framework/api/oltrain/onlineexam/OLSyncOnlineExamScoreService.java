package com.whaty.framework.api.oltrain.onlineexam;

import java.util.Map;

/**
 * 同步在线考试成绩信息服务类
 *
 * @author suoqiangqiang
 */
public interface OLSyncOnlineExamScoreService {

    /**
     * 同步在线考试成绩信息
     * @param requestMap
     * @return
     */
    Map<String, String> doSyncOnlineExamScore(Map<String, String> requestMap);
}
