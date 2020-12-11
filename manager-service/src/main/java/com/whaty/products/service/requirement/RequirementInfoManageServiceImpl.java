package com.whaty.products.service.requirement;

import com.whaty.common.string.StringUtils;
import com.whaty.constant.CommonConstant;
import com.whaty.constant.EnumConstConstants;
import com.whaty.core.commons.datasource.MasterSlaveRoutingDataSource;
import com.whaty.core.commons.exception.EntityException;
import com.whaty.core.framework.async.Task;
import com.whaty.core.framework.bean.Site;
import com.whaty.core.framework.grid.bean.GridConfig;
import com.whaty.dao.GeneralDao;
import com.whaty.domain.bean.RequirementInfo;
import com.whaty.framework.asserts.TycjParameterAssert;
import com.whaty.framework.config.util.PlatformConfigUtil;
import com.whaty.framework.config.util.SiteUtil;
import com.whaty.framework.grid.adapter.service.impl.TycjGridServiceAdapter;
import com.whaty.framework.scope.util.ScopeHandleUtils;
import com.whaty.products.service.message.facade.SendMessageFacade;
import com.whaty.util.CommonUtils;
import com.whaty.utils.UserUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 需求管理服务类
 *
 * @author suoqiangqiang
 */
@Lazy
@Service("requirementInfoManageService")
public class RequirementInfoManageServiceImpl extends TycjGridServiceAdapter<RequirementInfo> {
    @Resource(name = CommonConstant.GENERAL_DAO_BEAN_NAME)
    protected GeneralDao myGeneralDao;

    private final static Logger logger = LoggerFactory.getLogger(RequirementInfoManageServiceImpl.class);

    @Override
    protected List<RequirementInfo> readExcelToBean(Task task, File excelFile, Workbook workbook, GridConfig gridConfig,
                                                    Map<String, Object> params, Site site) throws EntityException {
        List<RequirementInfo> beanList = super.readExcelToBean(task, excelFile, workbook, gridConfig, params, site);
        String code = CommonUtils.changeDateToString(new Date(), "yyyy");
        int maxCode = Integer.parseInt(this.generateSerial().replace(code, ""));
        for (RequirementInfo bean : beanList) {
            bean.setSerial(code + CommonUtils.leftAddZero(maxCode + 1, 4));
            maxCode++;
        }
        return beanList;
    }

    @Override
    public void checkBeforeAdd(RequirementInfo bean) throws EntityException {
        bean.setCreateTime(new Date());
        bean.setCreateUser(UserUtils.getCurrentManager());
        bean.setEnumConstByFlagRequirementStatus(this.myGeneralDao
                .getEnumConstByNamespaceCode("FlagRequirementStatus", "1"));
        bean.setEnumConstByFlagFollowUpStatus(this.myGeneralDao
                .getEnumConstByNamespaceCode("FlagFollowUpStatus", "1"));
        bean.setSerial(this.generateSerial());
    }

    @Override
    public void checkBeforeUpdate(RequirementInfo bean) throws EntityException {
        bean.setUpdateTime(new Date());
    }

    /**
     * 获取权限范围内有效管理员
     *
     * @return
     */
    public List<Object[]> getManager() {
        String activeId = this.myGeneralDao
                .getEnumConstByNamespaceCode(EnumConstConstants.ENUM_CONST_NAMESPACE_ACTIVE, "1").getId();
        return this.myGeneralDao
                .getBySQL(ScopeHandleUtils.handleScopeSignOfSql("select id,name from pe_manager " +
                                "where flag_active='" + activeId + "' and [peUnit|fk_unit_id]" +
                                " and site_code = '" + SiteUtil.getSiteCode() + "'",
                        UserUtils.getCurrentUserId()));
    }

    /**
     * 需求分派
     *
     * @param ids
     * @param followUpUserId
     */
    public int doArrangeManager(String ids, String followUpUserId) {
        TycjParameterAssert.isAllNotBlank(ids, followUpUserId);
        String reStatusId = this.myGeneralDao
                .getEnumConstByNamespaceCode(EnumConstConstants.ENUM_CONST_NAMESPACE_REQUIREMENT_STATUS, "2").getId();
        int count = this.myGeneralDao.executeBySQL(" update requirement_info set fk_follow_up_user_id = ?," +
                "flag_requirement_status = ? where " + CommonUtils.madeSqlIn(ids, "id"), followUpUserId, reStatusId);
        if (PlatformConfigUtil.isWeChatSiteOpen(SiteUtil.getSiteCode())) {
            try {
                new SendMessageFacade().noticeWeChatTemplate("notifyRequirementAssign", ids, null,
                        Collections.singletonMap("followUpUserId", followUpUserId));
            } catch (Exception e) {
                if (logger.isInfoEnabled()) {
                    logger.info(e.getMessage());
                }
            }
        }
        return count;
    }

    /**
     * 生成编号
     *
     * @return
     */
    private String generateSerial() {
        String maxSerial = this.myGeneralDao
                .getOneBySQL(" SELECT cast((max(serial)+1) as char) FROM requirement_info WHERE " +
                                " serial like concat(DATE_FORMAT(now(),'%Y'),'%') and site_code = ?",
                        MasterSlaveRoutingDataSource.getDbType());
        if (StringUtils.isBlank(maxSerial)) {
            maxSerial = CommonUtils.changeDateToString(new Date(), "yyyy") + CommonUtils.leftAddZero(1, 4);
        }
        return maxSerial;
    }
}
