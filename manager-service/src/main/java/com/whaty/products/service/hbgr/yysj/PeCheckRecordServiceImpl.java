package com.whaty.products.service.hbgr.yysj;

import com.whaty.constant.CommonConstant;
import com.whaty.core.commons.exception.EntityException;
import com.whaty.dao.GeneralDao;
import com.whaty.domain.bean.hbgr.yysj.PeCheck;
import com.whaty.domain.bean.hbgr.yysj.PeCheckRecord;
import com.whaty.framework.config.util.SiteUtil;
import com.whaty.framework.grid.adapter.service.impl.TycjGridServiceAdapter;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * author weipengsen  Date 2020/7/13
 */
@Lazy
@Service("peCheckRecordService")
public class PeCheckRecordServiceImpl extends TycjGridServiceAdapter<PeCheckRecord> {

    @Resource(name = CommonConstant.GENERAL_DAO_BEAN_NAME)
    private GeneralDao myGeneralDao;

    @Override
    public void checkBeforeAdd(PeCheckRecord bean) throws EntityException {
        bean.setCreateTime(new Date());
    }

    public Map<String, Object> getCheckOptions() {
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("points", this.myGeneralDao.getMapBySQL("SELECT point.id as id," +
                " CONCAT(e.name, '-', point.name) as name, address FROM pe_check_point point" +
                " INNER JOIN enum_const e ON e.id = point.flag_scene"));
        resultMap.put("managers", this.myGeneralDao.getMapBySQL("SELECT manager.id as id, manager.`name` as name" +
                " FROM pe_manager manager INNER JOIN pe_pri_role role ON role.id = manager.fk_role_id" +
                " WHERE role.site_code = ? AND role.`CODE`= '229'", SiteUtil.getSiteCode()));
        resultMap.put("checkLevels", this.myGeneralDao.getMapBySQL("SELECT id, name FROM enum_const WHERE namespace = 'flagCheckLevel'"));
        resultMap.put("hygieneLevels", this.myGeneralDao.getMapBySQL("SELECT id, name FROM enum_const WHERE namespace = 'flagHygieneLevel'"));
        return resultMap;
    }

    public void addCheckRecord(PeCheckRecord peCheckRecord) {
        peCheckRecord.setCreateTime(new Date());
        this.myGeneralDao.save(peCheckRecord);
    }

    public List<Map<String, Object>> getCheckRecord(String id) {
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT                                                                    ");
        sql.append(" 	DATE_FORMAT(record.create_time, '%Y年%m月%d %h:%i') AS createTime,     ");
        sql.append(" 	manager.NAME AS managerName,                                           ");
        sql.append(" 	point.NAME AS pointName,                                               ");
        sql.append(" 	e.NAME AS checkLevel,                                                  ");
        sql.append(" 	e1.NAME AS hygieneLevel,                                               ");
        sql.append(" 	record.device_note as deviceNote,                                      ");
        sql.append(" 	record.problem_note as problemNote                                     ");
        sql.append(" FROM                                                                      ");
        sql.append(" 	pe_check_record record                                                 ");
        sql.append(" 	INNER JOIN pe_manager manager ON manager.id = record.fk_check_manager  ");
        sql.append(" 	INNER JOIN pe_check_point point ON point.id = record.fk_check_point    ");
        sql.append(" 	INNER JOIN enum_const e ON e.id = record.flag_check_level              ");
        sql.append(" 	INNER JOIN enum_const e1 ON e1.id = record.flag_hygiene_level          ");
        sql.append(" WHERE point.id = ?                                                      ");
        sql.append(" ORDER BY record.create_time DESC                                         ");
        return this.myGeneralDao.getMapBySQL(sql.toString(), id);
    }
}
