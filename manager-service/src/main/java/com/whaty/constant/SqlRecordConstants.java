package com.whaty.constant;

/**
 * sql记录的常量池
 *
 * @author weipengsen
 */
public interface SqlRecordConstants {

    /**
     * 单位基础信息
     */
    String UNIT_BASIC_SQL = "SELECT\n" +
            "\tun.id as id,\n" +
            "\tun.name as 名称,\n" +
            "\tut.name as 单位类型,\n" +
            "\tun.code as 编号,\n" +
            "\tun.head_name as 负责人姓名,\n" +
            "\tun.head_mobile as 负责人手机,\n" +
            "\tun.head_telephone as 负责人电话,\n" +
            "\tun.head_email as 负责人邮箱,\n" +
            "\tun.note as 备注\n" +
            "FROM\n" +
            "\tpe_unit un\n" +
            "INNER JOIN enum_const ut ON ut.id = un.flag_unit_type\n" +
            "WHERE\n" +
            "\tun.site_code = '${siteCode}' AND [ids|un.id]";

    /**
     * 班级基础信息
     */
    String CLASS_BASIC_SQL = "SELECT\n" +
            "\tthis.id AS id,\n" +
            "\tun. NAME AS 所属单位,\n" +
            "\ttit. NAME AS 培训种类,\n" +
            "\titem. NAME AS 所属项目,\n" +
            "\tthis. NAME AS 名称,\n" +
            "\tthis.entrust_unit_link_phone AS 委托单位联系电话,\n" +
            "\tthis.entrust_unit_linkman AS 委托单位联系人,\n" +
            "\thc. NAME AS 是否有证书,\n" +
            "\tacs. NAME AS 证书申请状态,\n" +
            "\tthis. CODE AS 开班编号,\n" +
            "\ttm. NAME AS 培训形式,\n" +
            "\tdate_format(this.start_time,'%Y-%m-%d') AS 开班日期,\n" +
            "\tdate_format(this.end_time,'%Y-%m-%d') AS 结束时间,\n" +
            "\tthis.training_day_number AS 培训天数,\n" +
            "\tthis.period AS 学时,\n" +
            "\tthis.credit AS 学分,\n" +
            "\tthis.training_person_number AS 培训人数,\n" +
            "\tthis.dining_location AS 用餐地点,\n" +
            "\tthis.training_total_fee AS 培训费总额,\n" +
            "\tcm. NAME AS 班主任,\n" +
            "\tthis.note AS 备注\n" +
            "FROM\n" +
            "\tpe_class this\n" +
            "LEFT OUTER JOIN pe_manager cm ON this.fk_class_master_id = cm.id\n" +
            "INNER JOIN enum_const acs ON this.flag_apply_certificate_status = acs.id\n" +
            "INNER JOIN enum_const hc ON this.flag_has_certificate = hc.id\n" +
            "INNER JOIN enum_const tm ON this.flag_training_mode = tm.id\n" +
            "INNER JOIN pe_unit un ON this.fk_unit_id = un.id AND [peUnit|un.id]\n" +
            "LEFT OUTER JOIN sso_user sc ON this.score_user = sc.id\n" +
            "INNER JOIN training_item item ON this.fk_training_item_id = item.id\n" +
            "INNER JOIN enum_const tit ON item.flag_training_item_type = tit.id\n" +
            "WHERE\n" +
            "[ids|this.id]" +
            "\tAND this.site_code = '${siteCode}'";

    /**
     * 合作单位基础信息
     */
    String COOPERATE_UNIT_BASIC_SQL = "SELECT\n" +
            "\tun.id AS id,\n" +
            "\tun. NAME AS 名称,\n" +
            "\tut. NAME AS 合作单位类型,\n" +
            "\tar. NAME AS 地区,\n" +
            "\tun.linkman AS 联系人,\n" +
            "\tun.telephone AS 联系电话,\n" +
            "\tun.linkman_position AS 联系人职位,\n" +
            "\tun.address AS 联系地址,\n" +
            "\tun.cooperate_date AS 合作日期,\n" +
            "\tu. NAME AS 合作单位,\n" +
            "\tun.division_proportion AS 分成比例,\n" +
            "\tun.tax_number AS 纳税人识别号\n" +
            "FROM\n" +
            "\tcooperate_unit un\n" +
            "INNER JOIN enum_const ut ON un.flag_cooperate_unit_type = ut.id\n" +
            "LEFT OUTER JOIN pe_area ar ON un.fk_area_id = ar.id\n" +
            "INNER JOIN pe_unit u ON un.fk_unit_id = u.id\n" +
            "WHERE\n" +
            "\tun.site_code = '${siteCode}'\n" +
            "AND [ids|u.id]";

    /**
     * 合作单位类型基础信息
     */
    String COOPERATE_UNIT_TYPE_BASIC_SQL = "SELECT\n" +
            "\tid AS id,\n" +
            "\tNAME AS 名称\n" +
            "FROM\n" +
            "\tenum_const\n" +
            "WHERE\n" +
            "\tnamespace = 'FlagCooperateUnitType'\n" +
            "AND team = ',${siteCode},'\n" +
            "AND [ids|id]";

    /**
     * 时间段基础信息
     */
    String COURSE_TIME_BASIC_SQL = "SELECT\n" +
            "\tid AS id,\n" +
            "\tNAME AS 名称,\n" +
            "\tstart_time AS 开始时间,\n" +
            "\tend_time AS 结束时间\n" +
            "FROM\n" +
            "\tcourse_time\n" +
            "WHERE\n" +
            "\tsite_code = '${siteCode}'\n" +
            "AND [ids|id]";

    /**
     * 课程类型基础信息
     */
    String COURSE_TYPE_BASIC_SQL = "SELECT\n" +
            "\tid AS id,\n" +
            "\tNAME AS 名称\n" +
            "FROM\n" +
            "\tenum_const\n" +
            "WHERE\n" +
            "\tnamespace = 'flagCourseType'\n" +
            "AND team = ',${siteCode},'\n" +
            "AND [ids|id]";

    /**
     * 部门类型基础信息
     */
    String DEPARTMENT_TYPE_BASIC_SQL = "SELECT\n" +
            "\tid AS id,\n" +
            "\tNAME AS 名称\n" +
            "FROM\n" +
            "\tenum_const\n" +
            "WHERE\n" +
            "\tnamespace = 'flagDepartmentType'\n" +
            "AND team = ',${siteCode},'\n" +
            "AND [ids|id]";

    /**
     * 学历基础信息
     */
    String EDUCATIONAL_BACKGROUND_BASIC_SQL = "SELECT\n" +
            "\tid AS id,\n" +
            "\tNAME AS 名称\n" +
            "FROM\n" +
            "\tenum_const\n" +
            "WHERE\n" +
            "\tnamespace = 'flagEducationalBackground'\n" +
            "AND team = ',${siteCode},'\n" +
            "AND [ids|id]";

    /**
     * 委托单位联系人基础信息
     */
    String ENTRUSTED_UNIT_LINKMAN_BASIC_SQL = "SELECT\n" +
            "\teul.id AS id,\n" +
            "\teul. NAME AS 联系人姓名,\n" +
            "\teul.job AS 联系人职务,\n" +
            "\teul.telephone AS 联系人电话,\n" +
            "\teul.mobile_number AS 联系人手机,\n" +
            "\teul.contacter AS 接洽人\n" +
            "FROM\n" +
            "\tentrusted_unit_linkman eul\n" +
            "INNER JOIN entrusted_unit eu ON eul.fk_unit_id = eu.id\n" +
            "WHERE\n" +
            " [ids|eul.id]" +
            "\tand eu.site_code='${siteCode}'";

    /**
     * 委托单位基础信息
     */
    String ENTRUSTED_UNIT_BASIC_SQL = "SELECT\n" +
            "\teu.id AS id,\n" +
            "\teu. NAME AS 单位名称,\n" +
            "\teut. NAME AS 委托单位类型,\n" +
            "\tar. NAME AS 地区,\n" +
            "\teu.address AS 联系地址,\n" +
            "\teu.tax_number AS 纳税人识别号\n" +
            "FROM\n" +
            "\tentrusted_unit eu\n" +
            "INNER JOIN enum_const eut ON eu.flag_entrusted_unit_type = eut.id\n" +
            "LEFT OUTER JOIN pe_area ar ON eu.fk_area_id = ar.id\n" +
            "WHERE\n" +
            "\teu.site_code = '${siteCode}'";

    /**
     * 委托单位类型基础信息
     */
    String ENTRUSTED_UNIT_TYPE_BASIC_SQL = "SELECT\n" +
            "\tid AS id,\n" +
            "\tNAME AS 名称\n" +
            "FROM\n" +
            "\tenum_const\n" +
            "WHERE\n" +
            "\tnamespace = 'flagEntrustedUnitType'\n" +
            "AND team = ',${siteCode},'\n" +
            "AND [ids|id]";

    /**
     * 信息来源基础信息
     */
    String INFO_SOURCE_BASIC_SQL = "SELECT\n" +
            "\tid AS id,\n" +
            "\tNAME AS 名称\n" +
            "FROM\n" +
            "\tenum_const\n" +
            "WHERE\n" +
            "\tnamespace = 'flagInfoSource'\n" +
            "AND team = ',${siteCode},'\n" +
            "AND [ids|id]";

    /**
     * 政策类型基础信息
     */
    String POLITY_TYPE_BASIC_SQL = "SELECT\n" +
            "\tid AS id,\n" +
            "\tNAME AS 名称\n" +
            "FROM\n" +
            "\tenum_const\n" +
            "WHERE\n" +
            "\tnamespace = 'flagFileType'\n" +
            "AND team = ',${siteCode},'\n" +
            "AND [ids|id]";

    /**
     * 附件类型基础信息
     */
    String ATTACH_FILE_TYPE_BASIC_SQL = "SELECT\n" +
            "\tid AS id,\n" +
            "\tNAME AS 名称\n" +
            "FROM\n" +
            "\tenum_const\n" +
            "WHERE\n" +
            "\tnamespace = 'flagAttachType'\n" +
            "AND [ids|id]";

    /**
     * 教师职称基础信息
     */
    String POSITIONAL_TITLE_BASIC_SQL = "SELECT\n" +
            "\tid AS id,\n" +
            "\tNAME AS 名称\n" +
            "FROM\n" +
            "\tenum_const\n" +
            "WHERE\n" +
            "\tnamespace = 'flagPositionalTitle'\n" +
            "AND team = ',${siteCode},'\n" +
            "AND [ids|id]";

    /**
     * 带班自检事务
     */
    String PREPARE_ITEM_BASIC_SQL = "SELECT\n" +
            "\tpi.id AS id,\n" +
            "\tpi. NAME AS 名称,\n" +
            "\tpt. NAME AS 准备类型,\n" +
            "\ttt. NAME AS 培训类型\n" +
            "FROM\n" +
            "\tprepare_item pi\n" +
            "INNER JOIN enum_const pt ON pi.flag_prepare_type = pt.id\n" +
            "INNER JOIN enum_const tt ON pi.flag_training_type = tt.id\n" +
            "WHERE\n" +
            "\tpi.site_code = '${siteCode}'\n" +
            "AND [ids|pi.id]";

    /**
     * 校区基础信息
     */
    String SCHOOL_ZONE_BASIC_SQL = "SELECT\n" +
            "\tid AS id,\n" +
            "\tNAME AS 名称\n" +
            "FROM\n" +
            "\tenum_const\n" +
            "WHERE\n" +
            "\tnamespace = 'flagSchoolZone'\n" +
            "AND team = ',${siteCode},'\n" +
            "AND [ids|id]";

    /**
     * 适合对象基础信息
     */
    String SUITABLE_TARGET_BASIC_SQL = "SELECT\n" +
            "\tid AS id,\n" +
            "\tNAME AS 名称\n" +
            "FROM\n" +
            "\tenum_const\n" +
            "WHERE\n" +
            "\tnamespace = 'flagSuitableTarget'\n" +
            "AND team = ',${siteCode},'\n" +
            "AND [ids|id]";

    /**
     * 适合地区基础信息
     */
    String TRAINING_AREA_BASIC_SQL = "SELECT\n" +
            "\tid AS id,\n" +
            "\tNAME AS 名称,\n" +
            "\tprovince AS 省/直辖市,\n" +
            "\tcity AS 城市,\n" +
            "\tcounty AS 县/区,\n" +
            "\tzip_code AS 邮政编码,\n" +
            "\tarea_code AS 电话区号\n" +
            "FROM\n" +
            "\tpe_area\n" +
            "WHERE\n" +
            "\tsite_code = '${siteCode}'\n" +
            "AND [ids|id]";

    /**
     * 培训证书基础信息
     */
    String TRAINING_CERTIFICATE_BASIC_SQL = "SELECT\n" +
            "\ttc.id AS id,\n" +
            "\ttc. NAME AS 名称,\n" +
            "\ttt. NAME AS 适合培训类型\n" +
            "FROM\n" +
            "\ttraining_certificate tc\n" +
            "INNER JOIN enum_const tt ON tc.flag_training_type = tt.id\n" +
            "WHERE\n" +
            "\ttc.site_code = '${siteCode}'";

    /**
     * 培训形式基础信息
     */
    String TRAINING_MODE_BASIC_SQL = "SELECT\n" +
            "\tid AS id,\n" +
            "\tNAME AS 名称\n" +
            "FROM\n" +
            "\tenum_const\n" +
            "WHERE\n" +
            "\tnamespace = 'flagTrainingMode'\n" +
            "AND team = ',${siteCode},'\n" +
            "AND [ids|id]";

    /**
     * 培训对象基础信息
     */
    String TRAINING_TARGET_BASIC_SQL = "SELECT\n" +
            "\tid AS id,\n" +
            "\tNAME AS 名称\n" +
            "FROM\n" +
            "\tenum_const\n" +
            "WHERE\n" +
            "\tnamespace = 'flagTrainingTarget'\n" +
            "AND team = ',${siteCode},'\n" +
            "AND [ids|id]";

    /**
     * 培训类型基础信息
     */
    String TRAINING_TYPE_BASIC_SQL = "SELECT\n" +
            "\tid AS id,\n" +
            "\tNAME AS 名称\n" +
            "FROM\n" +
            "\tenum_const\n" +
            "WHERE\n" +
            "\tnamespace = 'flagTrainingType'\n" +
            "AND team = ',${siteCode},'\n" +
            "AND [ids|id]";

    /**
     * 机构类型基础信息
     */
    String UNIT_TYPE_BASIC_SQL = "SELECT\n" +
            "\tid AS id,\n" +
            "\tNAME AS 名称\n" +
            "FROM\n" +
            "\tenum_const\n" +
            "WHERE\n" +
            "\tnamespace = 'flagUnitType'\n" +
            "AND team = ',${siteCode},'\n" +
            "AND [ids|id]";

    /**
     * 巡检点管理
     */
    String CHECK_POINT_BASIC_SQL = "SELECT\n" +
            "\tid AS id,\n" +
            "\tNAME AS 名称\n" +
            "FROM\n" +
            "\tenum_const\n" +
            "WHERE\n" +
            "\tnamespace = 'flagCheckPoint'\n";

    /**
     * 现场管理
     */
    String CHECK_SCENE_BASIC_SQL = "SELECT\n" +
            "\tid AS id,\n" +
            "\tNAME AS 名称\n" +
            "FROM\n" +
            "\tenum_const\n" +
            "WHERE\n" +
            "\tnamespace = 'flagScene'\n";

    /**
     * 班级宾馆基础信息
     */
    String CLASS_HOTEL_BASIC_SQL = "SELECT\n" +
            "\tchd.id AS id,\n" +
            "\tchd.note AS 备注,\n" +
            "\tchd.start_time AS 住宿开始时间,\n" +
            "\tchd.end_time AS 住宿结束时间,\n" +
            "\tchd.apply_time AS 申请时间,\n" +
            "\tau.login_id AS 申请人\n" +
            "FROM\n" +
            "\tclass_hotel_detail chd\n" +
            "INNER JOIN sso_user au ON au.id = chd.apply_user\n" +
            "WHERE\n" +
            "\t[ids|chd.id]\n";

    /**
     * 班级宾馆安排基础信息
     */
    String CLASS_HOTEL_ARRANGE_BASIC_SQL = "SELECT\n" +
            "\tarr.id as id,\n" +
            "\tho.`name` as 宾馆名称,\n" +
            "\tarr.male_price as 男单价,\n" +
            "\tarr.male_room_number as 男房间数,\n" +
            "\tarr.arrange_time as 安排时间,\n" +
            "\tau.login_id as 安排人\n" +
            "FROM\n" +
            "\tclass_hotel_arrange arr\n" +
            "INNER JOIN pe_hotel ho ON ho.id = arr.fk_hotel_id\n" +
            "INNER JOIN sso_user au ON au.id = arr.arrange_user\n" +
            "WHERE\n" +
            "\t[ids|arr.id]\n" +
            "\tAND [parentId|arr.fk_class_hotel_detail_id]\n";

    /**
     * 直播课程基础信息
     */
    String LIVE_COURSE_BASIC_SQL = "SELECT\n" +
            "\tlc.id AS id,\n" +
            "\tcla. NAME AS 班级名称,\n" +
            "\tcou. NAME AS 课程名称,\n" +
            "\tlc.exam_paper_code AS 试卷编号,\n" +
            "\tcon. NAME AS 是否有效,\n" +
            "\tlc.start_time AS 开始时间,\n" +
            "\tlc.end_time AS 结束时间,\n" +
            "\ttea. NAME AS 直播教师,\n" +
            "\tlc.live_url AS 直播地址\n" +
            "FROM\n" +
            "\tlive_course lc\n" +
            "INNER JOIN enum_const con ON lc.flag_active = con.id\n" +
            "INNER JOIN pe_class cla ON lc.fk_class_id = cla.id\n" +
            "LEFT OUTER JOIN pe_course cou ON lc.fk_course_id = cou.id\n" +
            "LEFT OUTER JOIN pe_teacher tea ON lc.fk_teacher_id = tea.id\n" +
            "WHERE\n" +
            "\t[ids|lc.id]\n" +
            "\tAND cla.site_code = '${siteCode}'";

    /**
     * 班级培训地点基础信息
     */
    String CLASS_TRAINING_PLACE_BASIC_SQL = "SELECT\n" +
            "\tc.id AS id,\n" +
            "\tun. NAME AS 申请单位,\n" +
            "\tc. CODE AS 班级编号,\n" +
            "\tc. NAME AS 班级名称,\n" +
            "\tpt. NAME AS 培训地点类型,\n" +
            "\tc.apply_time AS 申请时间,\n" +
            "\tau.login_id AS 申请人,\n" +
            "\tc.place_note AS 备注\n" +
            "FROM\n" +
            "\tpe_class c\n" +
            "INNER JOIN pe_unit un ON un.id = c.fk_unit_id\n" +
            "INNER JOIN enum_const pt ON pt.id = c.flag_place_type\n" +
            "INNER JOIN sso_user au ON au.id = c.apply_user\n" +
            "WHERE\n" +
            "\t[ids|c.id]\n" +
            "\tAND c.site_code = '${siteCode}'\n";

    /**
     * 班级课程基础信息
     */
    String CLASS_COURSE_BASIC_SQL = "SELECT\n" +
            "\tcc.id AS id,\n" +
            "\tcou. NAME AS 课程名称,\n" +
            "\tcou. CODE AS 课程编号,\n" +
            "\tcon2. NAME AS 培训对象,\n" +
            "\tcon. NAME AS 课程类型,\n" +
            "\ttea. NAME AS 讲师\n" +
            "FROM\n" +
            "\tclass_course cc\n" +
            "INNER JOIN pe_class cla ON cc.fk_class_id = cla.id\n" +
            "INNER JOIN pe_course cou ON cc.fk_course_id = cou.id\n" +
            "INNER JOIN enum_const con ON cou.flag_course_type = con.id\n" +
            "INNER JOIN enum_const con2 ON cou.flag_training_target = con2.id\n" +
            "INNER JOIN pe_teacher tea ON cou.fk_teacher_id = tea.id\n" +
            "WHERE\n" +
            "\t[ids|cc.id]\n" +
            "\tAND [parentId|cla.id]";

    /**
     * 安排班级课程
     */
    String ARRANGE_CLASS_COURSE_TIMETABL_BASIC_SQL = "SELECT\n" +
            "\tcct.id AS id,\n" +
            "\tcl. NAME AS '班级名称',\n" +
            "\tcou. NAME AS '课程名称',\n" +
            "\tcct.training_date as '培训日期',\n" +
            "\tcct.start_time AS '开始时间',\n" +
            "\tcct.end_time AS '结束时间',\n" +
            "\taa.name as '课件',\n" +
            "\tcct.note AS '备注',\n" +
            "\tcct.teacher_fee as '师资费用'\n" +
            "FROM\n" +
            "\tclass_course_timetable cct\n" +
            "INNER JOIN class_course cc ON cc.id = cct.fk_class_course_id\n" +
            "INNER JOIN pe_course cou ON cou.id = cc.fk_course_id\n" +
            "INNER JOIN pe_class cl ON cl.id = cc.fk_class_id\n" +
            "LEFT JOIN attach_file aa ON aa.id = cct.fk_courseware_id\n" +
            "WHERE \n" +
            "\t[parentId|cl.id]\n" +
            "\tAND [ids|cct.id]\n" +
            "GROUP BY\n" +
            "\tcct.id ";

    /**
     * 班级学员管理
     */
    String CLASS_STUDENT_BASIC_SQL = "SELECT\n" +
            "\tstu.id AS id,\n" +
            "\tcl. NAME AS '班级名称',\n" +
            "\tfg. NAME AS '性别',\n" +
            "\tstu.true_name AS '姓名',\n" +
            "\tstu.mobile AS '手机',\n" +
            "\tstu.work_unit AS '工作单位',\n" +
            "\tstu.certificate_number AS '证书编号',\n" +
            "\tff. NAME AS '民族',\n" +
            "\tstu.positional_title AS '职务',\n" +
            "\tstu.work_phone AS '办公电话',\n" +
            "\tstu.email AS '邮箱',\n" +
            "\tstu.create_date AS '操作时间',\n" +
            "\tstu.picture_url AS '查看图片'\n" +
            "FROM\n" +
            "\tpe_student stu\n" +
            "INNER JOIN sso_user su ON stu.create_by = su.id\n" +
            "LEFT OUTER JOIN enum_const ff ON stu.flag_folk = ff.id\n" +
            "INNER JOIN enum_const fg ON stu.flag_gender = fg.id\n" +
            "INNER JOIN pe_class cl ON stu.fk_class_id = cl.id\n" +
            "WHERE\n" +
            "\t[parentId|cl.id]\n" +
            "AND [ids|stu.id]\n" +
            "AND stu.site_code = '${siteCode}' ";

    /**
     * 培训项目基础信息
     */
    String TRAINING_ITEM_BASIC_SQL = "SELECT\n" +
            "\tthis.id AS id,\n" +
            "\tpeUnit. NAME AS '单位',\n" +
            "\tthis. CODE AS '项目编号',\n" +
            "\tthis. NAME AS '项目名称',\n" +
            "\tthis.linkman AS '联系人',\n" +
            "\tthis.link_phone AS '联系人电话',\n" +
            "\tenumConstByFlagItemStatus. NAME AS '项目状态',\n" +
            "\tcreateBy.TRUE_NAME AS '申报人',\n" +
            "\tthis.create_date AS '申报日期',\n" +
            "\tcheckUser.TRUE_NAME AS '审批人',\n" +
            "\tthis.check_date AS '审批时间',\n" +
            "\tenumConstByFlagTrainingType. NAME AS '培训类型',\n" +
            "\tenumConstByFlagTrainingTarget. NAME AS '培训对象',\n" +
            "\tenumConstByFlagIsLegal. NAME AS '是否法定',\n" +
            "\tthis.training_start_time AS '培训开始时间',\n" +
            "\tthis.training_end_time AS '培训结束时间',\n" +
            "\tthis.training_person_number AS '培训人数',\n" +
            "\tthis.teacher_fee AS '师资费',\n" +
            "\tthis.training_period AS '培训课时',\n" +
            "\tthis.room_fee AS '住宿费',\n" +
            "\tthis.food_fee AS '餐饮费',\n" +
            "\tthis.training_address AS '办班地点',\n" +
            "\tthis.transport_fee AS '交通费',\n" +
            "\tthis.teaching_fee AS '现场教学费',\n" +
            "\tthis.enroll_end_time AS '报名截止时间',\n" +
            "\tthis.site_fee AS '场地费',\n" +
            "\tthis.other_fee AS '其他费用',\n" +
            "\tthis.enroll_fee AS '报名费',\n" +
            "\tthis.total_fee AS '培训费',\n" +
            "\tthis.material_fee AS '资料费',\n" +
            "\tthis.period_fee AS '课时费（每课时）',\n" +
            "\tthis.put_on_record_time AS '备案日期',\n" +
            "\tthis.collect_fee_unit AS '收费单位',\n" +
            "\tcooperateUnit. NAME AS '合作单位名称',\n" +
            "\tenumConstByFlagCooperateUnitType. NAME AS '合作单位类型',\n" +
            "\tcooperateUnitArea. NAME AS '合作单位地区',\n" +
            "\tcooperateUnit.linkman AS '合作单位联系人',\n" +
            "\tcooperateUnit.telephone AS '合作单位联系电话',\n" +
            "\tcooperateUnit.linkman_position AS '合作单位联系职务',\n" +
            "\tcooperateUnit.division_proportion AS '合作单位分成比例',\n" +
            "\tentrustedUnit. NAME AS '委托单位名称',\n" +
            "\tenumConstByFlagEntrustedUnitType. NAME AS '委托单位类型',\n" +
            "\tentrustedUnitArea. NAME AS '委托单位地区',\n" +
            "\tenumConstByFlagInfoSource. NAME AS '信息来源',\n" +
            "\tconcat(\n" +
            "\t\tman.true_name,\n" +
            "\t\t'/',\n" +
            "\t\tman.login_id\n" +
            "\t) AS '项目负责人'\n" +
            "FROM\n" +
            "\ttraining_item this\n" +
            "INNER JOIN pe_unit peUnit ON peUnit.id = this.fk_unit_id\n" +
            "AND 1 = 1\n" +
            "INNER JOIN enum_const enumConstByFlagItemStatus ON enumConstByFlagItemStatus.id = this.flag_item_status\n" +
            "INNER JOIN enum_const enumConstByFlagTrainingType ON enumConstByFlagTrainingType.id = this.flag_training_type\n" +
            "INNER JOIN enum_const enumConstByFlagTrainingTarget ON enumConstByFlagTrainingTarget.id = this.flag_training_target\n" +
            "left JOIN enum_const enumConstByFlagIsLegal ON enumConstByFlagIsLegal.id = this.flag_is_legal\n" +
            "INNER JOIN enum_const enumConstByFlagTrainingItemType ON enumConstByFlagTrainingItemType.id = this.flag_training_item_type\n" +
            "LEFT JOIN sso_user createBy ON createBy.id = this.create_by\n" +
            "LEFT JOIN pe_manager man ON man.id = this.fk_manager_id\n" +
            "LEFT JOIN sso_user checkUser ON checkUser.id = this.fk_check_user_id\n" +
            "LEFT JOIN enum_const enumConstByFlagInfoSource ON enumConstByFlagInfoSource.id = this.flag_info_source\n" +
            "LEFT JOIN cooperate_unit cooperateUnit ON cooperateUnit.id = this.fk_cooperate_unit_id\n" +
            "LEFT JOIN enum_const enumConstByFlagCooperateUnitType ON enumConstByFlagCooperateUnitType.id = cooperateUnit.flag_cooperate_unit_type\n" +
            "LEFT JOIN pe_area cooperateUnitArea ON cooperateUnitArea.id = cooperateUnit.fk_area_id\n" +
            "LEFT JOIN entrusted_unit entrustedUnit ON entrustedUnit.id = this.fk_entrusted_unit_id\n" +
            "LEFT JOIN enum_const enumConstByFlagEntrustedUnitType ON enumConstByFlagEntrustedUnitType.id = entrustedUnit.flag_entrusted_unit_type\n" +
            "LEFT JOIN pe_area entrustedUnitArea ON entrustedUnitArea.id = entrustedUnit.fk_area_id\n" +
            "WHERE\n" +
            "\t[ids|this.id]\n" +
            "AND this.site_code = '${siteCode}'";

    /**
     * 管理基本信息
     */
    String MANAGER_BASIC_SQL = "SELECT\n" +
            "\tm.id AS id,\n" +
            "\tm.login_id AS '用户名',\n" +
            "\tm.true_name AS '姓名',\n" +
            "\tpr.NAME AS '用户角色',\n" +
            "\tun.NAME AS '所属单位',\n" +
            "\tac.NAME AS '是否有效',\n" +
            "\tdt.NAME AS '部门',\n" +
            "\tct.NAME AS '证件类型',\n" +
            "\tm.card_no AS '证件号码',\n" +
            "\tge.NAME AS '性别',\n" +
            "\tm.birthday AS '出生日期',\n" +
            "\tm.work_unit AS '工作单位',\n" +
            "\tm.mobile AS '手机',\n" +
            "\tm.telephone AS '工作电话',\n" +
            "\tm.home_phone AS '家庭电话',\n" +
            "\tm.email AS '电子邮箱',\n" +
            "\tm.address AS '联系地址',\n" +
            "\tm.zip_code AS '邮政编号',\n" +
            "\tm.qq AS 'QQ号',\n" +
            "IF (\n" +
            "\twu.openid IS NULL,\n" +
            "\t'否',\n" +
            "\t'是'\n" +
            ") AS '是否绑定微信'\n" +
            "FROM\n" +
            "\tpe_manager m\n" +
            "INNER JOIN sso_user ss ON ss.id = m.fk_sso_user_id\n" +
            "INNER JOIN pe_unit un ON un.id = m.fk_unit_id\n" +
            "INNER JOIN pe_pri_role pr ON pr.id = ss.FK_ROLE_ID\n" +
            "INNER JOIN enum_const ty ON ty.id = pr.FLAG_ROLE_TYPE\n" +
            "INNER JOIN enum_const ac ON ac.id = m.flag_active\n" +
            "LEFT JOIN enum_const dt ON dt.id = m.flag_department_type\n" +
            "LEFT JOIN enum_const ge ON ge.id = m.flag_gender\n" +
            "LEFT JOIN enum_const ct ON ct.id = m.flag_card_type\n" +
            "LEFT JOIN wechat_user wu ON wu.fk_sso_user_id = ss.id\n" +
            "WHERE\n" +
            "\t[ids|m.id]\n" +
            "AND m.site_code = '${siteCode}'";
    /**
     * 教师资源
     */
    String TEACHER_RESOURCE_BASIC_INFO = "SELECT\n" +
            "\ttea.id AS id,\n" +
            "\ttea.login_id AS '用户名',\n" +
            "\ttea.true_name AS '姓名',\n" +
            "\tpt. NAME AS '职称职务',\n" +
            "\teb. NAME AS '最高学历',\n" +
            "\tac. NAME AS '是否有效',\n" +
            "\tcat. NAME AS '证件类型',\n" +
            "\ttea.card_no AS '证件号码',\n" +
            "\tgen. NAME AS '性别',\n" +
            "\ttea.birthday AS '出生日期',\n" +
            "\ttea.work_unit AS '工作单位',\n" +
            "\ttea.mobile AS '手机',\n" +
            "\ttea.work_phone AS '工作电话',\n" +
            "\ttea.home_phone AS '家庭电话',\n" +
            "\ttea.email AS '电子邮箱',\n" +
            "\ttea.address AS '联系地址',\n" +
            "\ttea.zip_code AS '邮政编码',\n" +
            "\ttea.qq AS 'QQ号码',\n" +
            "IF (\n" +
            "\twu.openid IS NULL,\n" +
            "\t'否',\n" +
            "\t'是'\n" +
            ") AS '是否绑定微信',\n" +
            " tea.introduction AS '教师简介',\n" +
            " tea.picture_url AS '图片路径',\n" +
            " `is`. NAME AS '校内外'\n" +
            "FROM\n" +
            "\tpe_teacher tea\n" +
            "INNER JOIN enum_const pt ON pt.id = tea.flag_positional_title\n" +
            "LEFT JOIN enum_const eb ON eb.id = tea.flag_educational_background\n" +
            "INNER JOIN enum_const tt ON tt.id = tea.flag_tutor_teacher\n" +
            "INNER JOIN enum_const ct ON ct.id = tea.flag_course_teacher\n" +
            "INNER JOIN enum_const ic ON ic.id = tea.flag_isclassmaster\n" +
            "INNER JOIN enum_const ac ON ac.id = tea.flag_active\n" +
            "LEFT JOIN enum_const cat ON cat.id = tea.flag_card_type\n" +
            "LEFT JOIN enum_const gen ON gen.id = tea.flag_gender\n" +
            "LEFT JOIN wechat_user wu ON wu.fk_sso_user_id = tea.fk_sso_user_id\n" +
            "LEFT JOIN enum_const `is` ON `is`.id = tea.flag_in_school\n" +
            "WHERE\n" +
            "\t[ids|tea.id]\n" +
            "AND tea.site_code = '${siteCode}'";

    /**
     * 课程库基本信息
     */
    String COURSE_RESOURCE_BASIC_INFO = "SELECT\n" +
            "\tc.id AS id,\n" +
            "\tc.NAME AS '课程名称',\n" +
            "\tc.CODE AS '课程编码',\n" +
            "\ttt.NAME AS '适合对象',\n" +
            "\tct.NAME AS '课程类型',\n" +
            "\tc.course_intro AS '课程简介',\n" +
            "\ttea.NAME AS '讲师',\n" +
            "\ttea.work_unit AS '教师工作单位',\n" +
            "\ttea.mobile AS '教师手机号',\n" +
            "\ttea.login_id AS '教师用户名'\n" +
            "FROM\n" +
            "\tpe_course c\n" +
            "LEFT JOIN enum_const tt ON tt.id = c.flag_training_target\n" +
            "LEFT JOIN enum_const ct ON ct.id = c.flag_course_type\n" +
            "INNER JOIN pe_teacher tea ON tea.id = c.fk_teacher_id\n" +
            "WHERE\n" +
            "\t[ids|c.id]\n" +
            "AND c.site_code = '${siteCode}'";

    /**
     * 角色基本信息
     */
    String ROLE_BASIC_INFO = "SELECT\n" +
            "\trole.id as id,\n" +
            "\trole.name as '角色名称',\n" +
            "\troleType.name as  '角色类型'\n" +
            "FROM\n" +
            "\tpe_pri_role role\n" +
            "INNER JOIN enum_const roleType ON roleType.id = role.FLAG_ROLE_TYPE\n" +
            "LEFT JOIN enum_const em2 ON em2.id = role.flag_site_super_admin\n" +
            "WHERE\n" +
            "\t[ids|role.id]\n" +
            "AND role.site_code = '${siteCode}'";

    /**
     * 接待基本信息
     */
    String RECEIVED_BASIC_INFO = "SELECT\n" +
            "\tpr.id AS id,\n" +
            "\treceived_time AS '接待时间',\n" +
            "\treceived_by AS '接待事由',\n" +
            "\tregist_time AS '登记时间',\n" +
            "\tpeople_number AS '人数',\n" +
            "\treceived_require AS '接待要求',\n" +
            "\tparticipate_user AS '参加人员',\n" +
            "\tgroup_concat(pm. NAME) AS '接待人员',\n" +
            "\tnote AS '备注',\n" +
            "\tregist_user AS '接待人员'\n" +
            "FROM\n" +
            "\tpe_received pr\n" +
            "LEFT JOIN pri_received_manager prm ON prm.fk_received_id = pr.id\n" +
            "LEFT JOIN pe_manager pm ON pm.id = prm.fk_pe_manager_id\n" +
            "WHERE\n" +
            "\t[ids|pr.id]\n" +
            "AND pr.site_code = '${siteCode}'";

    /**
     * 接待修改
     */
    String RECEIVED_UPDATE = "SELECT\n" +
            "\tpr.id AS id,\n" +
            "\treceived_time AS '接待时间',\n" +
            "\treceived_by AS '接待事由',\n" +
            "\tregist_time AS '登记时间',\n" +
            "\tpeople_number AS '人数',\n" +
            "\treceived_require AS '接待要求',\n" +
            "\tparticipate_user AS '参加人员',\n" +
            "\tgroup_concat(pm. NAME) AS '接待人员',\n" +
            "\tnote AS '备注',\n" +
            "\tregist_user AS '接待人员'\n" +
            "FROM\n" +
            "\tpe_received pr\n" +
            "LEFT JOIN pri_received_manager prm ON prm.fk_received_id = pr.id\n" +
            "LEFT JOIN pe_manager pm ON pm.id = prm.fk_pe_manager_id\n" +
            "WHERE\n" +
            "\t[id|pr.id]\n" +
            "AND pr.site_code = '${siteCode}'";

    /**
     * 用印基本信息
     */
    String USE_SEAL_BASIC_INFO = "SELECT\n" +
            "\tus.id AS id,\n" +
            "\tpm.`name` AS '申请人',\n" +
            "\tpm.mobile AS '申请人手机号',\n" +
            "\tpu.`name` AS '申请单位',\n" +
            "\tus.applicant_use_time AS '申请用印时间',\n" +
            "\tpm1. NAME AS '受理人',\n" +
            "\tpm1.mobile AS '受理人手机号',\n" +
            "\tus.accept_time AS '受理时间',\n" +
            "\tec1.`NAME` AS '用印类型',\n" +
            "\tus.applicant_time AS '申请时间',\n" +
            "\tec2. NAME AS '状态',\n" +
            "\tus.note AS '用印事由'\n" +
            "FROM\n" +
            "\tuse_seal us\n" +
            "INNER JOIN sso_user su1 ON su1.ID = us.fk_applicant_user_id\n" +
            "INNER JOIN pe_manager pm ON pm.fk_sso_user_id = su1.ID\n" +
            "INNER JOIN pe_unit pu ON pm.fk_unit_id = pu.id\n" +
            "INNER JOIN enum_const ec1 ON ec1.id = us.flag_use_seal_type\n" +
            "LEFT JOIN pe_manager pm1 ON pm1.fk_sso_user_id = us.fk_accept_user_id\n" +
            "INNER JOIN enum_const ec2 ON ec2.id = us.flag_accept_status\n" +
            "WHERE\n" +
            "\t[ids|us.id]\n" +
            "AND us.site_code = '${siteCode}'";

    /**
     * 印刷管理基本信息
     */
    String PRINTING_BASIC_INFO = "SELECT\n" +
            "\tpp.id AS id,\n" +
            "\tpu. NAME AS '建议印刷单位',\n" +
            "\tpp.printing_content AS '印刷内容',\n" +
            "\tpp.paper AS '纸张要求',\n" +
            "\tpp.printing_name AS '印刷资料名称',\n" +
            "\tpp.printing_number AS '印刷数量',\n" +
            "\tpp.printing_require AS '要求',\n" +
            "\tpm1.`name` AS '申请人',\n" +
            "\tpm1.mobile AS '申请人手机号',\n" +
            "\tpp.price AS '费用',\n" +
            "\tpm2. NAME AS '受理人',\n" +
            "\tpm2.mobile AS '受理人手机号',\n" +
            "\tpp.accept_time AS '受理时间',\n" +
            "\tpm3.`name` AS '验收人',\n" +
            "\tpp.check_time AS '验收时间',\n" +
            "\tpu1. NAME AS '单位',\n" +
            "\tpp.note AS '备注',\n" +
            "\tpp.applicant_time AS '申请时间'\n" +
            "FROM\n" +
            "\tpe_printing pp\n" +
            "LEFT JOIN printing_unit pu ON pu.id = pp.fk_printing_unit_id\n" +
            "LEFT JOIN sso_user su1 ON su1.ID = pp.fk_applicant_user_id\n" +
            "LEFT JOIN sso_user su2 ON su2.ID = pp.fk_accept_user_id\n" +
            "LEFT JOIN sso_user su3 ON su3.ID = pp.fk_check_user_id\n" +
            "LEFT JOIN pe_manager pm1 ON su1.id = pm1.fk_sso_user_id\n" +
            "LEFT JOIN pe_manager pm2 ON su2.id = pm2.fk_sso_user_id\n" +
            "LEFT JOIN pe_manager pm3 ON su3.id = pm3.fk_sso_user_id\n" +
            "INNER JOIN pe_unit pu1 ON pu1.id = pp.fk_unit_id\n" +
            "WHERE\n" +
            "\t[ids|pp.id]\n" +
            "AND pp.site_code = '${siteCode}'";

    /**
     * 印刷单位基本信息
     */
    String PRINTING_UNIT_BASIC_INFO = "SELECT\n" +
            "\tpu.id AS id,\n" +
            "\tpu.NAME AS '印刷单位',\n" +
            "\tpu.agreement_price AS '合同价格',\n" +
            "\tpu.agreement_time AS '合同时间',\n" +
            "\tec.`NAME` AS '是否有效'\n" +
            "FROM\n" +
            "\tprinting_unit pu\n" +
            "INNER JOIN enum_const ec ON ec.id = pu.flag_is_valid\n" +
            "WHERE\n" +
            "\t[ids|pu.id]\n" +
            "AND pu.site_code = '${siteCode}'";

    /**
     * 宾馆基本信息
     */
    String HOTEL_BASIC_INFO = "SELECT\n" +
            "\tthis.id AS id,\n" +
            "\tthis. NAME AS '名称',\n" +
            "\tthis.address AS '地址',\n" +
            "\tthis.linkman AS '联系人',\n" +
            "\tthis.link_phone AS '联系电话',\n" +
            "\tthis.agreement_price AS '协议价格',\n" +
            "\tthis.note AS '备注'\n" +
            "FROM\n" +
            "\tpe_hotel this\n" +
            "WHERE" +
            "\t[ids|this.id]\n" +
            "AND this.site_code = '${siteCode}'";

    /**
     * 培训地点基本信息
     */
    String PLACE_BASIC_INFO = "SELECT\n" +
            "\tplace.id AS id,\n" +
            "\tplace. NAME AS '场地名称',\n" +
            "\tunit.`name` AS '所属单位',\n" +
            "\tplace.capacity AS '容量',\n" +
            "\tplace.charges AS '收费标准',\n" +
            "\tfsz. NAME AS '校区',\n" +
            "\tec.`NAME` AS '是否有效'\n" +
            "FROM\n" +
            "\tpe_place place\n" +
            "INNER JOIN pe_unit unit ON unit.id = place.fk_place_unit\n" +
            "INNER JOIN enum_const fsz ON fsz.id = place.flag_school_zone\n" +
            "INNER JOIN enum_const ec ON ec.id = place.flag_is_valid\n" +
            "WHERE" +
            "\t[ids|place.id]\n" +
            "AND place.site_code = '${siteCode}'";

    /**
     * 需求基本信息
     */
    String REQUIREMENT_INFO_BASIC_INFO = "SELECT\n" +
            "\tre.id AS id,\n" +
            "\tre.serial AS '编号',\n" +
            "\tre.customer AS '客户名称',\n" +
            "\tarea. NAME AS '地区',\n" +
            "\tre.linkman AS '联系人',\n" +
            "\tre.link_phone AS '联系电话',\n" +
            "\ttt.NAME AS '培训对象',\n" +
            "\tre.requirement_info AS '需求说明',\n" +
            "\tsource. NAME AS '信息来源',\n" +
            "\tcre.NAME AS '录入人员',\n" +
            "\tre.create_time AS '录入时间',\n" +
            "\tman.NAME AS '跟进人',\n" +
            "\tre.accept_time AS '接受时间',\n" +
            "\trt.NAME AS '需求状态',\n" +
            "\tft.NAME AS '跟进状态'\n" +
            "FROM\n" +
            "\trequirement_info re\n" +
            "INNER JOIN pe_area area ON area.id = re.fk_area_id\n" +
            "INNER JOIN enum_const tt ON tt.id = re.flag_training_target\n" +
            "INNER JOIN enum_const source ON source.id = re.flag_info_source\n" +
            "INNER JOIN pe_manager cre ON re.fk_create_user_id = cre.id\n" +
            "INNER JOIN sso_user sso ON sso.id = cre.fk_sso_user_id\n" +
            "LEFT JOIN pe_manager man ON re.fk_follow_up_user_id = man.id\n" +
            "LEFT JOIN enum_const rt ON rt.id = re.flag_requirement_status\n" +
            "LEFT JOIN enum_const ft ON ft.id = re.flag_follow_up_status\n" +
            "WHERE\n" +
            "\t[ids|re.id]\n" +
            "AND re.site_code = '${siteCode}'";

    /**
     * 需求跟进信息
     */
    String REQUIREMENT_FOLLOW_UP_INFO = "SELECT\n" +
            "  re.id AS id,\n" +
            "  re.serial AS '编号',\n" +
            "  re.customer AS '客户名称',\n" +
            "  area. NAME AS '地区',\n" +
            "  re.linkman AS '联系人',\n" +
            "  re.link_phone AS '联系电话',\n" +
            "  tt.NAME AS '培训对象',\n" +
            "  re.requirement_info AS '需求说明',\n" +
            "  source. NAME AS '信息来源',\n" +
            "  cre.NAME AS '录入人员',\n" +
            "  re.create_time AS '录入时间',\n" +
            "  man.NAME AS '跟进人',\n" +
            "  re.accept_time AS '接受时间',\n" +
            "  rt.NAME AS '需求状态',\n" +
            "  ft.NAME AS '跟进状态',\n" +
            "  re.daily_training_fee as '日培训费',\n" +
            "  re.daily_room_fee as '日住宿费',\n" +
            "  re.daily_transport_fee as '日交通费',\n" +
            "  re.daily_food_fee as '日餐费',\n" +
            "  re.daily_tea_break_fee as '日茶歇费',\n" +
            "  re.other_fee as '其他费用',\n" +
            "  re.note as '备注'\n" +
            "FROM\n" +
            "requirement_info re \n" +
            "inner join pe_area area on area.id=re.fk_area_id\n" +
            "INNER JOIN enum_const tt on tt.id=re.flag_training_target\n" +
            "INNER JOIN enum_const source on source.id=re.flag_info_source\n" +
            "INNER JOIN pe_manager cre on re.fk_create_user_id=cre.id\n" +
            "INNER JOIN pe_manager man on re.fk_follow_up_user_id=man.id\n" +
            "INNER JOIN sso_user sso on sso.id=man.fk_sso_user_id\n" +
            "INNER JOIN enum_const rt on rt.id=re.flag_requirement_status\n" +
            "LEFT JOIN enum_const ft on ft.id=re.flag_follow_up_status\n" +
            "where " +
            "\t[ids|re.id]\n" +
            "AND re.site_code = '${siteCode}'";

    /**
     * 项目评审管理信息
     */
    String REVIEW_ITEM_MANAGE_INFO = "SELECT\n" +
            "\tpr.id AS id,\n" +
            "\tpr.`name` AS '项目名称',\n" +
            "\tpr.background AS '项目背景',\n" +
            "\tgroup_concat(DISTINCT pm. NAME) AS '评审专家',\n" +
            "\tgroup_concat(DISTINCT pm1. NAME) AS '项目负责人',\n" +
            "\tpr.note AS '注意事项',\n" +
            "\tec.`NAME` AS '评审状态',\n" +
            "\tpr.opinion AS '评审意见',\n" +
            "\tsu.true_name AS '创建人',\n" +
            "\tpr.create_time AS '创建时间',\n" +
            "\tpr.review_requirement AS '评审要求'\n" +
            "FROM\n" +
            "\tpe_review pr\n" +
            "INNER JOIN pri_review_manager prm ON pr.id = prm.fk_review_id\n" +
            "AND prm.role = '2'\n" +
            "INNER JOIN pe_manager pm ON pm.id = prm.fk_manager_id\n" +
            "INNER JOIN pri_review_manager prm1 ON pr.id = prm1.fk_review_id\n" +
            "AND prm1.role = '1'\n" +
            "INNER JOIN pe_manager pm1 ON pm1.id = prm1.fk_manager_id\n" +
            "INNER JOIN enum_const ec ON ec.id = pr.flag_project_status\n" +
            "INNER JOIN sso_user su ON pr.create_user = su.ID\n" +
            "INNER JOIN pri_review_manager prm2 ON pr.id = prm2.fk_review_id\n" +
            "INNER JOIN pe_manager pm2 ON pm2.id = prm2.fk_manager_id\n" +
            "INNER JOIN sso_user su2 ON su2.ID = pm2.fk_sso_user_id\n" +
            "WHERE\n" +
            "\t[ids|pr.id]\n" +
            "and (su2.id = '${currentUserId}'\n" +
            "OR pr.create_user = '${currentUserId}')";

    /**
     * 添加或修改项目评审
     */
    String REVIEW_ADD_OR_UPDATE = "SELECT\n" +
            "\tpr.id AS id,\n" +
            "\tpr.`name` AS '项目名称',\n" +
            "\tpr.background AS '项目背景',\n" +
            "\tgroup_concat(DISTINCT pm. NAME) AS '评审专家',\n" +
            "\tgroup_concat(DISTINCT pm1. NAME) AS '项目负责人',\n" +
            "\tpr.note AS '注意事项',\n" +
            "\tec.`NAME` AS '评审状态',\n" +
            "\tpr.opinion AS '评审意见',\n" +
            "\tsu.true_name AS '创建人',\n" +
            "\tpr.create_time AS '创建时间',\n" +
            "\tpr.review_requirement AS '评审要求'\n" +
            "FROM\n" +
            "\tpe_review pr\n" +
            "INNER JOIN pri_review_manager prm ON pr.id = prm.fk_review_id\n" +
            "AND prm.role = '2'\n" +
            "INNER JOIN pe_manager pm ON pm.id = prm.fk_manager_id\n" +
            "INNER JOIN pri_review_manager prm1 ON pr.id = prm1.fk_review_id\n" +
            "AND prm1.role = '1'\n" +
            "INNER JOIN pe_manager pm1 ON pm1.id = prm1.fk_manager_id\n" +
            "INNER JOIN enum_const ec ON ec.id = pr.flag_project_status\n" +
            "INNER JOIN sso_user su ON pr.create_user = su.ID\n" +
            "INNER JOIN pri_review_manager prm2 ON pr.id = prm2.fk_review_id\n" +
            "INNER JOIN pe_manager pm2 ON pm2.id = prm2.fk_manager_id\n" +
            "INNER JOIN sso_user su2 ON su2.ID = pm2.fk_sso_user_id\n" +
            "WHERE\n" +
            "\t[id|pr.id]\n" +
            "and (su2.id = '${currentUserId}'\n" +
            "OR pr.create_user = '${currentUserId}')";

    /**
     * 项目分派信息
     */
    String TRAINING_ITEM_APPORTION_INFO = "SELECT\n" +
            "  this.id as id,\n" +
            "  ifnull(apt.name,'未分派') as 是否分派,\n" +
            "  this.apportion_date as 分派时间,\n" +
            "  apportionUser.LOGIN_ID as 分派人\n" +
            "FROM\n" +
            "\ttraining_item this \n" +
            "INNER JOIN pe_unit peUnit on peUnit.id = this.fk_unit_id AND [peUnit|un.id]\n" +
            "LEFT JOIN enum_const apt on apt.id = this.flag_is_apportion\n" +
            "LEFT JOIN sso_user apportionUser on apportionUser.id = this.fk_apportion_user_id\n" +
            "WHERE\n" +
            "[ids|this.id] \n" +
            "and this.site_code = '${siteCode}'";

    /**
     * 项目安排信息
     */
    String TRAINING_ITEM_ARRANGE_INFO = "SELECT\n" +
            "\tthis.id AS id,\n" +
            "\tthis.arrange_date AS '安排日期',\n" +
            "\tarrangeUnit. NAME AS '安排单位',\n" +
            "\tarrangeUser.true_name AS '安排人'\n" +
            "FROM\n" +
            "\ttraining_item this\n" +
            "LEFT JOIN sso_user apportionUser ON apportionUser.id = this.fk_apportion_user_id\n" +
            "LEFT JOIN sso_user arrangeUser ON arrangeUser.id = this.fk_arrange_user_id\n" +
            "LEFT JOIN pe_unit arrangeUnit ON arrangeUnit.id = this.fk_arrange_unit_id\n" +
            "WHERE\n" +
            "\t[ids|this.id]\n" +
            "AND this.site_code = '${siteCode}'";

    /**
     * 报名信息
     */
    String ENROLL_INFO_MANAGE_INFO = "SELECT\n" +
            "\tstu.id AS id,\n" +
            "\tstu.enroll_fee AS '报名费',\n" +
            "\tcl.CODE AS '班级编码',\n" +
            "\tcl.NAME AS '班级名称',\n" +
            "\tenumConstByFlagFeeStatus.NAME AS '交费状态',\n" +
            "\tenumConstByFlagPayWay.NAME AS '交费方式',\n" +
            "\tstu.fee_operate_time AS '交费日期',\n" +
            "\tst.NAME AS '学生状态',\n" +
            "\tstu.quit_school_date AS '退学日期',\n" +
            "\tstu.invoice_no AS '票据号',\n" +
            "\tstu.invoice_print_time AS '票据打印时间'\n" +
            "FROM\n" +
            "\tpe_student stu\n" +
            "LEFT JOIN training_item item ON item.id = stu.fk_training_item_id\n" +
            "LEFT JOIN pe_unit unit ON unit.id = item.fk_unit_id\n" +
            "LEFT JOIN enum_const enumConstByFlagFeeStatus ON enumConstByFlagFeeStatus.id = stu.flag_fee_status\n" +
            "LEFT JOIN enum_const st ON st.id = stu.flag_student_status\n" +
            "LEFT JOIN enum_const enumConstByFlagPayWay ON enumConstByFlagPayWay.id = stu.flag_pay_way\n" +
            "LEFT JOIN pe_class cl ON cl.id = stu.fk_class_id\n" +
            "WHERE\n" +
            "\t[ids|stu.id]\n" +
            "AND stu.site_code = '${siteCode}'";

    /**
     * 班级费用结算信息
     */
    String CLAZZ_SETTLE_ACCOUNT_INFO = "SELECT\n" +
            "\tcl.id as id,\n" +
            "\ttraining_total_fee as '培训费总额',\n" +
            "\tteacher_fee as '师资费',\n" +
            "\tteaching_fee as '现场教学费',\n" +
            "\ttransport_fee as '交通费',\n" +
            "\tfood_fee as '餐饮费',\n" +
            "\troom_fee as '住宿费',\n" +
            "\tenroll_fee as '报名费',\n" +
            "\tmaterial_fee as '资料费',\n" +
            "\tperiod_fee as '劳务带班费',\n" +
            "\tsite_fee as '场地费',\n" +
            "\tother_fee as '其他费用',\n" +
            "\tsas.name as '申请状态'\n" +
            "FROM\n" +
            "\tpe_class cl\n" +
            "INNER JOIN enum_const sas on sas.id=cl.flag_settle_account_status\n" +
            "WHERE\n" +
            "\t[ids|cl.id]\n" +
            "AND site_code = '${siteCode}'";

    /**
     * 结业证书编号信息
     */
    String COMPLETION_CERTIFICATE_INFO = "SELECT id as id, certificate_number as '毕业证编号' FROM pe_student " +
            "WHERE [ids|id] AND site_code = '${siteCode}'";

    /**
     * 需求跟进列表管理
     */
    String REQUIREMENT_FOLLOW_UP_LIST = REQUIREMENT_FOLLOW_UP_INFO + " AND sso.id = '${currentUserId}'\n";

    /**
     * 附件管理
     */
    String ATTACH_FILE = "SELECT\n" +
            "\tf.id as id,\n" +
            "\tf.name as 文件名,\n" +
            "\tty.name as 类型,\n" +
            "\tss.TRUE_NAME as 上传人,\n" +
            "\tf.create_time as 上传时间\n" +
            "FROM\n" +
            "\tattach_file f\n" +
            "INNER JOIN enum_const ty ON ty.id = f.flag_attach_type\n" +
            "LEFT JOIN sso_user ss ON ss.id = f.create_by\n" +
            "WHERE\n" +
            "\t[ids|f.id]\n" +
            "\tAND f.site_code = '${siteCode}'";

    /**
     * 培训管理员任务
     */
    String TRAINING_MANAGER_TASK = "SELECT\n" +
            "  tmt.id as id,\n" +
            "  tmt.name as '任务名称',\n" +
            "  tt.name as '任务类型',\n" +
            "  date_format(tmt.start_time,'%Y-%m-%d %H:%i:%s') as '开始时间',\n" +
            "  date_format(tmt.end_time,'%Y-%m-%d %H:%i:%s') as '结束时间',\n" +
            "  GROUP_CONCAT(DISTINCT chr.name) as '负责人',\n" +
            "  GROUP_CONCAT(DISTINCT cop.name) as '抄送人',\n" +
            "  pt.name as '优先级',\n" +
            "  nw.name as '通知方式',\n" +
            "  tmt.note as '任务描述',\n" +
            "  ttt.name as '任务状态',\n" +
            "  cre.true_name as '创建人'\n" +
            "FROM\n" +
            "\ttraining_manager_task tmt\n" +
            "INNER JOIN enum_const tt on tt.id=tmt.flag_task_type\n" +
            "INNER JOIN enum_const pt on pt.id=tmt.flag_priority_level\n" +
            "INNER JOIN enum_const ttt on ttt.id=tmt.flag_training_task_status\n" +
            "INNER JOIN enum_const nw on nw.id=tmt.flag_notify_way\n" +
            "INNER JOIN pe_manager chr on tmt.charge_persons like concat('%',chr.id,'%')\n" +
            "INNER JOIN pe_manager cop on tmt.copy_persons like concat('%',cop.id,'%')\n" +
            "INNER JOIN pe_manager cre on cre.id=tmt.create_manager\n" +
            "INNER JOIN sso_user sso on sso.id=cre.fk_sso_user_id \n" +
            "where " +
            "\t[ids|this.id]\n" +
            "GROUP BY tmt.id\n";

    /**
     * 易耗品管理
     */
    String CONSUMABLE_MANAGE_BASIC_SQL = "SELECT\n" +
            "  pc.id AS id,\n" +
            "  fup.`NAME` AS '领用用途',\n" +
            "  (CASE \n" +
            "\t\tWHEN fup.`CODE` = '0' THEN cl.name  \n" +
            "                WHEN fup.`CODE` = '1' THEN  pm1.`name`  \n" +
            "\t\tELSE '' \n" +
            "\t\tEND\n" +
            "\t) AS '领用单位',\n" +
            "  pc.note AS '领用备注', \n" +
            "  fus.name AS '领用状态',\n" +
            "  pc.applicant_user_tel AS '申请人手机号',\n" +
            "  fcs.name AS '验收状态',\n" +
            "  pm3.`name` AS '验收人',\n" +
            "  pc.check_time AS '验收时间',\n" +
            "  pc.check_price AS '验收金额'\n" +
            "FROM\n" +
            "\tpe_consumable pc\n" +
            "INNER JOIN enum_const fup ON fup.ID = pc.flag_use_purpose\n" +
            "LEFT JOIN enum_const fus ON fus.id = pc.flag_use_status\n" +
            "LEFT JOIN pe_class cl ON cl.id = pc.item_id\n" +
            "LEFT JOIN pe_manager pm1 ON pc.fk_applicant_user_id = pm1.fk_sso_user_id \n" +
            "LEFT JOIN pe_manager pm3 ON pc.fk_check_user_id = pm3.fk_sso_user_id\n" +
            "LEFT JOIN enum_const fcs ON fcs.id = pc.flag_check_status\n" +
            "where \n" +
            "\t[ids|pc.id]\n" +
            "  AND pc.site_code = '${siteCode}'\n" +
            "group by pc.id";

    /**
     * 易耗品物品管理
     */
    String CONSUMABLE_ITEM_MANAGE_BASIC_SQL = "SELECT \n" +
            "pci.id AS id,\n" +
            "pci.name AS '物品名称',\n" +
            "pci.price AS '单价' ,\n" +
            "pci.note AS '备注',\n" +
            "fct.name AS '所属种类',\n" +
            "fiv.`NAME` AS '是否有效'\n" +
            "FROM pe_consumable_item pci \n" +
            "INNER JOIN enum_const fct on pci.flag_consumable_type = fct.id\n" +
            "INNER JOIN enum_const fiv on pci.flag_is_valid = fiv.id\n" +
            "WHERE " +
            "\t[ids|pci.id]\n" +
            " AND pci.site_code = '${siteCode}'";
}
