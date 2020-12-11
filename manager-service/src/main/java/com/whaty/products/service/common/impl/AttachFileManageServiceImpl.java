package com.whaty.products.service.common.impl;

import com.whaty.constant.CommonConstant;
import com.whaty.core.commons.exception.EntityException;
import com.whaty.core.commons.util.Page;
import com.whaty.core.framework.bean.EnumConst;
import com.whaty.core.framework.grid.bean.GridConfig;
import com.whaty.core.framework.user.service.UserService;
import com.whaty.core.framework.util.BeanNames;
import com.whaty.dao.GeneralDao;
import com.whaty.HasAttachFile;
import com.whaty.domain.bean.AttachFile;
import com.whaty.domain.bean.SsoUser;
import com.whaty.framework.aop.notice.annotation.LogAndNotice;
import com.whaty.framework.asserts.TycjParameterAssert;
import com.whaty.framework.config.util.SiteUtil;
import com.whaty.framework.exception.ServiceException;
import com.whaty.framework.exception.UncheckException;
import com.whaty.framework.grid.builder.MenuBuilder;
import com.whaty.framework.grid.twolevellist.service.impl.AbstractTwoLevelListGridServiceImpl;
import com.whaty.framework.idocv.WhatyIdocvSdk;
import com.whaty.products.service.common.constant.ComConstant;
import com.whaty.util.CommonUtils;
import com.whaty.utils.StaticBeanUtils;
import com.whaty.utils.UserUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigInteger;
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static com.whaty.products.service.common.constant.ComConstant.ATTACH_FILE_PATH;

/**
 * 附件管理服务类
 *
 * @author weipengsen
 */
@Lazy
@Service("attachFileManageService")
public class AttachFileManageServiceImpl extends AbstractTwoLevelListGridServiceImpl<AttachFile> {

    @Resource(name = CommonConstant.GENERAL_DAO_BEAN_NAME)
    private GeneralDao myGeneralDao;

    @Resource(name = CommonConstant.OPEN_GENERAL_DAO_BEAN_NAME)
    private GeneralDao openGeneralDao;

    @Resource(name = BeanNames.USER_SERVICE)
    private UserService userService;

    private final WhatyIdocvSdk idocvSdk = new WhatyIdocvSdk();

    final static Set<String> PICTURE_TYPE_LIST = new TreeSet<>(Arrays.asList(".jpg", ".gif", ".bmp", ".png"));

    final static Set<String> WORLD_TYPE_LIST = new TreeSet<>(Arrays.asList(".doc", ".docx"));

    final static Set<String> EXCEL_TYPE_LIST = new TreeSet<>(Arrays.asList(".xls", ".xlsx"));

    final static Set<String> ZIP_TYPE_LIST = new TreeSet<>(Arrays.asList(".zip", ".rar"));

    final static Set<String> PPT_TYPE_LIST = new TreeSet<>(Arrays.asList(".ppt", ".pptx"));

    final static Set<String> PDF_TYPE_LIST = new TreeSet<>(Arrays.asList(".pdf", ".dbf"));

    final static Set<String> TEXT_TYPE_LIST = new TreeSet<>(Collections.singletonList(".txt"));

    private final static Logger logger = LoggerFactory.getLogger(AttachFileManageServiceImpl.class);

    @Override
    public void initGrid(GridConfig gridConfig, Map<String, Object> mapParam) {
        if (MapUtils.isNotEmpty(mapParam) && (mapParam.get("isShowBack") == null ||
                Boolean.valueOf(mapParam.get("isShowBack").toString()))) {
            gridConfig.getGridMenuList().add(0, MenuBuilder.buildBackMenu());
        }
        if (mapParam.containsKey("namespace")) {
            HasAttachFile.AttachFileBean bean = HasAttachFile.AttachFileBean
                    .getAttachConfigByNamespace((String) mapParam.get("namespace"));
            gridConfig.setTitle(bean.getListName() + gridConfig.getTitle());
        }
    }

    @Override
    protected Page afterList(Page page) {
        ((List<Map<String, Object>>) page.getItems()).stream()
                .filter(e -> StringUtils.isNotBlank((String) e.get("previewUrl")))
                .forEach(e -> e.computeIfPresent("previewUrl", (k, o) -> WhatyIdocvSdk.getReviewUrl((String) o)));
        Predicate<String> fileExists = e -> StringUtils.isNotBlank(e) && new File(CommonUtils.getRealPath(e)).exists();
        ((List<Map<String, Object>>) page.getItems())
                .forEach(e -> e.put("isExists", fileExists.test((String) e.get("url"))));
        return super.afterList(page);
    }

    @Override
    public void checkBeforeUpdate(AttachFile bean) throws EntityException {
        if (null == bean.getEnumConstByFlagAttachType()) {
            throw new EntityException("不是有效的下拉选项数据");
        }
        super.checkBeforeUpdate(bean);
    }

    /**
     * 上传附件
     *
     * @param linkId
     * @param namespace
     * @param fileName
     * @param upload
     * @throws IOException
     */
    @LogAndNotice("附件上传")
    public void doUploadAttachFile(String linkId, String namespace, String fileName, String flagScene, File upload) throws IOException {
        TycjParameterAssert.isAllNotBlank(linkId, namespace, fileName);
        TycjParameterAssert.fileAllExists(upload);
        HasAttachFile.AttachFileBean bean = HasAttachFile.AttachFileBean.getAttachConfigByNamespace(namespace);
        BigInteger count = this.myGeneralDao.getOneBySQL("select count(1) from attach_file " +
                "where link_id = ? and namespace = ?", linkId, namespace);
        if (count.intValue() >= bean.getLimit()) {
            throw new ServiceException("只能上传" + bean.getLimit() + "个文件");
        }
        if (this.myGeneralDao.checkNotEmpty("select 1 from attach_file where link_id = ? and namespace = ? " +
                "AND name = ?", linkId, namespace, fileName)) {
            throw new ServiceException(fileName + ": 已存在同名的附件");
        }
        String webPath = String.format(ATTACH_FILE_PATH, SiteUtil.getSiteCode(), namespace, linkId, fileName);
        String realPath = CommonUtils.getRealPath(webPath);
        File target = new File(realPath);
        String fileSize = CommonUtils.getFileSize(upload.length());
        AttachFile attachFile = this.buildAttachFile(linkId, namespace, fileName, webPath, fileSize, flagScene);
        this.myGeneralDao.save(attachFile);
        attachFile.setIdocvUuid(this.idocvSdk.upload(upload, fileName));
        this.myGeneralDao.save(attachFile);
        FileUtils.copyFile(upload, target);
    }

    private AttachFile buildAttachFile(String linkId, String namespace, String fileName, String url, String fileSize,
                                       String flagScene) {
        AttachFile attachFile = new AttachFile();
        attachFile.setLinkId(linkId);
        attachFile.setEnumConstByFlagAttachType(AttachType.findAttachType(fileName));
        attachFile.setCreateBy(this.myGeneralDao.getById(SsoUser.class, this.userService.getCurrentUser().getId()));
        attachFile.setUrl(url);
        attachFile.setNamespace(namespace);
        attachFile.setCreateTime(new Date());
        attachFile.setName(fileName);
        attachFile.setFileSize(fileSize);
        attachFile.setSiteCode(SiteUtil.getSiteCode());
        if(StringUtils.isNotBlank(flagScene)) {
            attachFile.setEnumConstByFlagScene(this.myGeneralDao
                    .getEnumConstByNamespaceCode("flagScene", flagScene));
        }
        return attachFile;
    }

    /**
     * 复制附件
     *
     * @param originLinkId
     * @param originNamespace
     * @param targetLinkId
     * @param targetNamespace
     * @param siteCode
     * @return
     */
    public void copyAttachFile(String originLinkId, String originNamespace, String targetLinkId,
                               String targetNamespace, String siteCode) {
        List<Map<String, Object>> originPath = this.myGeneralDao
                .getMapBySQL("select name as name, url as originUrl, flag_attach_type as attachType from attach_file " +
                                "where site_code = ? and namespace = ? and link_id = ?",
                        siteCode, originNamespace, originLinkId);
        originPath.forEach(e -> e.put("url", String
                .format(ATTACH_FILE_PATH, SiteUtil.getSiteCode(), targetNamespace, targetLinkId, e.get("name"))));
        this.myGeneralDao.batchExecuteSql(originPath.stream().map(e ->
                "INSERT INTO `attach_file`(`name`, `namespace`, `url`, `link_id`, `create_by`, `create_time`, " +
                        "`site_code`, `flag_attach_type`) VALUES ('" + e.get("name") + "', '" + targetNamespace +
                        "', '" + e.get("url") + "', '" + targetLinkId + "', '" + UserUtils.getCurrentUserId() +
                        "', now(), '" + siteCode + "', '" + e.get("attachType") + "')").collect(Collectors.toList()));
        List<File> savedFiles = new ArrayList<>();
        for (Map<String, Object> elem : originPath) {
            File file = new File(CommonUtils.mkDir(CommonUtils.getRealPath((String) elem.get("url"))));
            File originFile = new File(CommonUtils.getRealPath((String) elem.get("originUrl")));
            try {
                FileUtils.copyFile(originFile, file);
                savedFiles.add(file);
            } catch (IOException e) {
                if (e instanceof FileNotFoundException) {
                    logger.warn("copy attach file failure: " + e.getMessage());
                } else {
                    savedFiles.forEach(File::delete);
                    throw new UncheckException(e);
                }
            }
        }
    }

    /**
     * 下载附件
     *
     * @param ids
     * @param response
     * @return
     */
    public void downAttachFile(String ids, HttpServletResponse response) throws IOException {
        EnumConst downOperate = this.myGeneralDao
                .getEnumConstByNamespaceCode(ComConstant.ENUM_CONST_NAMESPACE_OPERATE_TYPE, "2");
        String sql = "insert into attach_file_operate_record(name, flag_file_operate_type, operate_time, " +
                "operate_user, namespace) SELECT NAME, '" + downOperate.getId() + "', now(), '" +
                this.userService.getCurrentUser().getId() + "', namespace FROM attach_file where id = ? ";
        this.myGeneralDao.executeBySQL(sql, ids);

        Map<String, Object> attachFile = this.myGeneralDao
                .getOneMapBySQL("select url, name from attach_file where id = ?", ids);
        String path = CommonUtils.getRealPath((String) attachFile.get("url"));
        File file = new File(path);
        if (!file.exists()) {
            throw new ServiceException("文件不存在");
        }
        CommonUtils.setContentToDownload(response, (String) attachFile.get("name"));
        FileUtils.copyFile(file, response.getOutputStream());
    }

    /**
     * 获取附件配置
     *
     * @param namespace
     * @return
     */
    public Map<String, Object> getAttachConfig(String namespace) {
        TycjParameterAssert.isAllNotBlank(namespace);
        HasAttachFile.AttachFileBean bean = HasAttachFile.AttachFileBean.getAttachConfigByNamespace(namespace);
        Map<String, Object> resultMap = new HashMap<>(2);
        resultMap.put("listName", bean.getListName());
        resultMap.put("limit", bean.getLimit());
        resultMap.put("isShowNav", false);
        return resultMap;
    }

    /**
     * 预览图片型附件
     * @param ids
     * @param response
     * @throws IOException
     */
    public void previewPicture(String ids, HttpServletResponse response) throws IOException {
        EnumConst downOperate = this.myGeneralDao
                .getEnumConstByNamespaceCode(ComConstant.ENUM_CONST_NAMESPACE_OPERATE_TYPE, "2");
        String sql = "insert into attach_file_operate_record(name, flag_file_operate_type, operate_time, " +
                "operate_user, namespace) SELECT NAME, '" + downOperate.getId() + "', now(), '" +
                this.userService.getCurrentUser().getId() + "', namespace FROM attach_file where id = ? ";
        this.myGeneralDao.executeBySQL(sql, ids);

        Map<String, Object> attachFile = this.myGeneralDao
                .getOneMapBySQL("select url, name from attach_file where id = ?", ids);
        String path = CommonUtils.getRealPath((String) attachFile.get("url"));
        File file = new File(path);
        if (!file.exists()) {
            file = new File(CommonUtils.getRealPath(CommonConstant.DEFAULT_ALBUM_PICTURE_PATH));
        }
        CommonUtils.setContentToDownload(response, (String) attachFile.get("name"));
        FileUtils.copyFile(file, response.getOutputStream());
    }

    /**
     * 附件类型枚举
     */
    private enum AttachType {

        // 图片
        PICTURE("1", e -> PICTURE_TYPE_LIST.stream().filter(e::contains).count() >= 1),

        // 文档
        WORLD("2", e -> WORLD_TYPE_LIST.stream().filter(e::contains).count() >= 1),

        // 表格
        EXCEL("3", e -> EXCEL_TYPE_LIST.stream().filter(e::contains).count() >= 1),

        // 压缩包
        ZIP("4", e -> ZIP_TYPE_LIST.stream().filter(e::contains).count() >= 1),

        // ppt
        PPT("5", e -> PPT_TYPE_LIST.stream().filter(e::contains).count() >= 1),

        // pdf
        PDF("6", e -> PDF_TYPE_LIST.stream().filter(e::contains).count() >= 1),

        // 文本
        TEXT("7", e -> TEXT_TYPE_LIST.stream().filter(e::contains).count() >= 1),

        OTHER("8", e -> false),;

        private String typeCode;

        private Function<String, Boolean> convertFunction;

        AttachType(String typeCode, Function<String, Boolean> convertFunction) {
            this.typeCode = typeCode;
            this.convertFunction = convertFunction;
        }

        /**
         * 根据文件名判断
         *
         * @param fileName
         * @return
         */
        static EnumConst findAttachType(String fileName) {
            String typeCode = Arrays.stream(values())
                    .filter(e -> e.getConvertFunction().apply(fileName.toLowerCase()))
                    .findFirst().orElse(OTHER).getTypeCode();
            return StaticBeanUtils.getGeneralDao()
                    .getEnumConstByNamespaceCode(ComConstant.ENUM_CONST_NAMESPACE_ATTACH_TYPE, typeCode);
        }

        public String getTypeCode() {
            return typeCode;
        }

        public Function<String, Boolean> getConvertFunction() {
            return convertFunction;
        }
    }

    @Override
    public void checkBeforeDelete(List idList) throws EntityException {
        EnumConst deleteOperate = this.myGeneralDao
                .getEnumConstByNamespaceCode(ComConstant.ENUM_CONST_NAMESPACE_OPERATE_TYPE, "1");
        String sql = "insert into attach_file_operate_record(name, flag_file_operate_type, operate_time, " +
                "operate_user, namespace) SELECT NAME, '" + deleteOperate.getId() + "', now(), '" +
                this.userService.getCurrentUser().getId() + "', namespace FROM attach_file where " +
                CommonUtils.madeSqlIn(idList, "id");
        this.myGeneralDao.executeBySQL(sql);
    }

    @Override
    protected void afterDelete(List idList) throws EntityException {
        List<String> fileUrls = this.openGeneralDao.getBySQL("select url from attach_file where " +
                CommonUtils.madeSqlIn(idList, "id"));
        List<File> files = fileUrls.stream().map(CommonUtils::getRealPath).map(File::new).filter(File::exists)
                .collect(Collectors.toList());
        List<File> backFiles = new ArrayList<>();
        try {
            for (File file : files) {
                File back = new File(file.getPath() + ".bak");
                backFiles.add(back);
                FileUtils.copyFile(file, back);
                file.delete();
            }
        } catch (IOException e) {
            for (File backFile : backFiles) {
                File oldFile = new File(backFile.getPath().substring(0, backFile.getPath().lastIndexOf(".")));
                try {
                    FileUtils.copyFile(backFile, oldFile);
                } catch (IOException e1) {
                    throw new UncheckException(e1);
                }
            }
            throw new UncheckException(e);
        }
        backFiles.forEach(File::delete);
    }

    public List<Map<String, Object>> getFlagScene() {
        return this.myGeneralDao.getMapBySQL("SELECT code, name FROM enum_const WHERE namespace = 'flagScene'");
    }

    @Override
    protected String getParentIdSearchParamName() {
        return "linkId";
    }

    @Override
    protected String getParentIdBeanPropertyName() {
        return "linkId";
    }
}
