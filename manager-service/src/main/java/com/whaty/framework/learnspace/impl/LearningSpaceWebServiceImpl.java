package com.whaty.framework.learnspace.impl;

import com.whaty.framework.config.util.LearnSpaceUtil;
import com.whaty.products.learning.webservice.LearningSpaceWebService;
import com.whaty.products.learning.webservice.bean.LearnAttrBean;
import com.whaty.framework.learnspace.GeneralHessianProxy;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 课程空间hessian代理类
 *
 * @author weipengsen
 */
@Lazy
@Service("learningSpaceWebService")
public class LearningSpaceWebServiceImpl implements LearningSpaceWebService {

    @Resource(name = "generalHessianProxy")
    private GeneralHessianProxy generalHessianProxy;

    public LearningSpaceWebService getLearningSpaceWebService(String siteCode) {
        return generalHessianProxy.getTargetSource(LearningSpaceWebService.class, siteCode);
    }

    @Override
    public List<Object[]> getDateFromLearnSpace(String arg0, String arg1,
                                                String arg2, String arg3) throws Exception {
        return getLearningSpaceWebService(arg3).getDateFromLearnSpace(arg0, arg1, arg2, arg3);
    }

    @Override
    public Map<String, Map<String, String>> getElectiveInfo(String[] arg0,
                                                            String arg1) throws Exception {
        return getLearningSpaceWebService(arg1).getElectiveInfo(arg0, arg1);
    }

    @Override
    public Map<String, Map<String, String>> getElectiveInfo(String[] strings, String s, String[] strings1) throws Exception {
        return null;
    }

    @Override
    public Map<String, Map<String, String>> getElectiveInfoByLoginId(String s, String s1) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<Object[]> getLearnTimes(String arg0, String arg1)
            throws Exception {
        return getLearningSpaceWebService(arg1).getLearnTimes(arg0, arg1);
    }

    @Override
    public boolean removeCourse(String arg0, String arg1) throws Exception {
        return getLearningSpaceWebService(arg1).removeCourse(arg0, arg1);
    }

    @Override
    public boolean removeCourseTeacher(String arg0, String arg1)
            throws Exception {
        return getLearningSpaceWebService(arg1).removeCourseTeacher(arg0, arg1);
    }

    @Override
    public boolean removeElective(String arg0, String arg1) throws Exception {
        return getLearningSpaceWebService(arg1).removeElective(arg0, arg1);
    }

    @Override
    public boolean removeManager(String arg0, String arg1) throws Exception {
        return getLearningSpaceWebService(arg1).removeManager(arg0, arg1);
    }

    @Override
    public boolean removeStudent(String arg0, String arg1) throws Exception {
        return getLearningSpaceWebService(arg1).removeStudent(arg0, arg1);
    }

    @Override
    public boolean removeTeacher(String arg0, String arg1) throws Exception {
        return getLearningSpaceWebService(arg1).removeTeacher(arg0, arg1);
    }

    @Override
    @Deprecated
    public boolean saveCopyCourse(String arg0, String arg1, String arg2,
                                  String arg3) throws Exception {
        return getLearningSpaceWebService(LearnSpaceUtil.getLearnSpaceSiteCode())
                .saveCopyCourse(arg0, arg1, arg2, arg3);
    }

    public boolean saveCopyCourse(String arg0, String arg1, String arg2,
                                  String arg3, String arg4, String siteCode) throws Exception {
        return getLearningSpaceWebService(siteCode).saveCopyCourse(arg0, arg1, arg2, arg3, arg4);
    }

    @Override
    @Deprecated
    public boolean saveCopyCourse(String arg0, String arg1, String arg2,
                                  String arg3, String arg4) throws Exception {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean saveCopyCourse(String s, String s1, String s2, String s3, String s4, String s5, String s6, String s7) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean saveCourse(String arg0, String arg1, String arg2, String arg3)
            throws Exception {
        return getLearningSpaceWebService(arg3).saveCourse(arg0, arg1, arg2, arg3);
    }

    @Override
    public boolean saveCourseBatch(List<String[]> arg0) throws Exception {
        String[] strings = arg0.get(0);
        String siteCode = strings[3];
        return getLearningSpaceWebService(siteCode).saveCourseBatch(arg0);
    }


    @Override
    public boolean saveCourseTeacher(String arg0, String arg1, String arg2,
                                     String arg3) throws Exception {
        return getLearningSpaceWebService(arg3).saveCourseTeacher(arg0, arg1, arg2, arg3);
    }

    @Override
    public boolean saveCourseTeacherBatch(List<String[]> arg0) throws Exception {
        String[] strings = arg0.get(0);
        String siteCode = strings[3];
        return getLearningSpaceWebService(siteCode).saveCourseTeacherBatch(arg0);
    }

    @Override
    public boolean saveElective(String arg0, String arg1, String arg2,
                                String arg3, boolean arg4, String arg5) throws Exception {
        return getLearningSpaceWebService(arg5).saveElective(arg0, arg1, arg2, arg3, arg4, arg5);
    }

    @Override
    public boolean saveElectiveBatch(List<String[]> arg0) throws Exception {
        String[] strings = arg0.get(0);
        String siteCode = strings[5];
        return getLearningSpaceWebService(siteCode).saveElectiveBatch(arg0);
    }

    @Override
    public boolean saveManager(String arg0, String arg1, String arg2,
                               String arg3, String arg4, String arg5, String arg6)
            throws Exception {
        return getLearningSpaceWebService(arg5).saveManager(arg0, arg1, arg2, arg3, arg4, arg5, arg6);
    }

    @Override
    public boolean saveManagerBatch(List<String[]> arg0) throws Exception {
        String[] strings = arg0.get(0);
        String siteCode = strings[5];
        return getLearningSpaceWebService(siteCode).saveManagerBatch(arg0);
    }

    @Override
    public boolean saveSudent(String arg0, String arg1, String arg2,
                              String arg3, String arg4, String arg5, String arg6)
            throws Exception {
        return getLearningSpaceWebService(arg5).saveSudent(arg0, arg1, arg2, arg3, arg4, arg5, arg6);
    }

    @Override
    public boolean saveSudentBatch(List<String[]> arg0) throws Exception {
        String[] strings = arg0.get(0);
        String siteCode = strings[5];
        return getLearningSpaceWebService(siteCode).saveSudentBatch(arg0);
    }

    @Override
    public boolean saveTeacher(String arg0, String arg1, String arg2,
                               String arg3, String arg4, String arg5, String arg6)
            throws Exception {
        return getLearningSpaceWebService(arg5).saveTeacher(arg0, arg1, arg2, arg3, arg4, arg5, arg6);
    }

    @Override
    public boolean saveTeacherBatch(List<String[]> arg0) throws Exception {
        String[] strings = arg0.get(0);
        String siteCode = strings[5];
        return getLearningSpaceWebService(siteCode).saveTeacherBatch(arg0);
    }

    @Override
    public void updateCourse(String arg0, String arg1, String arg2,
                             String arg3, String arg4) throws Exception {
        getLearningSpaceWebService(arg4).updateCourse(arg0, arg1, arg2, arg3, arg4);
    }

    @Override
    public boolean updateElective(String arg0, boolean arg1, String arg2)
            throws Exception {
        return getLearningSpaceWebService(arg2).updateElective(arg0, arg1, arg2);
    }

    @Override
    public boolean updateManager(String arg0, String arg1, String arg2,
                                 String arg3, String arg4, String arg5, String arg6)
            throws Exception {
        return getLearningSpaceWebService(arg6).updateManager(arg0, arg1, arg2, arg3, arg4, arg5, arg6);
    }

    @Override
    public boolean updateStudent(String arg0, String arg1, String arg2,
                                 String arg3, String arg4) throws Exception {
        return getLearningSpaceWebService(arg4).updateStudent(arg0, arg1, arg2, arg3, arg4);
    }

    @Override
    public boolean updateTeacher(String arg0, String arg1, String arg2,
                                 String arg3, String arg4) throws Exception {
        return getLearningSpaceWebService(arg4).updateTeacher(arg0, arg1, arg2, arg3, arg4);
    }


    /**
     * 课程分配教师的同步接口
     *
     * @param id          关系ID(不为空)
     * @param teacherId   教师ID(不为空)
     * @param courseId    课程ID(不为空)
     * @param siteCode    站点code(必填)
     * @param teachertype 教师类型(不为空) 1:授课2:辅导
     * @return 返回true时添加成功，否则添加失败
     * @throws Exception
     */
    @Override
    public boolean saveCourseTeacherType(String id, String teacherId, String
            courseId, String siteCode, String teachertype) throws Exception {
        return getLearningSpaceWebService(siteCode).saveCourseTeacherType(id, teacherId, courseId, siteCode, teachertype);
    }

    @Override
    public Map<String, Object> getSingleCourseInfo(String s, String s1) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<Map<String, Object>> getCourseInfoList(String s, String s1) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean saveHomework(Map<String, String> map) throws Exception {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean updateHomework(Map<String, String> map) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean deleteHomework(String s) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<Object[]> getNote(String arg0, String arg1, String arg2) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean updateUserInfo(Map<String, String> arg0) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean removeCourseTeacherBatch(List<Map<String, String>> list)
            throws Exception {
        String siteCode = list.get(0).get("siteCode");
        List<Map<String, String>> clist = new ArrayList<Map<String, String>>();
        for (Map<String, String> map : list) {
            Map<String, String> teMap = new HashMap<String, String>();
            teMap.put("teacherid", map.get("teacherid"));
            teMap.put("courseid", map.get("courseid"));
            clist.add(teMap);
        }
        return getLearningSpaceWebService(siteCode).removeCourseTeacherBatch(clist);
    }

    @Override
    public boolean removeElectiveBatch(List<Map<String, String>> list) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean saveSystemMessage(String s, int i, String s1, String s2, String s3, String s4)
            throws Exception {
        return getLearningSpaceWebService(s4).saveSystemMessage(s, 1, s1, s2, s3, s4);
    }

    @Override
    public String getMessageList(String s, String s1, Boolean aBoolean, int i, int i1) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getMessageInfo(String s) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean updateMessageRead(String s)  {
        throw new UnsupportedOperationException();
    }

    @Override
    public Map<String, String> queryCourseTopic(String s) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Map<String, Object> querySiteDataStatistics(String s) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void createWords(List<Map<String, Object>> list) {

    }

    @Override
    public Map<String, Object> saveVoteItemRecord(int i, int i1, String s, String s1, String s2, String s3) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Map<String, Object> saveLearnSpaceVoteStatus(String s, String s1, int i, String s2, String s3) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean saveClass(String s, String s1, String s2) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean saveClass(List<String[]> list) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean saveClassUser(String s, String s1) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean saveClassUser(List<String[]> list) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean removeClassTeacher(String s, String s1) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean saveElective(String s, String s1, String s2, String s3, boolean b, String s4, String s5) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean saveElectiveClassBatch(List<String[]> list) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean updateStudentClass(String s, String s1, String s2, String s3) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Map<String, Map<Object, Object>> queryCourseTopicInfo(String s, String s1) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean updateLabelType(String s, String s1, String s2) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean saveStudent(LearnAttrBean learnAttrBean) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean saveStudentBatch(List<LearnAttrBean> list) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean updateStudent(LearnAttrBean learnAttrBean) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Map<String, Object> getStudentLabel(String s, String s1) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Map<String, List<String>> getStudentLabelBatch(List<String> list, String s, String s1) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Map<String, Object> getStudentClassBatch(List<String> list, String s, String s1) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<Map<String, Object>> getlabelTypesBySiteCode(String s) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<Map<String, Object>> getClassInfos(String s) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Map<String, Object> getLabelInfoAndStudentLabel(List<String> list, String s, String s1) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<Map<String, Object>> getLabelTypeBySiteCode(String s, String s1) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<Map<String, Object>> getLabelTypeByDomain(String s, String s1) throws Exception {
        return null;
    }

    @Override
    public boolean convertUserRole(String s, String s1, String s2) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Map<String, Object> getComponentIdByItemId(String s) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Map<String, Object> getStuScoreDetail(String s, String s1) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Map<String, Object> getStuScoreDetail(String s, String s1, String s2) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<Map> getStuScoreDetailBatch(String s) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<Map> getStuScoreDetailBatch(String s, String s1) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Map<String, Object> getTeacherUndoDealCount(Object o) throws Exception {
        return null;
    }

    @Override
    public List<Map<String, Object>> getStudentLearnTime(String s, List<String> list, List<String> list1) throws Exception {
        return null;
    }

    @Override
    public List<Map<String, Object>> getStudentLearnStatus(String s, String s1, List<String> list) throws Exception {
        return null;
    }

    @Override
    public List<Map<String, Object>> getStudentStatisData(String s, List<String> list) throws Exception {
        return null;
    }

    @Override
    public List<Map<String, Object>> getStuLearnDataByDateTime(String s, String s1, String s2) throws Exception {
        return getLearningSpaceWebService(s).getStuLearnDataByDateTime(s, s1, s2);
    }

    @Override
    public List<Map<String, Object>> getStuLearnDataByDateTime(String s, String s1, String s2, String[] strings) throws Exception {
        return null;
    }

    @Override
    public boolean saveSystemMessage(String s, int i, String s1, String s2, String s3, String s4,
                                     String s5) throws Exception {
        return getLearningSpaceWebService(s5).saveSystemMessage(s, 1, s1, s2, s3, s4, s5);
    }


}
