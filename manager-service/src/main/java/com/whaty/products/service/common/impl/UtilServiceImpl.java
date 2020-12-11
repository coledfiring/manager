package com.whaty.products.service.common.impl;

import com.whaty.constant.CommonConstant;
import com.whaty.constant.SiteConstant;
import com.whaty.core.commons.datasource.MasterSlaveRoutingDataSource;
import com.whaty.core.framework.hibernate.dao.impl.ControlGeneralDao;
import com.whaty.dao.GeneralDao;
import com.whaty.file.excel.upload.facade.ExcelUploadFacade;
import com.whaty.framework.config.util.SiteUtil;
import com.whaty.products.service.common.UtilService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

/**
 * 业务通用服务类
 * @author weipengsen
 */
@Service("utilService")
public class UtilServiceImpl implements UtilService {

    @Resource(name = CommonConstant.SPRING_BEAN_NAME_EXCEL_UPLOAD_FACADE)
    private ExcelUploadFacade excelUploadFacade;

    @Resource(name = CommonConstant.GENERAL_DAO_BEAN_NAME)
    private GeneralDao generalDao;

    @Resource(name = CommonConstant.CONTROL_GENERAL_DAO_BEAN_NAME)
    private ControlGeneralDao controlGeneralDao;

    @Override
    public Map<String, Object> doUploadFile(File file, Object[] headers, List<String[]> checkList,
                                            List<String> sqlList, int titleRowNum, String infoPrefix) {
        return this.excelUploadFacade.doUploadFile(file, headers, checkList, sqlList, titleRowNum, infoPrefix);
    }

    @Override
    public void generateExcelAndWrite(OutputStream out, Map<String, List<String>> selectMap, Object[] headers) {
        this.generateExcelAndWrite(out, selectMap, headers, null, null);
    }

    @Override
    public void generateExcelAndWrite(OutputStream out, Map<String, List<String>> selectMap, Object[] headers, String title) {
        this.generateExcelAndWrite(out, selectMap, headers, title, null);
    }

    @Override
    public void generateExcelAndWrite(OutputStream out, Map<String, List<String>> selectMap, Object[] headers, String title, List<Object[]> data) {
        this.excelUploadFacade.generateExcelAndWrite(out, selectMap, headers, title, data);
    }

    @Override
    public List<Map<String,Object>> getBaseCategoryIdsBySiteRole(String roleId, String siteCode) {
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT                                                                     ");
        sql.append("   bc.id baseId,                                                            ");
        sql.append("   if(bc.router_name is not null,bc.router_name,ppc.id) categoryId,         ");
        sql.append("   ppc.name categoryName                                                    ");
        sql.append(" FROM                                                                       ");
        sql.append("   pe_pri_category ppc                                                      ");
        sql.append(" inner JOIN pr_role_menu prm ON prm.fk_menu_id = ppc.id                     ");
        sql.append("   AND prm.fk_role_id = '" + roleId + "'                                    ");
        sql.append(" INNER JOIN pe_web_site s ON ppc.fk_web_site_id = s.id                      ");
        sql.append(" INNER JOIN pe_base_category bc ON ppc.fk_base_category_id = bc.id          ");
        sql.append(" WHERE                                                                      ");
        sql.append("   ppc.isActive = '1'                                                       ");
        sql.append(" AND s.CODE = '" + siteCode + "'                                            ");
        String dbCode = MasterSlaveRoutingDataSource.getDbType();
        MasterSlaveRoutingDataSource.setDbType(SiteConstant.SITE_CODE_CONTROL);
        List<Map<String, Object>> baseCategoryIds = (List<Map<String, Object>>) controlGeneralDao.getMapList(sql.toString(), null);
        MasterSlaveRoutingDataSource.setDbType(dbCode);
        return baseCategoryIds;
    }
}
