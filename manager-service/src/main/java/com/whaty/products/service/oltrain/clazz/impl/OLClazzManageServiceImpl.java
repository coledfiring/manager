package com.whaty.products.service.oltrain.clazz.impl;

import com.whaty.constant.CommonConstant;
import com.whaty.core.commons.exception.EntityException;
import com.whaty.core.framework.grid.bean.GridConfig;
import com.whaty.dao.GeneralDao;
import com.whaty.domain.bean.online.OlPeClass;
import com.whaty.framework.asserts.TycjParameterAssert;
import com.whaty.framework.config.util.SiteUtil;
import com.whaty.framework.exception.ParameterIllegalException;
import com.whaty.framework.exception.ServiceException;
import com.whaty.framework.grid.adapter.service.impl.TycjGridServiceAdapter;
import com.whaty.products.service.resource.constant.ResourceConstants;
import com.whaty.util.CommonUtils;
import com.whaty.utils.UserUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 在线培训班级管理服务类
 *
 * @author suoqiangqiang
 */
@Lazy
@Service("olClazzManageService")
public class OLClazzManageServiceImpl extends TycjGridServiceAdapter<OlPeClass> {

    @Resource(name = CommonConstant.GENERAL_DAO_BEAN_NAME)
    private GeneralDao myGeneralDao;

    @Override
    public void checkBeforeAdd(OlPeClass bean) throws EntityException {
        this.checkBeforeAddOrUpdate(bean);
        bean.setCreateBy(UserUtils.getCurrentUser());
        bean.setCreateDate(new Date());
        bean.setCode(this.generateCode());
    }

    @Override
    public void checkBeforeUpdate(OlPeClass bean) throws EntityException {
        this.checkBeforeAddOrUpdate(bean);
    }

    private void checkBeforeAddOrUpdate(OlPeClass bean) throws EntityException {
        if (bean.getStartTime().after(bean.getEndTime())) {
            throw new EntityException("开始时间必须在结束时间之前");
        }
        if (com.whaty.common.string.StringUtils.isNotBlank(bean.getName())) {
            String sql = "select 1 from ol_pe_class where name = ? " +
                    (com.whaty.common.string.StringUtils.isNotBlank(bean.getId()) ? "AND id <> '" + bean.getId() + "'" : "");
            if (this.myGeneralDao.checkNotEmpty(sql, bean.getName())) {
                throw new EntityException("班级名称已存在");
            }
        }
    }
    /**
     * 通过引导创建数据
     *
     * @param bean
     * @param params
     * @param gridConfig
     * @return
     */
    public String createByFlow(OlPeClass bean, Map<String, Object> params, GridConfig gridConfig)
            throws ServiceException {
        try {
            this.mergeBeanByGridConfig(bean, bean, gridConfig, false);
            this.checkBeforeAdd(bean);
            this.checkBeforeAdd(bean, params);
            this.myGeneralDao.save(bean);
            this.afterAdd(bean);
            return bean.getId();
        } catch (EntityException ex) {
            throw new ServiceException(ex.getMessage());
        }
    }

    /**
     * 更新基础信息
     * @param bean
     * @param gridConfig
     */
    public Map updateBasicInfo(OlPeClass bean, GridConfig gridConfig) {
        return this.update(bean, gridConfig);
    }

    /**
     * 获取班级图片
     *
     * @param courseId
     * @return
     */
    public Map<String, Object>  getClassImage(String courseId) {
        TycjParameterAssert.isAllNotBlank(courseId);
        String url = this.myGeneralDao.getOneBySQL("select picture_url from ol_pe_class where id = ?", courseId);
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
     * 上传班级图片
     *
     * @param ids
     * @param file
     * @param filename
     * @throws IOException
     */
    public void doUploadClassImage(String ids, File file, String filename) throws IOException {
        TycjParameterAssert.isAllNotBlank(ids, filename);
        TycjParameterAssert.isAllNotNull(file);
        Map<String, Object> courseInfo = this.myGeneralDao
                .getOneMapBySQL("select code, picture_url as url from ol_pe_class where id = ?", ids);
        if (MapUtils.isEmpty(courseInfo)) {
            throw new ParameterIllegalException();
        }
        String type = filename.substring(filename.lastIndexOf("."));
        String webPath = String.format(ResourceConstants.COURSE_IMAGE_URL, SiteUtil.getSiteCode(),
                courseInfo.get("code") + type);
        this.myGeneralDao.executeBySQL("update ol_pe_class set picture_url = ? where id = ?", webPath, ids);
        CommonUtils.replaceSameNameFile(file, (String) courseInfo.get("url"), webPath);
    }

    /**
     * 生成编号
     * @return
     */
    private String generateCode() {
        String code = CommonUtils.changeDateToString(new Date(), "yyyy");
        String hql = "select max(c.code) from OlPeClass c where length(c.code) = ? and c.code like '" + code + "%'" +
                " and c.siteCode = ?";
        String maxCode = this.myGeneralDao.getOneByHQL(hql, code.length() + 4, SiteUtil.getSiteCode());
        if (StringUtils.isNotBlank(maxCode)) {
            maxCode = maxCode.replace(code, "");
            maxCode = code + CommonUtils.leftAddZero(Integer.parseInt(maxCode) + 1, 4);
        } else {
            maxCode = code + CommonUtils.leftAddZero(1, 4);
        }
        return maxCode;
    }
}
