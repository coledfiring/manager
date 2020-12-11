package com.whaty.products.service.clazz.template;

import com.whaty.core.commons.datasource.MasterSlaveRoutingDataSource;
import com.whaty.framework.config.util.SiteUtil;
import com.whaty.framework.exception.ServiceException;
import com.whaty.framework.exception.UncheckException;
import com.whaty.products.service.common.template.AbstractZipReadTemplate;
import com.whaty.util.CommonUtils;
import com.whaty.util.ValidateUtils;
import com.whaty.utils.StaticBeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.util.StreamUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

/**
 * 教师图片上传模板类
 *
 * @author weipengsen
 */
public class StudentPictureZipUploadTemplate extends AbstractZipReadTemplate {

    private final static String UPLOAD_IMAGE_PATH = "/incoming/studentPicture/%s/%s/%s";

    public StudentPictureZipUploadTemplate(File zipFile, Map<String, Object> extraParams) {
        super(zipFile, extraParams);
    }

    @Override
    protected void handleSingleFile(String fileName, InputStream input, Map<String, Object> extraParams) {
        if (!fileName.toLowerCase().matches(ValidateUtils.IMAGE_NAME_PATTERN)) {
            throw new ServiceException("格式不正确，请选择（.bmp,.jpg,.gif,.png）格式;<br/>");
        }
        String classId = (String) extraParams.get("classId");
        String uploadType = (String) extraParams.get("uploadType");
        String filePath = String.format(UPLOAD_IMAGE_PATH, SiteUtil.getSiteCode(), classId, fileName);
        CommonUtils.mkDir(CommonUtils.getRealPath(filePath));
        String mobile = fileName.substring(0, fileName.indexOf("."));
        List<Object> oldPathList = StaticBeanUtils.getGeneralDao()
                .getBySQL("select picture_url from pe_student where " + uploadType + " = ? and site_code = ? " +
                        " and fk_class_id = ?", mobile, MasterSlaveRoutingDataSource.getDbType(), classId);
        if (CollectionUtils.isEmpty(oldPathList)) {
            throw new ServiceException("没有对应的学生信息");
        }
        String oldPath = (String) oldPathList.get(0);
        File oldFileBak = null;
        if (StringUtils.isNotBlank(oldPath)) {
            oldPath = CommonUtils.getRealPath(oldPath);
            File oldFile = new File(oldPath);
            oldFileBak = new File(oldPath + ".bak");
            oldFile.renameTo(oldFileBak);
            oldFile.delete();
        }
        File target = new File(CommonUtils.getRealPath(filePath));
        try (OutputStream out = new FileOutputStream(target)) {
            StreamUtils.copy(input, out);
            StaticBeanUtils.getGeneralDao().executeBySQL("update pe_student set picture_url = ? where mobile = ?" +
                    " AND site_code = ? and fk_class_id = ?", filePath, mobile,
                    MasterSlaveRoutingDataSource.getDbType(), classId);
            if (oldFileBak != null && oldFileBak.exists()) {
                oldFileBak.delete();
            }
        } catch (Exception e) {
            if (target.exists()) {
                target.delete();
            }
            if (oldFileBak != null) {
                oldFileBak.renameTo(new File(oldPath));
            }
            throw new UncheckException(e);
        }
    }

    @Override
    protected long getSingleFileLimitSize() {
        return 200 * 1024;
    }

    @Override
    protected String getSingleFileSizeOverMessage() {
        return "每个图片不能超过200Kb";
    }
}