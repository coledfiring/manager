package com.whaty.products.service.workspace;

import com.whaty.constant.CommonConstant;
import com.whaty.core.commons.exception.EntityException;
import com.whaty.core.framework.user.service.UserService;
import com.whaty.core.framework.util.BeanNames;
import com.whaty.dao.GeneralDao;
import com.whaty.domain.bean.PolicyRegulation;
import com.whaty.domain.bean.PolicyRegulationExtend;
import com.whaty.framework.asserts.TycjParameterAssert;
import com.whaty.framework.grid.adapter.service.impl.TycjGridServiceAdapter;
import com.whaty.util.CommonUtils;
import com.whaty.utils.UserUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 政策法规服务类
 *
 * @author shangyu
 */
@Lazy
@Service("policyRegulationService")
public class PolicyRegulationServiceImpl extends TycjGridServiceAdapter<PolicyRegulation> {

    @Resource(name = CommonConstant.GENERAL_DAO_BEAN_NAME)
    private GeneralDao myGeneralDao;

    @Resource(name = BeanNames.USER_SERVICE)
    private UserService userService;

    @Override
    public void checkBeforeAdd(PolicyRegulation bean, Map<String, Object> params) throws EntityException {
        super.checkBeforeAdd(bean, params);
        bean.setCreateBy(UserUtils.getCurrentUser());
        bean.setCreateDate(new Date());
    }

    /**
     * 获取政策法规内容
     * @param extendId
     * @return
     */
    public Map<String, Object> getPolicyRegulationContent(String extendId) {
        String content = this.myGeneralDao
                .getOneBySQL("select content from policy_regulation_extend where id = ?", extendId);
        return Collections.singletonMap("policyRegulationContent", content);
    }

    /**
     * 保存政策法规内容
     * @param id
     * @param content
     */
    public void savePolicyRegulationContent(String id, String content) {
        TycjParameterAssert.isAllNotBlank(id, content);
        String extendId = this.myGeneralDao
                .getOneBySQL("select fk_file_extend_id from policy_regulation where id = ?", id);
        PolicyRegulationExtend extend = new PolicyRegulationExtend();
        extend.setContent(content);
        if (StringUtils.isNotBlank(extendId)) {
            extend.setId(extendId);
        }
        this.myGeneralDao.save(extend);
        this.myGeneralDao.flush();
        if (StringUtils.isBlank(extendId)) {
            this.myGeneralDao.executeBySQL("update policy_regulation set fk_file_extend_id = ? where id = ?",
                    extend.getId(), id);
        }
    }

    @Override
    public void checkBeforeDelete(List idList) throws EntityException {
        if (this.myGeneralDao.checkNotEmpty("select 1 from policy_regulation pr " +
                " INNER JOIN enum_const ec ON ec.id = pr.flag_active " +
                " WHERE ec.code = '1' AND " + CommonUtils.madeSqlIn(idList, "pr.id"))) {
            throw new EntityException("存在有效数据不可删除");
        }
    }
}
