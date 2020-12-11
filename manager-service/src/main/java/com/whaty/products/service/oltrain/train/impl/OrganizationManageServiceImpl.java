package com.whaty.products.service.oltrain.train.impl;

import com.whaty.constant.CommonConstant;
import com.whaty.dao.GeneralDao;
import com.whaty.domain.bean.online.OlPeOrganization;
import com.whaty.framework.grid.adapter.service.impl.TycjGridServiceAdapter;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 在线培训机构管理服务类
 *
 * @author suoqiangqiang
 */
@Lazy
@Service("organizationManageService")
public class OrganizationManageServiceImpl extends TycjGridServiceAdapter<OlPeOrganization> {

    @Resource(name = CommonConstant.GENERAL_DAO_BEAN_NAME)
    private GeneralDao myGeneralDao;
}
