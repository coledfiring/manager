package com.whaty.products.service.workspace;

import com.whaty.constant.CommonConstant;
import com.whaty.core.framework.user.service.UserService;
import com.whaty.core.framework.util.BeanNames;
import com.whaty.dao.GeneralDao;
import com.whaty.framework.config.util.SiteUtil;
import com.whaty.framework.scope.util.ScopeHandleUtils;
import net.sf.json.JSONArray;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * 工作台服务类
 *
 * @author weipengsen
 */
@Lazy
@Service("workBenchServiceImpl")
public class WorkBenchServiceImpl {

    @Resource(name = CommonConstant.GENERAL_DAO_BEAN_NAME)
    private GeneralDao myGeneralDao;

    @Resource(name = BeanNames.USER_SERVICE)
    private UserService userService;

    /**
     * 获取工作台信息
     *
     * @return
     */
    public Map<String, Object> getWorkBenchData() {
        StringBuilder sql = new StringBuilder();
        LinkedHashMap<String, Object> resultMap = new LinkedHashMap<>();
        //获取工作台数据信息
        sql.append("   select                                                 ");
        sql.append("     module_data_type  as module_data_type,               ");
        sql.append("     module_type as module_type,                          ");
        sql.append("     module_data_name as module_data_name,                ");
        sql.append("     pe_priority_action_id as pe_priority_action_id,      ");
        sql.append("     module_name as module_name,                          ");
        sql.append("     module_data_sql as module_data_sql,                  ");
        sql.append("     module_sql as module_sql,                            ");
        sql.append("     module_data_content as module_data_content,          ");
        sql.append("     more_sql as more_sql,                                ");
        sql.append("     url as url                                           ");
        sql.append("   from data_board                                        ");
        sql.append("   where                                                  ");
        sql.append("      site_code = ?                                       ");
        sql.append("   AND  module_type in ('04', '05')                       ");
        sql.append("   order by module_type,module_data_type                  ");
        List<Map<String, Object>> dataBoardList = this.myGeneralDao.getMapBySQL(sql.toString(), SiteUtil.getSiteCode());
        for (Map dataBoardMap : dataBoardList) {
            String moduleType = (String) dataBoardMap.get("module_type");
              if ("04".equals(moduleType)) {
                List<Map<String, Object>> bulletinList = myGeneralDao.getMapBySQL(ScopeHandleUtils.handleScopeSignOfSql(
                        (String)dataBoardMap.get("module_data_sql"), userService.getCurrentUser().getId()));
                resultMap.put(CommonConstant.JOB_ARRANGE, bulletinList);
            }
            if ("05".equals(moduleType)) {
                List<Map<String, Object>> bulletinList = myGeneralDao.getMapBySQL(ScopeHandleUtils.handleScopeSignOfSql(
                        (String)dataBoardMap.get("module_data_sql"), userService.getCurrentUser().getId()));
                resultMap.put(CommonConstant.POLICY_REGULATION, bulletinList);
            }
        }
        sql.delete(0, sql.length());
        String SchoolInfo = myGeneralDao
                .getOneBySQL("SELECT VALUE FROM system_variables WHERE NAME = ? AND site_code = ?", "workbench",
                SiteUtil.getSiteCode());
        JSONArray schoolInfoArray = JSONArray.fromObject(SchoolInfo);
        resultMap.put(CommonConstant.SCHOOL_INFO, schoolInfoArray);
        return resultMap;
    }
}
