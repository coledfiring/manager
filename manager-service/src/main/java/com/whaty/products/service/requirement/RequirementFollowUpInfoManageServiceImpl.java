package com.whaty.products.service.requirement;

import com.whaty.constant.CommonConstant;
import com.whaty.core.commons.exception.EntityException;
import com.whaty.dao.GeneralDao;
import com.whaty.domain.bean.RequirementFollowUpInfo;
import com.whaty.framework.grid.twolevellist.service.impl.AbstractTwoLevelListGridServiceImpl;
import com.whaty.utils.UserUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 需求追踪信息管理服务类
 *
 * @author suoqiangqiang
 */
@Lazy
@Service("requirementFollowUpInfoManageService")
public class RequirementFollowUpInfoManageServiceImpl extends AbstractTwoLevelListGridServiceImpl<RequirementFollowUpInfo> {
    @Resource(name = CommonConstant.GENERAL_DAO_BEAN_NAME)
    protected GeneralDao myGeneralDao;

    @Override
    protected String getParentIdBeanPropertyName() {
        return "requirementInfo.id";
    }

    @Override
    protected String getParentIdSearchParamName() {
        return "requirementInfo.id";
    }

    @Override
    public void checkBeforeAdd(RequirementFollowUpInfo bean, Map<String, Object> params) throws EntityException {
        super.checkBeforeAdd(bean, params);
        this.checkBeforeAddOrUpdate(bean);
        bean.setPeManager(UserUtils.getCurrentManager());
        bean.setCreateTime(new Date());
    }

    @Override
    public void checkBeforeUpdate(RequirementFollowUpInfo bean) throws EntityException {
        this.checkBeforeAddOrUpdate(bean);
        bean.setUpdateTime(new Date());

    }

    @Override
    public void checkBeforeDelete(List idList) throws EntityException {
        super.checkBeforeDelete(idList);
    }

    /**
     * 更新、增加前检查
     *
     * @param bean
     */
    private void checkBeforeAddOrUpdate(RequirementFollowUpInfo bean) throws EntityException {
        String followUpStatusCode = this.myGeneralDao.getOneBySQL(" select ft.code from requirement_info ri " +
                " inner join enum_const ft on ft.id=ri.flag_requirement_status" +
                " where ri.id = ? ", bean.getRequirementInfo().getId());
        if (!"3".equals(followUpStatusCode)) {
            throw new EntityException("只有确认需求后才能继续操作");
        }
    }
}
