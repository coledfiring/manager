package com.whaty.products.service.hbgr.yysj;

import com.whaty.constant.CommonConstant;
import com.whaty.core.commons.exception.EntityException;
import com.whaty.dao.GeneralDao;
import com.whaty.domain.bean.hbgr.yysj.PeCheckWaterRecord;
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
@Service("peCheckWaterRecordService")
public class PeCheckWaterRecordServiceImpl extends TycjGridServiceAdapter<PeCheckWaterRecord> {

    @Resource(name = CommonConstant.GENERAL_DAO_BEAN_NAME)
    private GeneralDao myGeneralDao;

    @Override
    public void checkBeforeAdd(PeCheckWaterRecord bean) throws EntityException {
        bean.setCreateTime(new Date());
    }

    public Map<String, Object> getWaterCheckOptions() {
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("managers", this.myGeneralDao.getMapBySQL("SELECT manager.id as id, manager.`name` as name" +
                " FROM pe_manager manager INNER JOIN pe_pri_role role ON role.id = manager.fk_role_id" +
                " WHERE role.site_code = ? AND role.`CODE`= '229'", SiteUtil.getSiteCode()));
        resultMap.put("falgIsValids", this.myGeneralDao.getMapBySQL("SELECT id, name FROM enum_const WHERE namespace = 'FlagIsvalid'"));
        return resultMap;
    }

    public void addWeterCheckRecord(PeCheckWaterRecord peCheckWaterRecord) {
        peCheckWaterRecord.setCreateTime(new Date());
        this.myGeneralDao.save(peCheckWaterRecord);
    }

    public List<Map<String, Object>> getCheckWaterRecord(String id) {
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT                                                                    ");
        sql.append(" 	manager.NAME AS managerName,                                           ");
        sql.append(" 	point.NAME AS pointName,                                               ");
        sql.append(" 	e.NAME AS isValid1,                                                    ");
        sql.append(" 	e1.NAME AS isValid2,                                                   ");
        sql.append("   record.PH_1 as ph1,                                                     ");
        sql.append("   record.PH_2 as ph2,                                                     ");
        sql.append("   record.CI_1 as ci1,                                                     ");
        sql.append("   record.CI_2 as ci2,                                                     ");
        sql.append("   record.hard_1 as hard1,                                                 ");
        sql.append("   record.hard_2 as hard2,	                                               ");
        sql.append("   DATE_FORMAT(record.create_time, '%Y年%m月%d %h:%i') as createTime	       ");
        sql.append(" FROM                                                                      ");
        sql.append(" 	pe_check_water_record record                                           ");
        sql.append(" 	INNER JOIN pe_manager manager ON manager.id = record.fk_check_manager  ");
        sql.append(" 	INNER JOIN pe_check_point point ON point.id = record.fk_check_point    ");
        sql.append(" 	INNER JOIN enum_const e ON e.id = record.flag_isvalid_1                ");
        sql.append(" 	INNER JOIN enum_const e1 ON e1.id = record.flag_isvalid_2              ");
        sql.append(" WHERE point.id = ?                                                      ");
        sql.append(" ORDER BY record.create_time DESC                                         ");
        return this.myGeneralDao.getMapBySQL(sql.toString(), id);
    }
}
