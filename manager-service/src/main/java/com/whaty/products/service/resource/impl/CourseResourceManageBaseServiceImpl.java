package com.whaty.products.service.resource.impl;

import com.whaty.constant.CommonConstant;
import com.whaty.core.commons.datasource.MasterSlaveRoutingDataSource;
import com.whaty.core.commons.exception.EntityException;
import com.whaty.core.framework.async.Task;
import com.whaty.core.framework.bean.Site;
import com.whaty.core.framework.grid.bean.GridConfig;
import com.whaty.core.framework.user.service.UserService;
import com.whaty.core.framework.util.BeanNames;
import com.whaty.dao.GeneralDao;
import com.whaty.domain.bean.PeCourse;
import com.whaty.domain.bean.PeUnit;
import com.whaty.framework.asserts.TycjParameterAssert;
import com.whaty.framework.config.util.SiteUtil;
import com.whaty.framework.exception.ParameterIllegalException;
import com.whaty.framework.grid.adapter.service.impl.TycjGridServiceAdapter;
import com.whaty.products.service.resource.constant.ResourceConstants;
import com.whaty.util.CommonUtils;
import com.whaty.utils.UserUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * 课程资源服务类 不操作课程空间
 *
 * @author pingzhihao
 */
@Lazy
@Service("courseResourceManageBaseService")
public class CourseResourceManageBaseServiceImpl extends TycjGridServiceAdapter<PeCourse> {

    @Resource(name = CommonConstant.GENERAL_DAO_BEAN_NAME)
    protected GeneralDao myGeneralDao;

    @Resource(name = BeanNames.USER_SERVICE)
    protected UserService userService;

    /**
     * 根据课程Id获取图片路径
     *
     * @param courseId 课程id
     * @return 课程名、课程图片的路径
     */
    public Map<String, Object> getPhotoPreviewList(String courseId) {
        if (StringUtils.isBlank(courseId)) {
            return null;
        }
        return this.myGeneralDao.getOneMapBySQL("SELECT course_url as photo, NAME as name FROM pe_course" +
                " WHERE id = ?", courseId);
    }

    @Override
    public void checkBeforeAdd(PeCourse bean) throws EntityException {
        bean.setCode(this.generateCourseCode());
        bean.setCreateBy(UserUtils.getCurrentUser());
        bean.setCreateDate(new Date());
        this.checkBeforeAddOrUpdate(bean);
    }

    @Override
    public void checkBeforeUpdate(PeCourse bean) throws EntityException {
        this.checkBeforeAddOrUpdate(bean);
    }

    protected void checkBeforeAddOrUpdate(PeCourse bean) throws EntityException {
        String additionalSql = bean.getId() == null ? "" : " and id<>'" + bean.getId() + "'";
        if (this.myGeneralDao.checkNotEmpty("select 1 from pe_course where name = ? and site_code = ? " +
                    "and fk_teacher_id = ?" + additionalSql, bean.getName(), MasterSlaveRoutingDataSource.getDbType(),
                bean.getPeTeacher().getId())) {
            throw new EntityException("此讲师已存在此名称的课程");
        }
        if (this.myGeneralDao.checkNotEmpty("select 1 from pe_course where code = ? and site_code = ? " +
                    "and fk_teacher_id = ?" + additionalSql, bean.getCode(), MasterSlaveRoutingDataSource.getDbType(),
                bean.getPeTeacher().getId())) {
            throw new EntityException("此讲师已存在此编号的课程");
        }
    }

    @Override
    protected List<PeCourse> readExcelToBean(Task task, File excelFile, Workbook workbook, GridConfig gridConfig,
                                             Map<String, Object> params, Site site) throws EntityException {
        List<PeCourse> courseList = super.readExcelToBean(task, excelFile, workbook, gridConfig, params, site);
        String code = UserUtils.getCurrentUnit().getCode() + CommonUtils.changeDateToString(new Date(), "yy");
        int maxCode = Integer.parseInt(this.generateCourseCode().replace(code, ""));
        for (PeCourse course : courseList) {
            course.setCode(code + CommonUtils.leftAddZero(maxCode + 1, 4));
            maxCode++;
        }
        return courseList;
    }

    /**
     * 获取课程图片
     *
     * @param courseId
     * @return
     */
    public Map<String, Object> getCourseImage(String courseId) {
        TycjParameterAssert.isAllNotBlank(courseId);
        String url = this.myGeneralDao.getOneBySQL("select course_url from pe_course where id = ?", courseId);
        if (StringUtils.isBlank(url)) {
            return null;
        }
        String fileName = url.substring(url.lastIndexOf("/") + 1);
        Map<String, Object> imageInfo = new HashMap<>(2);
        imageInfo.put("name", fileName);
        imageInfo.put("url", url);
        return imageInfo;
    }

    /**
     * 上传课程图片
     *
     * @param ids
     * @param file
     * @param filename
     * @throws IOException
     */
    public void doUploadCourseImage(String ids, File file, String filename) throws IOException {
        TycjParameterAssert.isAllNotBlank(ids, filename);
        TycjParameterAssert.isAllNotNull(file);
        Map<String, Object> courseInfo = this.myGeneralDao
                .getOneMapBySQL("select code, course_url as url from pe_course where id = ?", ids);
        if (MapUtils.isEmpty(courseInfo)) {
            throw new ParameterIllegalException();
        }
        String type = filename.substring(filename.lastIndexOf("."));
        String webPath = String.format(ResourceConstants.COURSE_IMAGE_URL, SiteUtil.getSiteCode(),
                courseInfo.get("code") + type);
        this.myGeneralDao.executeBySQL("update pe_course set course_url = ? where id = ?", webPath, ids);
        File bakFile = null;
        if (StringUtils.isNotBlank((String) courseInfo.get("url"))) {
            File oldFile = new File(CommonUtils.getRealPath((String) courseInfo.get("url")));
            if (oldFile.exists()) {
                bakFile = CommonUtils.bakFile(oldFile);
                oldFile.delete();
            }
        }
        try {
            FileUtils.copyFile(file, new File(CommonUtils.getRealPath(webPath)));
        } catch (Exception e) {
            if (bakFile != null) {
                CommonUtils.rollbackFile(bakFile);
            }
            throw e;
        } finally {
            if (bakFile != null) {
                bakFile.delete();
            }
        }
    }

    /**
     * 生成课程代码
     *
     * @return
     */
    private String generateCourseCode() throws EntityException {
        PeUnit pe = UserUtils.getCurrentUnit();
        if (pe == null) {
            throw new EntityException("没有单位的用户无法添加课程");
        }
        String code = pe.getCode() + CommonUtils.changeDateToString(new Date(), "yy");
        String sql = "select max(code) from pe_course where length(code) = ? and code like '" + code + "%'";
        String maxCode = this.myGeneralDao.getOneBySQL(sql, code.length() + 4);
        if (StringUtils.isNotBlank(maxCode)) {
            maxCode = maxCode.replace(code, "");
            maxCode = code + CommonUtils.leftAddZero(Integer.parseInt(maxCode) + 1, 4);
        } else {
            maxCode = code + CommonUtils.leftAddZero(1, 4);
        }
        return maxCode;
    }
}
