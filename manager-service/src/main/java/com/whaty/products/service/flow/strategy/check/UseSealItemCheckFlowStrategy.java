package com.whaty.products.service.flow.strategy.check;

import com.whaty.constant.CommonConstant;
import com.whaty.dao.GeneralDao;
import com.whaty.framework.config.util.SiteUtil;
import com.whaty.products.service.flow.domain.tab.AbstractBaseTab;
import com.whaty.products.service.flow.domain.tab.BaseInfoTab;
import com.whaty.products.service.flow.domain.tab.FileListTab;
import com.whaty.products.service.flow.helper.TabHelper;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 用印管理审批流程策略
 *
 * @author pingzhihao
 */
@Lazy
@Component("useSealItemCheckFlowStrategy")
public class UseSealItemCheckFlowStrategy extends AbstractCheckFlowStrategy {

    @Resource(name = CommonConstant.GENERAL_DAO_BEAN_NAME)
    private GeneralDao generalDao;

    /**
     * 更新审批状态
     *
     * @param id
     * @param statusCode
     */
    private void updateUseStatus(String id, String statusCode) {
        this.generalDao.executeBySQL("UPDATE use_seal us INNER JOIN enum_const cs " +
                " ON cs.namespace = 'flagCheckStatus' AND cs.CODE = ? " +
                " SET us.flag_check_status = cs.id WHERE us.id = ?", statusCode, id);
    }

    @Override
    public Boolean checkCanApply(String itemId) {
        return this.generalDao.checkNotEmpty("SELECT 1 FROM use_seal us " +
                "left JOIN enum_const cs ON cs.id = us.flag_check_status " +
                "WHERE (us.flag_check_status is null or cs.code in ('3','4') ) AND us.id = ?", itemId);
    }

    @Override
    public void applyCheck(String itemId) {
        this.updateUseStatus(itemId, "1");
    }

    @Override
    public void passCheck(String itemId) {
        this.updateUseStatus(itemId, "2");
    }

    @Override
    public void cancelCheck(String itemId) {
        this.updateUseStatus(itemId, "4");
    }

    @Override
    public void noPassCheck(String itemId) {
        this.updateUseStatus(itemId, "3");
    }

    @Override
    public String getItemName(String itemId) {
        return "用印申请审批";
    }

    @Override
    public List<AbstractBaseTab> getBusiness(String itemId) {
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT                                                                  ");
        sql.append("   pm.`name` AS `申请人`,                                                 ");
        sql.append("   pm.mobile AS `申请人手机号`,                                            ");
        sql.append("   us.applicant_time AS `申请时间`,                                       ");
        sql.append("   pu.`name` AS `申请单位`,                                               ");
        sql.append("   IFNULL(dt.name,'') as `申请部门`,                                      ");
        sql.append("   ec1.`NAME` AS `用印类型`,                                              ");
        sql.append("   us.note AS `用印事由`                                                  ");
        sql.append(" FROM                                                                    ");
        sql.append(" 	use_seal us                                                          ");
        sql.append(" INNER JOIN pe_manager pm ON pm.fk_sso_user_id = us.fk_applicant_user_id ");
        sql.append(" INNER JOIN pe_unit pu ON pm.fk_unit_id = pu.id                          ");
        sql.append(" INNER JOIN enum_const ec1 ON ec1.id = us.flag_use_seal_type             ");
        sql.append(" LEFT JOIN enum_const dt ON dt.id = pm.flag_department_type              ");
        sql.append(" where  us.id = ?                                                        ");
        Map<String, Object> baseInfo = this.generalDao.getOneMapBySQL(sql.toString(), itemId);
        String[] baseHeader = {"申请人", "申请人手机号", "申请时间", "申请单位", "申请部门", "用印类型", "用印事由"};
        //获取附件列表信息
        return Arrays.asList(new BaseInfoTab("基础信息", baseHeader, baseInfo),
                TabHelper.buildFileListTab("附件列表", itemId, "useSealManage"));
    }

    @Override
    public String getScopeId(String itemId) {
        return this.generalDao.getOneBySQL("select fk_applicant_unit from use_seal where id = ?", itemId);
    }
}
