package com.whaty.products.service.requirement;

import com.whaty.constant.CommonConstant;
import com.whaty.constant.EnumConstConstants;
import com.whaty.core.commons.exception.EntityException;
import com.whaty.dao.GeneralDao;
import com.whaty.domain.bean.RequirementInfo;
import com.whaty.framework.asserts.TycjParameterAssert;
import com.whaty.framework.exception.ServiceException;
import com.whaty.framework.grid.adapter.service.impl.TycjGridServiceAdapter;
import com.whaty.util.CommonUtils;
import com.whaty.utils.UserUtils;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * 需求追踪列表服务类
 *
 * @author suoqiangqiang
 */
@Lazy
@Service("requirementFollowUpListService")
public class RequirementFollowUpListServiceImpl extends TycjGridServiceAdapter<RequirementInfo> {
    @Resource(name = CommonConstant.GENERAL_DAO_BEAN_NAME)
    protected GeneralDao myGeneralDao;

    @Override
    public void checkBeforeUpdate(RequirementInfo bean) throws EntityException {
        if (!"3".equals(bean.getEnumConstByFlagRequirementStatus().getCode())) {
            throw new EntityException("只有确认需求后才能继续操作");
        }
        bean.setUpdateTime(new Date());
    }

    /**
     * 确认接受需求
     *
     * @param ids
     * @return
     */
    public int doConfirmRequirement(String ids) {
        TycjParameterAssert.isAllNotBlank(ids);
        List checkList = this.myGeneralDao
                .getBySQL("select 1 from requirement_info where ? and fk_follow_up_user_id<>?",
                        CommonUtils.madeSqlIn(ids, "id"), UserUtils.getCurrentManager().getId());
        if (CollectionUtils.isNotEmpty(checkList)) {
            throw new ServiceException("只能接受分派给本人的需求");
        }
        String requirementStatusId = this.myGeneralDao
                .getEnumConstByNamespaceCode(EnumConstConstants.ENUM_CONST_NAMESPACE_REQUIREMENT_STATUS, "3").getId();
        String followUpStatusId = this.myGeneralDao
                .getEnumConstByNamespaceCode(EnumConstConstants.ENUM_CONST_NAMESPACE_FOLLOW_UP_STATUS, "2").getId();
        return this.myGeneralDao.executeBySQL("update requirement_info set " +
                " accept_time=now(),Flag_Requirement_Status=?,Flag_Follow_Up_Status=? where "
                + CommonUtils.madeSqlIn(ids, "id"), requirementStatusId, followUpStatusId);
    }
}
