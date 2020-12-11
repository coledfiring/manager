package com.whaty.framework.api.onlineExam.impl;

import com.whaty.constant.CommonConstant;
import com.whaty.dao.GeneralDao;
import com.whaty.framework.api.onlineExam.SyncOnlineExamScoreService;
import com.whaty.util.ValidateUtils;
import net.sf.json.JSONObject;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 同步在线考试成绩信息服务类
 *
 * @author suoqiangqiang
 */
@Lazy
@Service("syncOnlineExamScoreService")
public class SyncOnlineExamScoreServiceImpl implements SyncOnlineExamScoreService {


    @Resource(name = CommonConstant.GENERAL_DAO_BEAN_NAME)
    private GeneralDao myGeneralDao;

    @Override
    public Map<String, String> doSyncOnlineExamScore(Map<String, String> requestMap) {
        Map<String, String> resultMap = new HashMap<>();
        String examStu = requestMap.get("attachData");
        String studentScore = requestMap.get("studentScore");
        String loginId = requestMap.get("loginId");
        String batchCode = requestMap.get("batchCode");
        StringBuilder sql = new StringBuilder();
        sql.append(" insert into student_online_exam_score_history (id,fk_exam_student_id,jsonData,create_date) ");
        sql.append(" values                                                                                     ");
        sql.append(" (                                                                                          ");
        sql.append("  replace (uuid(), '-', ''),                                                                ");
        sql.append("  '" + examStu + "',                                                                        ");
        sql.append("  '" + JSONObject.fromObject(requestMap) + "',                                              ");
        sql.append("  now()                                                                                     ");
        sql.append(" )                                                                                          ");
        this.myGeneralDao.executeBySQL(sql.toString());
        sql.delete(0, sql.length());
        if (StringUtils.isBlank(examStu) || StringUtils.isBlank(studentScore) || StringUtils.isBlank(loginId) ||
                StringUtils.isBlank(batchCode)) {
            resultMap.put("retCode", "-1");
            resultMap.put("retMsg", "参数错误");
            return resultMap;
        }
        if (!ValidateUtils.SCORE_REG_PATTERN.matcher(studentScore).find()) {
            resultMap.put("retCode", "-1");
            resultMap.put("retMsg", "分数必须为0-100的小数或整数");
            return resultMap;
        }
        sql.append(" select                                                                               ");
        sql.append("   stu.id as id,                                                                      ");
        sql.append("   exam.id as examId,                                                                 ");
        sql.append("   sc.id as scId                                                                      ");
        sql.append(" from                                                                                 ");
        sql.append("   pe_student stu                                                                     ");
        sql.append(" inner join sso_user sso on sso.id=stu.fk_sso_user_id                                 ");
        sql.append(" inner join class_online_exam exam on exam.paper_no = '" + batchCode + "'             ");
        sql.append(" left join student_online_exam_score sc on sc.id='" + examStu + "'                    ");
        sql.append(" where                                                                                ");
        sql.append("   sso.login_id = '" + loginId + "'                                                   ");
        List<Map<String, Object>> checkList = this.myGeneralDao.getMapBySQL(sql.toString());
        if (CollectionUtils.isEmpty(checkList)) {
            resultMap.put("retCode", "-1");
            resultMap.put("retMsg", "学生信息有误");
            return resultMap;
        }
        String scId = (String) checkList.get(0).get("scId");
        String examId = (String) checkList.get(0).get("examId");
        String stuId = (String) checkList.get(0).get("id");
        if (StringUtils.isBlank(scId)) {
            sql.delete(0, sql.length());
            sql.append(" insert into student_online_exam_score (           ");
            sql.append("     id,                                           ");
            sql.append("     fk_class_online_exam_id,                      ");
            sql.append("     fk_student_id,                                ");
            sql.append("     update_time,                                  ");
            sql.append("     create_time,                                  ");
            sql.append("     score                                         ");
            sql.append(" ) values (                                        ");
            sql.append("     ?,                                            ");
            sql.append("     ?,                                            ");
            sql.append("     ?,                                            ");
            sql.append("     now(),                                        ");
            sql.append("     now(),                                        ");
            sql.append("     ?                                             ");
            sql.append(" )                                                 ");
            this.myGeneralDao.executeBySQL(sql.toString(), examStu, examId, stuId, studentScore);
        }
        sql.delete(0, sql.length());
        sql.append(" update student_online_exam_score examStu                                             ");
        sql.append(" inner join enum_const sp on sp.namespace='flagScorePublish'                          ");
        sql.append(" and sp.code='1'                                                                      ");
        sql.append(" set examStu.score = " + studentScore + ",                                            ");
        sql.append(" examStu.flag_score_publish=sp.id                                                     ");
        sql.append(" where                                                                                ");
        sql.append("   examStu.id = '" + examStu + "'                                                     ");
        sql.append(" and ifnull(examStu.score,0) < " + studentScore + "                                   ");
        this.myGeneralDao.executeBySQL(sql.toString());
        resultMap.put("retCode", "0");
        resultMap.put("retMsg", "");
        return resultMap;
    }
}
