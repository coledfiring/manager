package com.whaty.products.service.resource.template;

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
public class TeacherPictureZipUploadTemplate extends AbstractZipReadTemplate {

    private final static String UPLOAD_IMAGE_PATH = "/incoming/teacherPicture/%s/%s";

    public TeacherPictureZipUploadTemplate(File zipFile) {
        super(zipFile);
    }

    @Override
    protected void handleSingleFile(String fileName, InputStream input, Map<String, Object> extraParams) {
        if (!fileName.toLowerCase().matches(ValidateUtils.IMAGE_NAME_PATTERN)) {
            throw new ServiceException("格式不正确，请选择（.bmp,.jpg,.gif,.png）格式;<br/>");
        }
        String filePath = String.format(UPLOAD_IMAGE_PATH, SiteUtil.getSiteCode(), fileName);
        CommonUtils.mkDir(CommonUtils.getRealPath(filePath));
        String loginId = fileName.substring(0, fileName.indexOf("."));
        List<Object> oldPathList = StaticBeanUtils.getGeneralDao()
                .getBySQL("select picture_url from pe_teacher where login_id = ? and site_code = ?", loginId,
                        MasterSlaveRoutingDataSource.getDbType());
        if (CollectionUtils.isEmpty(oldPathList)) {
            throw new ServiceException("没有对应的教师用户名");
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
            StaticBeanUtils.getGeneralDao().executeBySQL("update pe_teacher set picture_url = ? where login_id = ?" +
                            " AND site_code = ? ", filePath, loginId, MasterSlaveRoutingDataSource.getDbType());
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
