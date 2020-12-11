package com.whaty.products.service.oltrain.clazz.impl;

import com.whaty.constant.CommonConstant;
import com.whaty.dao.GeneralDao;
import com.whaty.domain.bean.online.OlPeStudent;
import com.whaty.framework.grid.adapter.service.impl.TycjGridServiceAdapter;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;


/**
 * 在线培训学员查询服务类
 *
 * @author suoqiangqiang
 */
@Lazy
@Service("olClassStudentManageSearchService")
public class OLClassStudentSearchServiceImpl extends TycjGridServiceAdapter<OlPeStudent> {

    @Resource(name = CommonConstant.GENERAL_DAO_BEAN_NAME)
    private GeneralDao myGeneralDao;
}
