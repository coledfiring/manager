package com.whaty.products.service.siteManager.impl;

import com.whaty.constant.CommonConstant;
import com.whaty.core.framework.api.exception.ApiException;
import com.whaty.core.framework.user.service.UserService;
import com.whaty.core.framework.util.BeanNames;
import com.whaty.dao.GeneralDao;
import com.whaty.framework.config.util.SiteUtil;
import com.whaty.products.service.siteManager.SiteRoleMenuManegeService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 站点业务库角色菜单管理服务类
 * @author suoqiangqiang
 */
@Lazy
@Service("siteRoleMenuManegeService")
public class SiteRoleMenuManegeServiceImpl implements SiteRoleMenuManegeService {

    @Resource(name = CommonConstant.GENERAL_DAO_BEAN_NAME)
    private GeneralDao myGeneralDao;

    @Resource(name = BeanNames.USER_SERVICE)
    private UserService userService;

    @Override
    public List getAllRole(String siteId) throws ApiException {
        List roleList = myGeneralDao.getByHQL(" select r from PePriRole r inner join r.enumConstByFlagRoleType e " +
                "where r.enumConstByFlagRoleType.code = '3' and r.siteCode = ? ", SiteUtil.getSiteCode());
        if (CollectionUtils.isEmpty(roleList)) {
            throw new ApiException(CommonConstant.PARAM_ERROR);
        }
        return roleList;
    }

    @Override
    public boolean checkIsHavePermission() throws ApiException {
        String roleId = userService.getCurrentUser().getRole().getId();
        List roleList = myGeneralDao
                .getBySQL("select 1 from pe_pri_role role " +
                        "INNER JOIN enum_const em on em.id=role.FLAG_ROLE_TYPE AND em.code='9998' " +
                        "where role.id = '" + roleId + "'");
        //只有站点超管用户可以操作此列表
        return CollectionUtils.isNotEmpty(roleList);
    }

}