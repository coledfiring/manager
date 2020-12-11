package com.whaty.framework.learnspace.impl;

import com.whaty.constant.CommonConstant;
import com.whaty.framework.exception.LearningSpaceException;
import com.whaty.products.learning.webservice.LearningSpaceWebService;
import com.whaty.framework.learnspace.SynchronizeLearnspaceService;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 课程空间同步操作工具类
 * @author suoqiangqiang
 */
@Lazy
@Service("synchronizeLearnSpaceService")
public class SynchronizeLearnspaceServiceImpl implements SynchronizeLearnspaceService {

    @Resource(name = CommonConstant.LEARNING_SPACE_WEB_SERVICE_BEAN_NAME)
    private LearningSpaceWebService learningSpaceWebService;

    /**
     * 批量更新管理员
     * @param peManagerList
     * @param defaultPwd
     * @param learnSpaceSiteCode
     */
    public void updateLearnSpaceManager(List<Map<String, Object>> peManagerList, String defaultPwd, String learnSpaceSiteCode) {
        peManagerList.forEach(manager -> {
            String pwd = com.whaty.core.commons.util.CommonUtils.getMD5(manager.get("loginId") + defaultPwd);
            try {
                boolean flag = learningSpaceWebService.updateManager(
                        (String) manager.get("id"),
                        (String) manager.get("loginId"),
                        (String) manager.get("trueName"),
                        null,
                        (String) manager.get("code"),
                        pwd,
                        learnSpaceSiteCode
                );
                if (!flag) {
                    throw new LearningSpaceException();
                }
            } catch (Exception e) {
                //同步失败后进行数据回滚
                peManagerList.forEach(rawManager -> {
                    try {
                        boolean flag = learningSpaceWebService.updateManager(
                                (String) rawManager.get("id"),
                                (String) rawManager.get("loginId"),
                                (String) rawManager.get("trueName"),
                                null,
                                (String) rawManager.get("code"),
                                (String) rawManager.get("pwd"),
                                learnSpaceSiteCode
                        );
                        if (!flag) {
                            throw new LearningSpaceException();
                        }
                    } catch (Exception e1) {
                        throw new LearningSpaceException(e1);
                    }
                });
                throw new LearningSpaceException(e);
            }
        });
    }
}
