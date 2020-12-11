package com.whaty.products.service.classmaster.impl;

import com.whaty.constant.CommonConstant;
import com.whaty.constant.EnumConstConstants;
import com.whaty.core.commons.datasource.MasterSlaveRoutingDataSource;
import com.whaty.core.framework.bean.EnumConst;
import com.whaty.dao.GeneralDao;
import com.whaty.framework.asserts.TycjParameterAssert;
import com.whaty.products.service.classmaster.ClassMasterClassService;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 班主任班级信息
 *
 * @author weipengsen
 */
@Lazy
@Service("classMasterClassService")
public class ClassMasterClassServiceImpl implements ClassMasterClassService {

    @Resource(name = CommonConstant.GENERAL_DAO_BEAN_NAME)
    private GeneralDao generalDao;

    @Override
    public Map<String, Object> getClassInfo(String classId) {
        TycjParameterAssert.isAllNotBlank(classId);
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT                                                                    ");
        sql.append(" 	c.id AS id,                                                            ");
        sql.append(" 	c. NAME AS className,                                                  ");
        sql.append(" 	count(DISTINCT cs.id) AS signNum,                                      ");
        sql.append(" 	count(DISTINCT hg.id) AS homeworkNum,                                  ");
        sql.append(" 	ifnull(qg.questionNum, 0) AS questionNum,                              ");
        sql.append(" 	ifnull(qg.voteNum, 0) AS voteNum,                                      ");
        sql.append(" 	count(DISTINCT stu.id) AS studentNum,                                  ");
        sql.append(" 	ifnull(cc.onlineCourseNum, 0) AS onlineCourseNum,                      ");
        sql.append(" 	ifnull(cc.faceCourseNum, 0) AS faceCourseNum,                          ");
        sql.append(" 	count(DISTINCT af.id) AS attachFileNum,                                ");
        sql.append(" 	concat(                                                                ");
        sql.append(" 		date_format(                                                       ");
        sql.append(" 			c.start_time,                                                  ");
        sql.append(" 			'%Y年%m月%d日'                                                 ");
        sql.append(" 		),                                                                 ");
        sql.append(" 		' - ',                                                             ");
        sql.append(" 		date_format(                                                       ");
        sql.append(" 			c.end_time,                                                    ");
        sql.append(" 			'%Y年%m月%d日'                                                 ");
        sql.append(" 		)                                                                  ");
        sql.append(" 	) AS classTime                                                         ");
        sql.append(" FROM                                                                      ");
        sql.append(" 	pe_class c                                                             ");
        sql.append(" LEFT JOIN class_sign cs ON cs.fk_class_id = c.id                          ");
        sql.append(" LEFT JOIN homework_group hg ON hg.item_id = c.id                          ");
        sql.append(" AND hg.is_delete = '0'                                                    ");
        sql.append(" LEFT JOIN (                                                               ");
        sql.append(" 	SELECT                                                                 ");
        sql.append(" 		sum(IF(ty. CODE = '1', 1, 0)) AS questionNum,                      ");
        sql.append(" 		sum(IF(ty. CODE = '2', 1, 0)) AS voteNum,                          ");
        sql.append(" 		qg.item_id AS item_id                                              ");
        sql.append(" 	FROM                                                                   ");
        sql.append(" 		questionnaire_group qg                                             ");
        sql.append(" 	INNER JOIN enum_const ty ON ty.id = qg.flag_questionnaire_group_type   ");
        sql.append(" 	WHERE                                                                  ");
        sql.append(" 		qg.item_id = ?                                                     ");
        sql.append(" 	AND qg.is_delete = '0'                                                 ");
        sql.append(" ) qg ON qg.item_id = c.id                                                 ");
        sql.append(" LEFT JOIN pe_student stu ON stu.fk_class_id = c.id                        ");
        sql.append(" LEFT JOIN (                                                               ");
        sql.append(" SELECT                                                                    ");
        sql.append(" 	cc.fk_class_id as fk_class_id,                                         ");
        sql.append(" 	sum(if(ty.code = '4', 1, 0)) as onlineCourseNum,                       ");
        sql.append(" 	sum(if(ty.code <> '4' AND cct.training_date is not null, 1, 0)) as faceCourseNum ");
        sql.append(" FROM                                                                      ");
        sql.append(" 	class_course cc                                                        ");
        sql.append(" INNER JOIN pe_class pc ON cc.fk_class_id = pc.id                          ");
        sql.append(" LEFT  JOIN class_course_timetable cct ON cct.fk_class_course_id = cc.id   ");
        sql.append(" INNER JOIN pe_course c ON c.id = cc.fk_course_id                          ");
        sql.append(" INNER JOIN enum_const ty ON ty.id = c.flag_course_type                    ");
        sql.append(" INNER JOIN enum_const tt ON c.flag_training_target = tt.id                ");
        sql.append(" INNER JOIN pe_teacher tea ON c.fk_teacher_id = tea.id                     ");
        sql.append(" WHERE                                                                     ");
        sql.append(" 	cc.fk_class_id = ?                                                     ");
        sql.append(" ) cc ON cc.fk_class_id = c.id                                             ");
        sql.append(" LEFT JOIN attach_file af ON af.link_id = c.id                             ");
        sql.append(" AND af.namespace = 'peClass'                                              ");
        sql.append(" WHERE                                                                     ");
        sql.append(" 	c.id = ?                                                               ");
        return this.generalDao.getOneMapBySQL(sql.toString(), classId, classId, classId);
    }

    @Override
    public Map<String, Object> getClassDetail(String classId) {
        TycjParameterAssert.isAllNotBlank(classId);
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT                                                             ");
        sql.append(" 	un.name as unitName,                                            ");
        sql.append(" 	tit.name as trainingItemType,                                   ");
        sql.append(" 	tit.code as trainingItemTypeCode,                               ");
        sql.append(" 	ti.name as trainingItem,                                        ");
        sql.append(" 	c.name as className,                                            ");
        sql.append(" 	cu.telephone as cooperateLinkPhone,                             ");
        sql.append(" 	cu.linkman as cooperateLinkman,                                 ");
        sql.append(" 	cu.name as cooperateUnit,                                       ");
        sql.append(" 	hc.name as hasCertificate,                                      ");
        sql.append(" 	cs.name as certificateStatus,                                   ");
        sql.append(" 	c.code as classCode,                                            ");
        sql.append(" 	tm.name as trainingMode,                                        ");
        sql.append("   date_format(c.start_time, '%Y-%m-%d') as classDate,              ");
        sql.append("   date_format(c.end_time, '%Y-%m-%d') as endDate,                  ");
        sql.append(" 	c.training_day_number as trainingDay,                           ");
        sql.append(" 	c.period as period,                                             ");
        sql.append(" 	c.credit as credit,                                             ");
        sql.append(" 	c.training_person_number as trainingPersonNumber,               ");
        sql.append(" 	c.dining_location as diningLocation,                            ");
        sql.append(" 	c.training_total_fee as trainingTotalFee,                       ");
        sql.append(" 	m.name as classMaster,                                          ");
        sql.append(" 	date_Format(c.create_date, '%Y-%m-%d %H:%i:%s') as createDate,  ");
        sql.append(" 	sz.name as schoolZone,                                          ");
        sql.append(" 	c.note as note                                                  ");
        sql.append(" FROM                                                               ");
        sql.append(" 	pe_class c                                                      ");
        sql.append(" INNER JOIN pe_unit un on un.id = c.fk_unit_id                      ");
        sql.append(" INNER JOIN training_item ti on ti.id = c.fk_training_item_id       ");
        sql.append(" INNER JOIN enum_const tit on tit.id = ti.flag_training_item_type   ");
        sql.append(" LEFT JOIN cooperate_unit cu on cu.id = ti.fk_cooperate_unit_id     ");
        sql.append(" INNER JOIN enum_const hc on hc.id = c.flag_has_certificate         ");
        sql.append(" INNER JOIN enum_const cs on cs.id = c.flag_apply_certificate_status");
        sql.append(" INNER JOIN enum_const tm on tm.id = c.flag_training_mode           ");
        sql.append(" INNER JOIN pe_manager m on m.id = c.fk_class_master_id             ");
        sql.append(" LEFT JOIN enum_const sz on sz.id = c.flag_school_zone             ");
        sql.append(" WHERE c.id = ?                                                     ");
        Map<String, Object> classDetail = this.generalDao.getOneMapBySQL(sql.toString(), classId);
        classDetail.putAll(Optional.ofNullable(this.generalDao
                .getOneMapBySQL("SELECT max( IF ( ty. CODE = '1', e.training_content, NULL )) AS admission, " +
                        "max( IF ( ty. CODE = '5', e.training_content, NULL )) AS guide, " +
                        "max( IF ( ty. CODE = '6', e.training_content, NULL )) AS `require`, " +
                        "max( IF ( ty. CODE = '8', e.training_content, NULL )) AS `introduce`, " +
                        "max( IF ( ty. CODE = '7', e.training_content, NULL )) AS handbook FROM pe_class c " +
                        "INNER JOIN training_item ti ON ti.id = c.fk_training_item_id " +
                        "LEFT JOIN training_item_extend e ON e.fk_item_id = ti.id OR e.fk_item_id = c.id " +
                        "LEFT JOIN enum_const ty ON ty.id = e.flag_item_extend_type " +
                        "WHERE c.id = ? GROUP BY c.id", classId)).orElse(new HashMap<>(2)));
        return classDetail;
    }

    @Override
    public List<Object[]> listOnlineCourses(String classId) {
        TycjParameterAssert.isAllNotBlank(classId);
        return this.generalDao.getBySQL("SELECT c.id AS id, c. NAME AS `name` FROM pe_course c " +
                "INNER JOIN enum_const ty ON ty.id = c.flag_course_type WHERE ty.code = '4' AND c.site_code = ? AND " +
                "NOT EXISTS ( SELECT 1 FROM class_course cc WHERE cc.fk_class_id = ? AND cc.fk_course_id = c.id )",
                MasterSlaveRoutingDataSource.getDbType(), classId);
    }

    @Override
    public void saveContent(String classId, String code, String content) {
        TycjParameterAssert.isAllNotBlank(classId, code);
        content = content == null ? "" : content;
        EnumConst type = this.generalDao
                .getEnumConstByNamespaceCode(EnumConstConstants.ENUM_CONST_NAMESPACE_FLAG_ITEM_EXTEND_TYPE, code);
        TycjParameterAssert.isAllNotNull(type);
        boolean isClass = !"1".equals(code);
        String itemId = null;
        if (!isClass) {
            itemId = this.generalDao.getOneBySQL("select fk_training_item_id from pe_class where id = ?", classId);
        }
        this.generalDao.executeBySQL("insert into training_item_extend (id, training_content, " +
                "flag_item_extend_type, fk_item_id, namespace) values(replace(uuid(), '-', ''), ?, ?, ?, ?) " +
                        "on duplicate key update training_content = ?",
                content, type.getId(), isClass ? classId : itemId, isClass ? "peClass" : "trainingItem", content);
    }
}
