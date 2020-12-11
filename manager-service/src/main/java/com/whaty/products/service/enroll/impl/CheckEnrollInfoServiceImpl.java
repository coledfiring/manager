package com.whaty.products.service.enroll.impl;

import com.whaty.constant.CommonConstant;
import com.whaty.constant.EnumConstConstants;
import com.whaty.constant.SiteConstant;
import com.whaty.core.commons.datasource.MasterSlaveRoutingDataSource;
import com.whaty.core.framework.bean.EnumConst;
import com.whaty.dao.GeneralDao;
import com.whaty.framework.asserts.TycjParameterAssert;
import com.whaty.framework.config.util.SiteUtil;
import com.whaty.framework.exception.ServiceException;
import com.whaty.products.service.enroll.CheckEnrollInfoService;
import com.whaty.products.service.enroll.domain.EnrollInfoModel;
import com.whaty.products.service.message.facade.SendMessageFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 审核报名信息
 *
 * @author weipengsen
 */
@Lazy
@Service("checkEnrollInfoService")
public class CheckEnrollInfoServiceImpl implements CheckEnrollInfoService {

    @Resource(name = CommonConstant.GENERAL_DAO_BEAN_NAME)
    private GeneralDao generalDao;

    @Resource(name = CommonConstant.OPEN_GENERAL_DAO_BEAN_NAME)
    private GeneralDao openGeneralDao;

    private final static Logger logger = LoggerFactory.getLogger(CheckEnrollInfoServiceImpl.class);

    @Override
    public EnrollInfoModel getEnrollInfo(String studentId) {
        TycjParameterAssert.isAllNotBlank(studentId);
        Integer regulationId = this.openGeneralDao
                .getOneBySQL("select ti.fk_enroll_regulation_id from pe_student stu " +
                        "inner join training_item ti on ti.id = stu.fk_training_item_id where stu.id = ?", studentId);
        MasterSlaveRoutingDataSource.setDbType(SiteConstant.SITE_CODE_CONTROL);
        if (Objects.isNull(regulationId)) {
            regulationId = this.openGeneralDao.getOneBySQL("select reg.id from enroll_column_regulation reg " +
                   "inner join enum_const de on de.id = reg.flag_is_default where de.code = '1' and reg.site_code = ? "
                    , SiteUtil.getSiteCode());
        }
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT                                                         ");
        sql.append(" 	'" + studentId + "' as `studentId`,                         ");
        sql.append(" 	ec.name as `name`,                                          ");
        sql.append(" 	ec.data_index as dataIndex,                                 ");
        sql.append(" 	ia.code as isAttachFile,                                    ");
        sql.append(" 	gr.name as groupName,                                       ");
        sql.append(" 	ty.code as columnType,                                      ");
        sql.append(" 	gr.code as groupCode,                                       ");
        sql.append(" 	de.serial_number as serialNumber                            ");
        sql.append(" FROM                                                           ");
        sql.append(" 	enroll_column_regulation_detail de                          ");
        sql.append(" INNER JOIN enroll_column ec ON ec.id = de.fk_column_id         ");
        sql.append(" INNER JOIN enum_const ty on ty.id = ec.flag_enroll_column_type ");
        sql.append(" INNER JOIN enum_const gr on gr.id = ec.flag_enroll_column_group");
        sql.append(" LEFT JOIN enum_const ia on ia.id = ec.flag_is_attach_file      ");
        sql.append(" where                                                          ");
        sql.append(" 	de.is_active = '1'                                          ");
        sql.append(" AND de.fk_regulation_id = ?                                    ");
        List<Map<String, Object>> columnList = this.openGeneralDao.getMapBySQL(sql.toString(), regulationId);
        MasterSlaveRoutingDataSource.setDbType(SiteUtil.getSiteCode());
        String itemName = this.openGeneralDao.getOneBySQL("select ti.name from pe_student stu " +
                "inner join training_item ti on ti.id = stu.fk_training_item_id where stu.id = ?", studentId);
        EnrollInfoModel model = EnrollInfoModel.convert(itemName, columnList);
        model.setStatus(this.openGeneralDao.getOneBySQL("select st.code from pe_student stu " +
                "inner join enum_const st on st.id = stu.flag_enroll_check_status where stu.id = ?", studentId));
        return model;
    }

    @Override
    public void doPass(String id) {
        TycjParameterAssert.isAllNotBlank(id);
        EnumConst pass = this.generalDao.getEnumConstByNamespaceCode(EnumConstConstants
                .ENUM_CONST_NAMESPACE_ENROLL_CHECK_STATUS, "2");
        this.generalDao.executeBySQL("update pe_student set flag_enroll_check_status = ? where id = ?",
                pass.getId(), id);
        this.noticeWeChat(id);
    }

    @Override
    public void doNoPass(String id, String reason) {
        TycjParameterAssert.isAllNotBlank(id);
        EnumConst noPass = this.generalDao.getEnumConstByNamespaceCode(EnumConstConstants
                .ENUM_CONST_NAMESPACE_ENROLL_CHECK_STATUS, "3");
        this.generalDao.executeBySQL("update pe_student set flag_enroll_check_status = ?, enroll_check_note = ? " +
                        "where id = ?", noPass.getId(), reason, id);
        this.noticeWeChat(id);
    }

    /**
     * 发送微信通知
     * @param id
     */
    private void noticeWeChat(String id) {
        try {
            new SendMessageFacade().noticeWeChatTemplate("noticeEnrollCheck", id, null, null);
        } catch (ServiceException e) {
            if (logger.isWarnEnabled()) {
                logger.warn(e.getMessage());
            }
        } catch (Exception e) {
            logger.error("weChat notice error", e);
        }
    }
}
