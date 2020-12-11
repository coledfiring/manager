package com.whaty.utils;

import com.whaty.domain.bean.PeManager;
import com.whaty.domain.bean.PeUnit;
import com.whaty.domain.bean.SsoUser;

/**
 * 用户相关工具类
 *
 * @author weipengsen
 */
public class UserUtils {

    /**
     * 获取当前用户的单位
     * @return
     */
    public static PeUnit getCurrentUnit() {
        return getCurrentManager().getPeUnit();
    }

    /**
     * 获取当前管理端
     * @return
     */
    public static PeManager getCurrentManager() {
        return (PeManager) StaticBeanUtils.getGeneralDao().getOneByHQL("from PeManager where ssoUser.id = ?",
                StaticBeanUtils.getUserService().getCurrentUser().getId());
    }

    /**
     * 获取当前用户
     * @return
     */
    public static SsoUser getCurrentUser() {
        return StaticBeanUtils.getGeneralDao().getById(SsoUser.class,
                StaticBeanUtils.getUserService().getCurrentUser().getId());
    }

    /**
     * 获取当前用户id
     * @return
     */
    public static String getCurrentUserId() {
        return StaticBeanUtils.getUserService().getCurrentUser().getId();
    }
}
