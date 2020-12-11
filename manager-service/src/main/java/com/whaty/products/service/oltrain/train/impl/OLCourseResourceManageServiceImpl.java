package com.whaty.products.service.oltrain.train.impl;

import com.whaty.constant.CommonConstant;
import com.whaty.core.commons.datasource.MasterSlaveRoutingDataSource;
import com.whaty.core.commons.exception.EntityException;
import com.whaty.core.framework.grid.bean.GridConfig;
import com.whaty.core.framework.grid.bean.menu.AbstractGridMenu;
import com.whaty.core.framework.grid.bean.menu.ToUrlGridMenu;
import com.whaty.dao.GeneralDao;
import com.whaty.domain.bean.online.OlPeCourse;
import com.whaty.framework.asserts.TycjParameterAssert;
import com.whaty.framework.config.util.LearnSpaceUtil;
import com.whaty.framework.config.util.SiteUtil;
import com.whaty.framework.exception.LearningSpaceException;
import com.whaty.framework.exception.ParameterIllegalException;
import com.whaty.framework.exception.UncheckException;
import com.whaty.framework.grid.adapter.service.impl.TycjGridServiceAdapter;
import com.whaty.framework.grid.doamin.ExcelCheckResult;
import com.whaty.products.learning.webservice.LearningSpaceWebService;
import com.whaty.products.service.resource.constant.ResourceConstants;
import com.whaty.util.CommonUtils;
import com.whaty.utils.UserUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.management.MalformedObjectNameException;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 在线培训课程资源管理服务类
 *
 * @author suoqiangqiang
 */
@Lazy
@Service("olCourseResourceManageService")
public class OLCourseResourceManageServiceImpl extends TycjGridServiceAdapter<OlPeCourse> {

    @Resource(name = CommonConstant.GENERAL_DAO_BEAN_NAME)
    private GeneralDao myGeneralDao;

    @Resource(name = CommonConstant.OPEN_GENERAL_DAO_BEAN_NAME)
    private GeneralDao openGeneralDao;

    @Resource(name = CommonConstant.LEARNING_SPACE_WEB_SERVICE_BEAN_NAME)
    private LearningSpaceWebService learningSpaceWebService;

    @Override
    public void initGrid(GridConfig gridConfig, Map<String, Object> mapParam) {
        String loginId = UserUtils.getCurrentUser().getLoginId();
        String domain;
        try {
            domain = CommonUtils.getServerName() + ":" + CommonUtils.getServerPort();
        } catch (MalformedObjectNameException e) {
            throw new UncheckException(e);
        }
        List<AbstractGridMenu> gridMenuList = gridConfig.getGridMenuList();
        //课程设计按钮
        String urlDesign = String.format(ResourceConstants.LEARNING_SPACE_COURSE_DESIGN,
                LearnSpaceUtil.getHttpClientUrl(),
                loginId, LearnSpaceUtil.getLearnSpaceSiteCode(), domain);
        ((ToUrlGridMenu) gridMenuList.get(0)).setUrl(urlDesign);
        //课程预览按钮
        String urlPreview = String.format(ResourceConstants.LEARNING_SPACE_COURSE_PREPARE_SHOW,
                LearnSpaceUtil.getHttpClientUrl(),
                loginId, LearnSpaceUtil.getLearnSpaceSiteCode(), domain);
        ((ToUrlGridMenu) gridMenuList.get(1)).setUrl(urlPreview);
    }

    @Override
    public void checkBeforeAdd(OlPeCourse bean) throws EntityException {
        this.checkBeforeAddOrUpdate(bean);
        bean.setCreateDate(new Date());
        bean.setCreateBy(UserUtils.getCurrentUser());
    }

    @Override
    public void checkBeforeUpdate(OlPeCourse bean) throws EntityException {
        this.checkBeforeAddOrUpdate(bean);
    }

    @Override
    public void afterAdd(OlPeCourse bean) throws EntityException {
        if (LearnSpaceUtil.learnSpaceIsOpen()) {
            try {
                String siteCode = SiteUtil.getSiteCode();
                if (!learningSpaceWebService
                        .saveCourse(bean.getId(), bean.getName(), bean.getCode(), siteCode)) {
                    throw new LearningSpaceException();
                }
            } catch (Exception e) {
                throw new EntityException("课程空间同步失败");
            }
        }
    }

    @Override
    public void afterExcelImport(List<OlPeCourse> beanList) throws EntityException {
        if (LearnSpaceUtil.learnSpaceIsOpen()) {
            String siteCode = LearnSpaceUtil.getLearnSpaceSiteCode();
            try {
                List<String[]> batchSaveList =
                        beanList.stream().map(e -> new String[]{e.getId(), e.getName(),
                                e.getCode(), siteCode}).collect(Collectors.toList());
                learningSpaceWebService.saveCourseBatch(batchSaveList);
            } catch (Exception e) {
                throw new EntityException("课程空间同步失败");
            }
        }
    }

    @Override
    protected void afterUpdate(OlPeCourse bean) throws EntityException {
        if (LearnSpaceUtil.learnSpaceIsOpen()) {
            try {
                try {
                    learningSpaceWebService.updateCourse(bean.getId(), bean.getName(),
                            bean.getCode(), null, MasterSlaveRoutingDataSource.getDbType());
                } catch (Exception e) {
                    throw new LearningSpaceException(e);
                }
            } catch (Exception e) {
                throw new EntityException("课程空间同步失败");
            }
        }
    }

    @Override
    protected void afterDelete(List idList) throws EntityException {
        if (LearnSpaceUtil.learnSpaceIsOpen()) {
            try {
                try {
                    String ids = CommonUtils.join(idList, CommonConstant.SPLIT_ID_SIGN, null);
                    learningSpaceWebService.removeCourse(ids, LearnSpaceUtil.getLearnSpaceSiteCode());
                } catch (Exception e) {
                    throw new LearningSpaceException(e);
                }
            } catch (Exception e) {
                throw new EntityException("课程空间同步失败");
            }
        }
    }

    @Override
    protected ExcelCheckResult checkExcelImportError(List<OlPeCourse> beanList, Workbook workbook,
                                                     ExcelCheckResult excelCheckResult) {
        Map<String, Map<String, List<Integer>>> sameDataMap = new HashMap<>();
        sameDataMap.put("表格中已存在相同编码的课程；", this.checkSameField(beanList, Arrays.asList("code")));
        return this.checkSameData(sameDataMap, workbook, excelCheckResult);
    }

    /**
     * 添加、修改前校验
     * @param bean
     * @throws EntityException
     */
    private void checkBeforeAddOrUpdate(OlPeCourse bean) throws EntityException {
        String additionalSql = bean.getId() == null ? "" : " and id<>'" + bean.getId() + "'";
        if (this.myGeneralDao.checkNotEmpty("select 1 from ol_pe_course where code = ? and site_code = ? "
                + additionalSql, bean.getCode(), MasterSlaveRoutingDataSource.getDbType())) {
            throw new EntityException("已存在此编码的课程");
        }
    }

    /**
     * 获取课程图片
     *
     * @param courseId
     * @return
     */
    public Map<String, Object> getCourseImage(String courseId) {
        TycjParameterAssert.isAllNotBlank(courseId);
        String url = this.myGeneralDao.getOneBySQL("select course_url from ol_pe_course where id = ?", courseId);
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
                .getOneMapBySQL("select code, course_url as url from ol_pe_course where id = ?", ids);
        if (MapUtils.isEmpty(courseInfo)) {
            throw new ParameterIllegalException();
        }
        String type = filename.substring(filename.lastIndexOf("."));
        String webPath = String.format(ResourceConstants.COURSE_IMAGE_URL, SiteUtil.getSiteCode(),
                courseInfo.get("code") + type);
        this.myGeneralDao.executeBySQL("update ol_pe_course set course_url = ? where id = ?", webPath, ids);
        CommonUtils.replaceSameNameFile(file, (String) courseInfo.get("url"), webPath);
    }
}
