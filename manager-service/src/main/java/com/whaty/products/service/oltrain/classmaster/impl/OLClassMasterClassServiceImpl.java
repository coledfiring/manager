package com.whaty.products.service.oltrain.classmaster.impl;


import com.whaty.constant.CommonConstant;
import com.whaty.constant.EnumConstConstants;
import com.whaty.core.commons.datasource.MasterSlaveRoutingDataSource;
import com.whaty.core.framework.bean.EnumConst;
import com.whaty.dao.GeneralDao;
import com.whaty.framework.asserts.TycjParameterAssert;
import com.whaty.products.service.oltrain.classmaster.OLClassMasterClassService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 班主任班级信息
 *
 * @author suoqiangqiang
 */
@Service("olClassMasterClassService")
public class OLClassMasterClassServiceImpl implements OLClassMasterClassService {

    @Resource(name = CommonConstant.GENERAL_DAO_BEAN_NAME)
    private GeneralDao generalDao;

    @Override
    public Map<String, Object> getClassInfo(String classId) {
        TycjParameterAssert.isAllNotBlank(classId);
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT                                                                    ");
        sql.append(" 	c.id AS id,                                                            ");
        sql.append(" 	c. NAME AS className,                                                  ");
        sql.append(" 	count(DISTINCT stu.id) AS studentNum,                                  ");
        sql.append(" 	count(DISTINCT cc.fk_course_id) AS onlineCourseNum,                    ");
        sql.append(" 	count(DISTINCT af.id) AS attachFileNum                                 ");
        sql.append(" from	                                                                   ");
        sql.append("   ol_pe_class c	                                                       ");
        sql.append(" left JOIN ol_class_course cc ON cc.fk_class_id = c.id                     ");
        sql.append(" left JOIN ol_class_student stu ON stu.fk_class_id = c.id                  ");
        sql.append(" LEFT JOIN attach_file af ON af.link_id = c.id                             ");
        sql.append(" AND af.namespace = 'olPeClass'                                            ");
        sql.append(" WHERE                                                                     ");
        sql.append(" 	c.id = ?                                                               ");
        return this.generalDao.getOneMapBySQL(sql.toString(), classId);
    }

    @Override
    public Map<String, Object> getClassDetail(String classId) {
        TycjParameterAssert.isAllNotBlank(classId);
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT                                                             ");
        sql.append(" 	c.name as className,                                            ");
        sql.append("   date_format(c.start_time, '%Y-%m-%d') as classDate,              ");
        sql.append("   date_format(c.end_time, '%Y-%m-%d') as endDate,                  ");
        sql.append(" 	c.period as period,                                             ");
        sql.append(" 	c.credit as credit,                                             ");
        sql.append(" 	date_Format(c.create_date, '%Y-%m-%d %H:%i:%s') as createDate,  ");
        sql.append(" 	cs.name as classStatus                                          ");
        sql.append(" FROM                                                               ");
        sql.append(" 	ol_pe_class c                                                   ");
        sql.append(" INNER JOIN enum_const cs on cs.id = c.flag_online_class_status     ");
        sql.append(" WHERE c.id = ?                                                     ");
        Map<String, Object> classDetail = this.generalDao.getOneMapBySQL(sql.toString(), classId);
        classDetail.putAll(Optional.ofNullable(this.generalDao
                .getOneMapBySQL("SELECT max( IF ( ty. CODE = '1', e.content, NULL )) AS introduce " +
                        "FROM ol_pe_class c " +
                        "LEFT JOIN ol_item_extend e ON e.fk_item_id = c.id " +
                        "LEFT JOIN enum_const ty ON ty.id = e.flag_online_item_extend_type " +
                        "WHERE c.id = ? GROUP BY c.id", classId)).orElse(new HashMap<>(2)));
        return classDetail;
    }

    @Override
    public List<Object[]> listOnlineCourses(String classId) {
        TycjParameterAssert.isAllNotBlank(classId);
        return this.generalDao.getBySQL("SELECT c.id AS id, c. NAME AS `name` FROM ol_pe_course c " +
                " WHERE c.site_code = ? " +
                " and c.flag_isvalid = ?" +
                " and NOT EXISTS ( SELECT 1 FROM ol_class_course cc WHERE cc.fk_class_id = ?" +
                " AND cc.fk_course_id = c.id )", MasterSlaveRoutingDataSource.getDbType(), this.generalDao
                .getEnumConstByNamespaceCode(EnumConstConstants.ENUM_CONST_NAMESPACE_FLAG_IS_VALID, "1")
                .getId(), classId);
    }

    @Override
    public void saveContent(String classId, String code, String content) {
        TycjParameterAssert.isAllNotBlank(classId, code);
        content = content == null ? "" : content;
        EnumConst type = this.generalDao
                .getEnumConstByNamespaceCode(EnumConstConstants.ENUM_CONST_NAMESPACE_FLAG_ONLINE_ITEM_EXTEND_TYPE, code);
        TycjParameterAssert.isAllNotNull(type);
        this.generalDao.executeBySQL("insert into ol_item_extend (id, content, flag_online_item_extend_type," +
                        " fk_item_id, namespace) values(replace(uuid(), '-', ''), ?, ?, ?, ?) " +
                        "on duplicate key update content = ?",
                content, type.getId(), classId, "peClass", content);
    }
}
