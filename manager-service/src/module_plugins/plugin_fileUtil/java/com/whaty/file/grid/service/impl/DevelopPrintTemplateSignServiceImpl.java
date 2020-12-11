package com.whaty.file.grid.service.impl;

import com.whaty.constant.CommonConstant;
import com.whaty.core.commons.datasource.MasterSlaveRoutingDataSource;
import com.whaty.dao.GeneralDao;
import com.whaty.file.domain.bean.PePrintTemplateSign;
import com.whaty.framework.aop.notice.annotation.LogAndNotice;
import com.whaty.framework.exception.ParameterIllegalException;
import com.whaty.framework.exception.ServiceException;
import com.whaty.framework.grid.supergrid.service.impl.SuperAdminGridServiceImpl;
import com.whaty.util.CommonUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 超管打印模板占位符管理服务类
 * @author weipengsen
 */
@Lazy
@Service("developPrintTemplateSignService")
public class DevelopPrintTemplateSignServiceImpl extends SuperAdminGridServiceImpl<PePrintTemplateSign> {

    @Resource(name = CommonConstant.GENERAL_DAO_BEAN_NAME)
    private GeneralDao myGeneralDao;

    /**
     * 复制组到指定的模板
     * @param ids
     * @param printId
     */
    @LogAndNotice("自定义模板打印管理，复制组到指定的模板")
    public void doCopyGroupToTemplate(String ids, String printId) {
        // 校验
        if (StringUtils.isBlank(ids) || StringUtils.isBlank(printId)) {
            throw new ParameterIllegalException();
        }
        // 检查传递的组是否在指定的打印模板中有相同的名字
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT                                                          ");
        sql.append(" 	1                                                            ");
        sql.append(" FROM                                                            ");
        sql.append(" 	pe_print_template_group g                                    ");
        sql.append(" INNER JOIN pe_print_template_group g2 ON g. NAME = g2. NAME     ");
        sql.append(" WHERE                                                           ");
        sql.append(CommonUtils.madeSqlIn(ids, "g.id"));
        sql.append(" AND g2.fk_print_template_id = '" + printId + "'");
        List<Object> list = this.myGeneralDao.getBySQL(sql.toString());
        if (CollectionUtils.isNotEmpty(list)) {
            throw new ServiceException("指定的模板中有相同名字的组");
        }
        sql.delete(0, sql.length());
        sql.append(" INSERT INTO pe_print_template_group (             ");
        sql.append(" 	id,                                            ");
        sql.append(" 	name,                                          ");
        sql.append(" 	fk_print_template_id,                          ");
        sql.append(" 	serial_number                                  ");
        sql.append(" )                                                 ");
        sql.append(" SELECT                                            ");
        sql.append(" 	REPLACE (uuid(), '-', '') AS id,               ");
        sql.append(" 	NAME AS NAME,                                  ");
        sql.append(" 	'" + printId + "' AS fk_print_template_id,     ");
        sql.append(" 	serial_number AS serial_number                 ");
        sql.append(" FROM                                              ");
        sql.append(" 	pe_print_template_group                        ");
        sql.append(" WHERE                                             ");
        sql.append(CommonUtils.madeSqlIn(ids, "id"));
        this.myGeneralDao.executeBySQL(sql.toString());
        sql.delete(0, sql.length());
        sql.append(" INSERT INTO pe_print_template_sign (                                            ");
        sql.append(" 	id,                                                                          ");
        sql.append(" 	FK_PRINT_TEMPLATE_GROUP_ID,                                                  ");
        sql.append(" 	name,                                                                        ");
        sql.append(" 	sign,                                                                        ");
        sql.append(" 	SERIAL_NUMBER,                                                               ");
        sql.append(" 	FLAG_ACTIVE,                                                                 ");
        sql.append(" 	is_show                                                                      ");
        sql.append(" ) SELECT                                                                        ");
        sql.append(" 	replace(uuid(), '-', '') as id,                                              ");
        sql.append(" 	g2.id as fk_print_template_group_id,                                         ");
        sql.append(" 	si.name as name,                                                             ");
        sql.append(" 	si.sign as sign,                                                             ");
        sql.append(" 	si.serial_number as serial_number,                                           ");
        sql.append(" 	si.flag_active as flag_active,                                               ");
        sql.append(" 	si.is_show as is_show                                                        ");
        sql.append(" FROM                                                                            ");
        sql.append(" 	pe_print_template_sign si                                                    ");
        sql.append(" INNER JOIN pe_print_template_group g ON g.id = si.FK_PRINT_TEMPLATE_GROUP_ID    ");
        sql.append(" INNER JOIN pe_print_template_group g2 ON g2.`name` = g.`name`                   ");
        sql.append(" AND g2.fk_print_template_id = '" + printId + "'                                 ");
        sql.append(" WHERE                                                                           ");
        sql.append(CommonUtils.madeSqlIn(ids, "g.id"));
        this.myGeneralDao.executeBySQL(sql.toString());
    }

    /**
     * 获取所有的模板
     * @return
     */
    public List<Object[]> listTemplate() {
        List<Object[]> templates = this.myGeneralDao
                .getBySQL("select id, title from pe_print_template where code in ('word', 'pdf', 'excel') " +
                        "and site_code = ?", MasterSlaveRoutingDataSource.getDbType());
        return templates;
    }
}
