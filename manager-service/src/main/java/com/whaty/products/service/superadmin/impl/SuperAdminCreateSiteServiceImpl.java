package com.whaty.products.service.superadmin.impl;

import com.whaty.constant.CommonConstant;
import com.whaty.constant.SiteConstant;
import com.whaty.core.commons.cache.service.CacheService;
import com.whaty.core.commons.datasource.MasterSlaveRoutingDataSource;
import com.whaty.core.commons.exception.EntityException;
import com.whaty.core.framework.api.exception.MessageException;
import com.whaty.core.framework.bean.CorePePriRole;
import com.whaty.core.framework.bean.EnumConst;
import com.whaty.core.framework.bean.PeWebSite;
import com.whaty.core.framework.bean.PeWebSiteDetail;
import com.whaty.core.framework.hibernate.dao.impl.ControlGeneralDao;
import com.whaty.core.framework.service.impl.SiteServiceImpl;
import com.whaty.core.framework.service.siteManager.ManagerControlDataOperateService;
import com.whaty.dao.GeneralDao;
import com.whaty.domain.bean.OperateGuideDescription;
import com.whaty.domain.bean.PeWebSiteConfig;
import com.whaty.domain.bean.message.SendMessageSite;
import com.whaty.file.excel.upload.exception.StreamOperateException;
import com.whaty.file.excel.upload.util.ExcelStreamUtils;
import com.whaty.framework.aop.notice.annotation.LogAndNotice;
import com.whaty.framework.common.spring.SpringUtil;
import com.whaty.framework.config.BasicConfigHelperManagement;
import com.whaty.framework.config.domain.ExamSystemConfig;
import com.whaty.framework.config.domain.IConfig;
import com.whaty.framework.config.domain.JdbcConfig;
import com.whaty.framework.config.domain.LearnSpaceConfig;
import com.whaty.framework.config.domain.PlatformConfig;
import com.whaty.framework.config.domain.WorkSpaceConfig;
import com.whaty.framework.config.helper.ExamSystemConfigHelper;
import com.whaty.framework.config.helper.JdbcConfigHelper;
import com.whaty.framework.config.helper.LearnSpaceConfigHelper;
import com.whaty.framework.config.helper.PlatformConfigHelper;
import com.whaty.framework.config.helper.SiteConfigHelper;
import com.whaty.framework.config.helper.WorkSpaceConfigHelper;
import com.whaty.framework.exception.ServiceException;
import com.whaty.framework.exception.UncheckException;
import com.whaty.products.service.superadmin.SuperAdminCreateSiteService;
import com.whaty.util.CommonUtils;
import com.whaty.util.ValidateUtils;
import net.sf.json.JSONObject;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.hibernate.Session;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.validation.ConstraintViolation;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * 超管端开设站点服务实现类
 *
 * @author weipengsen
 */
@Lazy
@Service("superAdminCreateSiteServiceImpl")
public class SuperAdminCreateSiteServiceImpl implements SuperAdminCreateSiteService {

    @Resource(name = CommonConstant.CONTROL_GENERAL_DAO_BEAN_NAME)
    private ControlGeneralDao controlGeneralDao;

    @Resource(name = CommonConstant.OPEN_GENERAL_DAO_BEAN_NAME)
    private GeneralDao openGeneralDao;

    @Resource(name = "managerControlDataOperateService")
    private ManagerControlDataOperateService managerControlDataOperateService;

    @Resource(name = "core_cacheService")
    private CacheService cacheService;

    @Resource(name = "siteConfigHelper")
    private SiteConfigHelper siteConfigHelper;

    private final static String[] CREATE_SITE_HEADERS = new String[]{"站点名称*", "站点编号*", "站点域名*",
            "数据源编号*", "appId*", "样例站点编号*", "数据源配置*", "课程空间配置*", "平台业务配置*",
            "工作室配置*", "考试系统配置", "ssoAppId*", "ssoAppSecret*", "ssoBasePath*"};
    /**
     * 解压目标文件夹
     */
    private final static String UNZIP_TARGET_DIRECTORY = "/incoming/upload/unzip/";
    /**
     * 管理端logo地址
     */
    private final static String SITE_MANAGE_LOGO_PATH = "/incoming/public/logo/%s/manage_logo.png";
    /**
     * 登录界面logo地址
     */
    private final static String SITE_LOGIN_LOGO_PATH = "/incoming/public/logo/%s/login_logo.png";
    /**
     * 工作室logo地址
     */
    private final static String SITE_WORKSPACE_LOGO_PATH = "/incoming/public/logo/%s/workspace_logo.png";

    @Override
    public void createSites(File upload) throws IOException {
        Map<String, Object> fileList = this.extractFiles(upload);
        List<Map<String, String>> sites = collectSites((File) fileList.get("excel"));
        try {
            sites.forEach(site -> {
                try {
                    this.createSite(site, (Map<String, File>) fileList.get(site.get("code")));
                } catch (IOException e) {
                    throw new UncheckException(e);
                }
            });
        } catch (Exception e) {
            sites.forEach(site -> this.deleteSite(site));
            throw e;
        }
        this.cacheService.remove(this.getSitesCacheKey());
    }

    private String getSitesCacheKey() {
        return this.getProjectPath() + "_" + "website_cache";
    }

    private String getProjectPath() {
        String projectPath;
        try {
            projectPath = SiteServiceImpl.class.getClassLoader().getResource("").toURI().getPath();
            projectPath = projectPath.substring(0, projectPath.indexOf("WEB-INF/"));
        } catch (URISyntaxException var3) {
            projectPath = "learn_space_api";
        }

        return projectPath + "_api";
    }

    @Override
    @LogAndNotice("上传站点logo")
    public void doUploadSiteLogo(File upload) throws IOException {
        Map<String, Object> siteLogos = this.extractFiles(upload);
        siteLogos.forEach((siteCode, logos) -> {
            try {
                this.saveSiteLogo(siteCode, (Map<String, File>) logos);
            } catch (IOException e) {
                throw new UncheckException(e);
            }
        });
    }

    @Override
    public void updateSiteConfig() {
        BasicConfigHelperManagement.ALL_HELPER.forEach(e -> e.updateRemoteVersion(System.currentTimeMillis()));
    }

    /**
     * 根据站点code和logo文件保存logo
     *
     * @param siteCode
     * @param logo
     */
    private void saveSiteLogo(String siteCode, Map<String, File> logo) throws IOException {
        List<PeWebSite> siteList = this.controlGeneralDao
                .getByHQL("from PeWebSite where code = '" + siteCode + "'");
        if (CollectionUtils.isNotEmpty(siteList)) {
            PeWebSite site = siteList.get(0);
            this.controlGeneralDao.executeBySQL("delete from pe_web_site_detail where id = '" + site.getId() + "'");
            this.saveSiteLogo(site, logo);
        }
    }

    /**
     * 创建站点
     *
     * @param siteInfo
     * @param logo
     */
    private void createSite(Map<String, String> siteInfo, Map<String, File> logo) throws IOException {
        PeWebSite site = this.saveSiteToDataBase(siteInfo);
        ((SiteConfigHelper) SpringUtil.getBean("siteConfigHelper")).updateRemoteVersion(System.currentTimeMillis());
        this.saveSiteConfig(site, siteInfo);
        this.copySiteMenu(siteInfo.get("templateCode"), site);
        this.handleBusinessDataBase(siteInfo);
        this.generateBasicRole(site);
        if (MapUtils.isNotEmpty(logo)) {
            this.saveSiteLogo(site, logo);
        }
        this.copyOperateGuide(siteInfo.get("templateCode"), site);
        this.copyMessageTemplate(siteInfo.get("templateCode"), site);
        this.copyDataBoard(siteInfo.get("templateCode"), site);
        this.copyPrintTemplate(siteInfo.get("templateCode"), site);
        this.copyEnumConst(siteInfo.get("templateCode"), site);
        this.copySmsTemplateConfig(siteInfo.get("templateCode"), site);
        this.copyBaseRole(site);
    }

    /**
     * 保存站点配置
     *
     * @param site
     * @param siteInfo
     */
    private void saveSiteConfig(PeWebSite site, Map<String, String> siteInfo) {

        ((SiteConfigHelper) SpringUtil.getBean("siteConfigHelper")).updateRemoteVersion(System.currentTimeMillis());

        String jdbcConfig = siteInfo.get("jdbcConfig");
        this.checkViolation(jdbcConfig, JdbcConfig.class);
        this.openGeneralDao.save(new PeWebSiteConfig(jdbcConfig, this.openGeneralDao
                .getEnumConstByNamespaceCode("flagConfigType", "1"), site));
        ((JdbcConfigHelper) SpringUtil.getBean("jdbcConfigHelper"))
                .updateRemoteVersion(System.currentTimeMillis());

        String learnSpaceConfig = siteInfo.get("learnSpaceConfig");
        this.checkViolation(learnSpaceConfig, LearnSpaceConfig.class);
        this.openGeneralDao.save(new PeWebSiteConfig(learnSpaceConfig, this.openGeneralDao
                .getEnumConstByNamespaceCode("flagConfigType", "2"), site));
        ((LearnSpaceConfigHelper) SpringUtil.getBean("learnSpaceConfigHelper"))
                .updateRemoteVersion(System.currentTimeMillis());

        String platformConfig = siteInfo.get("platformConfig");
        this.checkViolation(platformConfig, PlatformConfig.class);
        this.openGeneralDao.save(new PeWebSiteConfig(platformConfig, this.openGeneralDao
                .getEnumConstByNamespaceCode("flagConfigType", "3"), site));
        ((PlatformConfigHelper) SpringUtil.getBean("platformConfigHelper"))
                .updateRemoteVersion(System.currentTimeMillis());

        String workSpaceConfig = siteInfo.get("workSpaceConfig");
        this.checkViolation(workSpaceConfig, WorkSpaceConfig.class);
        this.openGeneralDao.save(new PeWebSiteConfig(workSpaceConfig, this.openGeneralDao
                .getEnumConstByNamespaceCode("flagConfigType", "6"), site));
        ((WorkSpaceConfigHelper) SpringUtil.getBean("workSpaceConfigHelper"))
                .updateRemoteVersion(System.currentTimeMillis());

        String examSystemConfig = siteInfo.get("examSystemConfig");
        if (StringUtils.isNotBlank(examSystemConfig)) {
            this.checkViolation(examSystemConfig, ExamSystemConfig.class);
            this.openGeneralDao.save(new PeWebSiteConfig(examSystemConfig, this.openGeneralDao
                    .getEnumConstByNamespaceCode("flagConfigType", "5"), site));
            ((ExamSystemConfigHelper) SpringUtil.getBean("examSystemConfigHelper"))
                    .updateRemoteVersion(System.currentTimeMillis());
        }
        try {
            TimeUnit.SECONDS.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 校验配置是否合法
     *
     * @param config
     * @param configClass
     */
    private void checkViolation(String config, Class<? extends IConfig> configClass) {
        Set<ConstraintViolation<IConfig>> violations = ValidateUtils.checkConstraintValidate(
                (IConfig) JSONObject.toBean(JSONObject.fromObject(config), configClass));
        if (CollectionUtils.isNotEmpty(violations)) {
            StringBuilder message = new StringBuilder("配置非法:");
            violations.stream().map(e -> String.format("%s %s;", e.getPropertyPath(), e.getMessage()))
                    .forEach(message::append);
            throw new ServiceException(message.toString());
        }
    }

    /**
     * 处理业务库
     * 1、工作室课程空间地址修改
     *
     * @param siteInfo
     */
    private void handleBusinessDataBase(Map<String, String> siteInfo) {
        MasterSlaveRoutingDataSource.setDbType(siteInfo.get("code"));
        this.controlGeneralDao.executeBySQL("update pe_site_config set site_code = '" + siteInfo.get("code") +
                "' where code = 'site_config'");
    }

    /**
     * 复制操作指导
     *
     * @param sourceSiteCode
     * @param site
     */
    private void copyOperateGuide(String sourceSiteCode, PeWebSite site) {
        MasterSlaveRoutingDataSource.setDbType(SiteConstant.SITE_CODE_CONTROL);
        List<OperateGuideDescription> descriptions = this.controlGeneralDao
                .getByHQL("from OperateGuideDescription where peWebSite.code = '" + sourceSiteCode + "'");
        descriptions.forEach(e -> e.setPeWebSite(site));
        Session session = this.controlGeneralDao.getMyHibernateTemplate().getSessionFactory().openSession();
        session.beginTransaction();
        try {
            descriptions.forEach(e -> session.save(e));
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
            throw e;
        } finally {
            session.close();
        }
    }

    /**
     * 复制消息模版
     *
     * @param sourceSiteCode
     * @param site
     */
    private void copyMessageTemplate(String sourceSiteCode, PeWebSite site) {
        MasterSlaveRoutingDataSource.setDbType(SiteConstant.SITE_CODE_CONTROL);
        List<SendMessageSite> descriptions = this.controlGeneralDao
                .getByHQL("from SendMessageSite where peWebSite.code = '" + sourceSiteCode + "'");
        descriptions.forEach(e -> e.setPeWebSite(site));
        Session session = this.controlGeneralDao.getMyHibernateTemplate().getSessionFactory().openSession();
        session.beginTransaction();
        try {
            descriptions.forEach(e -> session.save(e));
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
            throw e;
        } finally {
            session.close();
        }
    }

    /**
     * 复制数据看板
     *
     * @param sourceSiteCode
     * @param site
     */
    private void copyDataBoard(String sourceSiteCode, PeWebSite site) {
        MasterSlaveRoutingDataSource.setDbType(SiteConstant.SERVICE_SITE_CODE_BASE);
        StringBuilder sql = new StringBuilder();
        sql.append(" INSERT INTO pe_chart_def (                                   ");
        sql.append("  `ID`,                                                       ");
        sql.append("  `CHART`,                                                    ");
        sql.append("  `CODE`,                                                     ");
        sql.append("  `DATA_DIRECTION`,                                           ");
        sql.append("  `HAS_TIME_LINE`,                                            ");
        sql.append("  `HAS_DATA_ZOOM`,                                            ");
        sql.append("  `DATA_ZOOM_START`,                                          ");
        sql.append("  `DATA_ZOOM_END`,                                            ");
        sql.append("  `ZOOM_LOCK`,                                                ");
        sql.append("  `TYPE`,                                                     ");
        sql.append("  `DATA_INDEX_COLUMN`,                                        ");
        sql.append("  `VALUE_COLUMNS_STR`,                                        ");
        sql.append("  `INPUT_DATE`,                                               ");
        sql.append("  `chart_sql`,                                                ");
        sql.append("  `IS_DEL`,                                                   ");
        sql.append("  `is_cache`,                                                 ");
        sql.append("  `site_code`                                                 ");
        sql.append(" ) select                                                     ");
        sql.append("      concat(`ID`,'-','" + site.getCode() + "') as ID,        ");
        sql.append("      `CHART`,                                                ");
        sql.append("      `CODE`,                                                 ");
        sql.append("      `DATA_DIRECTION`,                                       ");
        sql.append("      `HAS_TIME_LINE`,                                        ");
        sql.append("      `HAS_DATA_ZOOM`,                                        ");
        sql.append("      `DATA_ZOOM_START`,                                      ");
        sql.append("      `DATA_ZOOM_END`,                                        ");
        sql.append("      `ZOOM_LOCK`,                                            ");
        sql.append("      `TYPE`,                                                 ");
        sql.append("      `DATA_INDEX_COLUMN`,                                    ");
        sql.append("      `VALUE_COLUMNS_STR`,                                    ");
        sql.append("      `INPUT_DATE`,                                           ");
        sql.append("      REPLACE(chart_sql,'\\'" + sourceSiteCode + "\\'',concat('\\'','"
                + site.getCode() + "','\\'')),    ");
        sql.append("      `IS_DEL`,                                               ");
        sql.append("      `is_cache`,                                             ");
        sql.append("      '" + site.getCode() + "'                                ");
        sql.append("   FROM                                                       ");
        sql.append("     pe_chart_def                                             ");
        sql.append("   WHERE                                                      ");
        sql.append("     site_code = '" + sourceSiteCode + "'                     ");
        this.controlGeneralDao.executeBySQL(sql.toString());
        sql.delete(0, sql.length());
        sql.append(" INSERT INTO pe_chart_column_def (                                             ");
        sql.append("  `ID`,                                                                        ");
        sql.append("  `FK_CHART_DEF_ID`,                                                           ");
        sql.append("  `COLUMN_INDEX`,                                                              ");
        sql.append("  `TYPE`,                                                                      ");
        sql.append("  `SERIES_NAME`,                                                               ");
        sql.append("  `GROUP_NAME`,                                                                ");
        sql.append("  `AXIS_INDEX`,                                                                ");
        sql.append("  `INPUT_DATE`,                                                                ");
        sql.append("  `COLUMN_NAME`                                                                ");
        sql.append(" ) select                                                                      ");
        sql.append("      concat(pcc.`ID`,'-','" + site.getCode() + "'),                           ");
        sql.append("      concat(pcc.`FK_CHART_DEF_ID`,'-','" + site.getCode() + "'),              ");
        sql.append("      pcc.`COLUMN_INDEX`,                                                      ");
        sql.append("      pcc.`TYPE`,                                                              ");
        sql.append("      pcc.`SERIES_NAME`,                                                       ");
        sql.append("      pcc.`GROUP_NAME`,                                                        ");
        sql.append("      pcc.`AXIS_INDEX`,                                                        ");
        sql.append("      pcc.`INPUT_DATE`,                                                        ");
        sql.append("      pcc.`COLUMN_NAME`                                                        ");
        sql.append("   FROM                                                                        ");
        sql.append("     pe_chart_column_def pcc                                                   ");
        sql.append("   inner join pe_chart_def pcd on pcd.id=pcc.FK_CHART_DEF_ID                   ");
        sql.append("   WHERE                                                                       ");
        sql.append("     pcd.site_code = '" + sourceSiteCode + "'                                  ");
        this.controlGeneralDao.executeBySQL(sql.toString());
        sql.delete(0, sql.length());
        sql.append(" INSERT INTO data_board (                                                    ");
        sql.append("  `id`,                                                                      ");
        sql.append("  `module_type`,                                                             ");
        sql.append("  `module_name`,                                                             ");
        sql.append("  `module_sql`,                                                              ");
        sql.append("  `module_data_type`,                                                        ");
        sql.append("  `module_data_name`,                                                        ");
        sql.append("  `module_data_sql`,                                                         ");
        sql.append("  `module_data_content`,                                                     ");
        sql.append("  `url`,                                                                     ");
        sql.append("  `pe_priority_action_Id`,                                                   ");
        sql.append("  `more_sql`,                                                                ");
        sql.append("  `site_code`,                                                               ");
        sql.append("  `module_data_config`,                                                      ");
        sql.append("  `code`,                                                                    ");
        sql.append("  `div_type`,                                                                ");
        sql.append("  `icon`,                                                                    ");
        sql.append("  `module_category_id`                                                       ");
        sql.append(" ) select                                                                    ");
        sql.append("      uuid() as ID,                                                          ");
        sql.append("     `module_type`,                                                          ");
        sql.append("     `module_name`,                                                          ");
        sql.append("     REPLACE(module_sql,'\\'" + sourceSiteCode + "\\'',concat('\\'','"
                + site.getCode() + "','\\'')),  ");
        sql.append("     `module_data_type`,                                                     ");
        sql.append("     `module_data_name`,                                                     ");
        sql.append("     REPLACE(module_data_sql,'\\'" + sourceSiteCode + "\\'',concat('\\'','"
                + site.getCode() + "','\\'')),  ");
        sql.append("     `module_data_content`,                                                  ");
        sql.append("     `url`,                                                                  ");
        sql.append("     `pe_priority_action_Id`,                                                ");
        sql.append("     `more_sql`,                                                             ");
        sql.append("      '" + site.getCode() + "',                                              ");
        sql.append("      `module_data_config`,                                                  ");
        sql.append("      `code`,                                                                ");
        sql.append("      `div_type`,                                                            ");
        sql.append("      `icon`,                                                                ");
        sql.append("      `module_category_id`                                                   ");
        sql.append("   FROM                                                                      ");
        sql.append("     data_board                                                              ");
        sql.append("   WHERE                                                                     ");
        sql.append("     site_code = '" + sourceSiteCode + "'                                    ");
        this.controlGeneralDao.executeBySQL(sql.toString());
        sql.delete(0, sql.length());
        sql.append(" INSERT INTO system_variables (                                              ");
        sql.append("  `id`,                                                                      ");
        sql.append("  `NAME`,                                                                    ");
        sql.append("  `VALUE`,                                                                   ");
        sql.append("  `PATTERN`,                                                                 ");
        sql.append("  `NOTE`,                                                                    ");
        sql.append("  `site_code`                                                                ");
        sql.append(" ) select                                                                    ");
        sql.append("      concat(`ID`,'-','" + site.getCode() + "') as ID,                       ");
        sql.append("      `NAME`,                                                                ");
        sql.append("      `VALUE`,                                                               ");
        sql.append("      `PATTERN`,                                                             ");
        sql.append("      `NOTE`,                                                                ");
        sql.append("      '" + site.getCode() + "'                                               ");
        sql.append("   FROM                                                                      ");
        sql.append("     system_variables                                                        ");
        sql.append("   WHERE                                                                     ");
        sql.append("     site_code = '" + sourceSiteCode + "'                                    ");
        sql.append("   and name='dataBoardChartCodeList'                                         ");
        this.controlGeneralDao.executeBySQL(sql.toString());
        MasterSlaveRoutingDataSource.setDbType(SiteConstant.SITE_CODE_CONTROL);
    }

    /**
     * 复制打印模版
     *
     * @param sourceSiteCode
     * @param site
     */
    private void copyPrintTemplate(String sourceSiteCode, PeWebSite site) {
        MasterSlaveRoutingDataSource.setDbType(SiteConstant.SERVICE_SITE_CODE_BASE);
        StringBuilder sql = new StringBuilder();
        sql.append(" INSERT INTO pe_print_template (                              ");
        sql.append("  `ID`,                                                       ");
        sql.append("  `TITLE`,                                                    ");
        sql.append("  `template_code`,                                            ");
        sql.append("  `CODE`,                                                     ");
        sql.append("  `PATH`,                                                     ");
        sql.append("  `THUMB`,                                                    ");
        sql.append("  `TEAM`,                                                     ");
        sql.append("  `NUM`,                                                      ");
        sql.append("  `VARIABLE`,                                                 ");
        sql.append("  `MODIFY_DATE`,                                              ");
        sql.append("  `DEFAULT_PATH`,                                             ");
        sql.append("  `SEARCH_SQL`,                                               ");
        sql.append("  `SEARCH_TYPE`,                                              ");
        sql.append("  `print_page_size`,                                          ");
        sql.append("  `site_code`                                                 ");
        sql.append(" ) select                                                     ");
        sql.append("      concat(`ID`,'-','" + site.getCode() + "') as ID,        ");
        sql.append("      `TITLE`,                                                ");
        sql.append("      `template_code`,                                        ");
        sql.append("      `CODE`,                                                 ");
        sql.append("      `PATH`,                                                 ");
        sql.append("      `THUMB`,                                                ");
        sql.append("      `TEAM`,                                                 ");
        sql.append("      `NUM`,                                                  ");
        sql.append("      `VARIABLE`,                                             ");
        sql.append("      `MODIFY_DATE`,                                          ");
        sql.append("      `DEFAULT_PATH`,                                         ");
        sql.append("      REPLACE(SEARCH_SQL,'\\'" + sourceSiteCode + "\\'',concat('\\'','"
                + site.getCode() + "','\\'')),                                           ");
        sql.append("      `SEARCH_TYPE`,                                          ");
        sql.append("      `print_page_size`,                                      ");
        sql.append("      '" + site.getCode() + "'                                ");
        sql.append("   FROM                                                       ");
        sql.append("     pe_print_template                                        ");
        sql.append("   WHERE                                                      ");
        sql.append("     site_code = '" + sourceSiteCode + "'                     ");
        this.controlGeneralDao.executeBySQL(sql.toString());
        sql.delete(0, sql.length());
        sql.append(" INSERT INTO pe_print_template_group (                                         ");
        sql.append("  `ID`,                                                                        ");
        sql.append("  `name`,                                                                      ");
        sql.append("  `fk_print_template_id`,                                                      ");
        sql.append("  `serial_number`                                                              ");
        sql.append(" ) select                                                                      ");
        sql.append("      concat(pcc.`ID`,'-','" + site.getCode() + "'),                           ");
        sql.append("      pcc.name,                                                                ");
        sql.append("      concat(pcc.`fk_print_template_id`,'-','" + site.getCode() + "'),         ");
        sql.append("      pcc.`serial_number`                                                      ");
        sql.append("   FROM                                                                        ");
        sql.append("     pe_print_template_group pcc                                               ");
        sql.append("   inner join pe_print_template pcd on pcd.id=pcc.fk_print_template_id         ");
        sql.append("   WHERE                                                                       ");
        sql.append("     pcd.site_code = '" + sourceSiteCode + "'                                  ");
        this.controlGeneralDao.executeBySQL(sql.toString());
        sql.delete(0, sql.length());
        sql.append(" INSERT INTO pe_print_template_sign (                                                      ");
        sql.append("  `ID`,                                                                                    ");
        sql.append("  `FK_PRINT_TEMPLATE_GROUP_ID`,                                                            ");
        sql.append("  `name`,                                                                                  ");
        sql.append("  `sign`,                                                                                  ");
        sql.append("  `SERIAL_NUMBER`,                                                                         ");
        sql.append("  `FLAG_ACTIVE`,                                                                           ");
        sql.append("  `is_show`                                                                                ");
        sql.append(" ) select                                                                                  ");
        sql.append("      concat(pcc.`ID`,'-','" + site.getCode() + "'),                                       ");
        sql.append("      concat(pcc.`FK_PRINT_TEMPLATE_GROUP_ID`,'-','" + site.getCode() + "'),               ");
        sql.append("      pcc.`name`,                                                                          ");
        sql.append("      pcc.`sign`,                                                                          ");
        sql.append("      pcc.`SERIAL_NUMBER`,                                                                 ");
        sql.append("      pcc.`FLAG_ACTIVE`,                                                                   ");
        sql.append("      pcc.`is_show`                                                                        ");
        sql.append("   FROM                                                                                    ");
        sql.append("     pe_print_template_sign pcc                                                            ");
        sql.append("   inner join pe_print_template_group pcg on pcg.id=pcc.FK_PRINT_TEMPLATE_GROUP_ID         ");
        sql.append("   inner join pe_print_template pcd on pcd.id=pcg.fk_print_template_id                     ");
        sql.append("   WHERE                                                                                   ");
        sql.append("     pcd.site_code = '" + sourceSiteCode + "'                                              ");
        this.controlGeneralDao.executeBySQL(sql.toString());
        MasterSlaveRoutingDataSource.setDbType(SiteConstant.SITE_CODE_CONTROL);
    }

    /**
     * 复制站点常量
     * @param sourceSiteCode
     * @param site
     */
    private void copyEnumConst(String sourceSiteCode, PeWebSite site) {
        MasterSlaveRoutingDataSource.setDbType(SiteConstant.SERVICE_SITE_CODE_BASE);
        StringBuilder sql = new StringBuilder();
        sql.append(" INSERT INTO enum_const (                                                      ");
        sql.append("  `ID`,                                                                        ");
        sql.append("  `name`,                                                                      ");
        sql.append("  `CODE`,                                                                      ");
        sql.append("  `NAMESPACE`,                                                                 ");
        sql.append("  `IS_DEFAULT`,                                                                ");
        sql.append("  `CREATE_DATE`,                                                               ");
        sql.append("  `NOTE`,                                                                      ");
        sql.append("  `TEAM`                                                                       ");
        sql.append(" ) select                                                                      ");
        sql.append("      REPLACE(uuid(), '-', ''),                                                ");
        sql.append("      `name`,                                                                  ");
        sql.append("      `CODE`,                                                                  ");
        sql.append("      `NAMESPACE`,                                                             ");
        sql.append("      `IS_DEFAULT`,                                                            ");
        sql.append("      now(),                                                                   ");
        sql.append("      `NOTE`,                                                                  ");
        sql.append("      '," + site.getCode() + ",'                                               ");
        sql.append("   FROM                                                                        ");
        sql.append("     enum_const                                                                ");
        sql.append("   WHERE                                                                       ");
        sql.append("     TEAM = '," + sourceSiteCode + ",'                                         ");
        this.controlGeneralDao.executeBySQL(sql.toString());
        MasterSlaveRoutingDataSource.setDbType(SiteConstant.SITE_CODE_CONTROL);
    }

    /**
     * 复制短信模版
     * @param sourceSiteCode
     * @param site
     */
    private void copySmsTemplateConfig(String sourceSiteCode, PeWebSite site) {
        MasterSlaveRoutingDataSource.setDbType(SiteConstant.SERVICE_SITE_CODE_BASE);
        StringBuilder sql = new StringBuilder();
        sql.append(" INSERT INTO sms_template_config (                                             ");
        sql.append("  `ID`,                                                                        ");
        sql.append("  `name`,                                                                      ");
        sql.append("  `CODE`,                                                                      ");
        sql.append("  `secret`,                                                                    ");
        sql.append("  `app_key`,                                                                   ");
        sql.append("  `domain`,                                                                    ");
        sql.append("  `free_sign_name`,                                                            ");
        sql.append("  `param_string`,                                                              ");
        sql.append("  `template_code`,                                                             ");
        sql.append("  `site_code`                                                                  ");
        sql.append(" ) select                                                                      ");
        sql.append("      REPLACE(uuid(), '-', ''),                                                ");
        sql.append("      `name`,                                                                  ");
        sql.append("      `CODE`,                                                                  ");
        sql.append("      `secret`,                                                                ");
        sql.append("      `app_key`,                                                               ");
        sql.append("      `domain`,                                                                ");
        sql.append("      `free_sign_name`,                                                        ");
        sql.append("      `param_string`,                                                          ");
        sql.append("      `template_code`,                                                         ");
        sql.append("      '" + site.getCode() + "'                                                 ");
        sql.append("   FROM                                                                        ");
        sql.append("     sms_template_config                                                       ");
        sql.append("   WHERE                                                                       ");
        sql.append("     site_code = '" + sourceSiteCode + "'                                      ");
        this.controlGeneralDao.executeBySQL(sql.toString());
        MasterSlaveRoutingDataSource.setDbType(SiteConstant.SITE_CODE_CONTROL);
    }

    /**
     * 复制基础角色
     * @param site
     */
    private void copyBaseRole(PeWebSite site) {
        MasterSlaveRoutingDataSource.setDbType(SiteConstant.SERVICE_SITE_CODE_BASE);
        StringBuilder sql = new StringBuilder();
        sql.append(" INSERT INTO pe_pri_role (                                                     ");
        sql.append("  `ID`,                                                                        ");
        sql.append("  `name`,                                                                      ");
        sql.append("  `CODE`,                                                                      ");
        sql.append("  `FLAG_ROLE_TYPE`,                                                            ");
        sql.append("  `FLAG_BAK`,                                                                  ");
        sql.append("  `flag_site_super_admin`,                                                     ");
        sql.append("  `site_code`                                                                  ");
        sql.append(" )                                                                             ");
        sql.append(" VALUES                                                                        ");
        sql.append("   (                                                                           ");
        sql.append("      REPLACE(uuid(), '-', ''),                                                ");
        sql.append("      '教师',                                                                   ");
        sql.append("      '1',                                                                     ");
        sql.append("      '402880951dad22bd011dad5d21fa0005',                                      ");
        sql.append("      NULL,                                                                    ");
        sql.append("      NULL,                                                                    ");
        sql.append("      '" + site.getCode() + "'                                                 ");
        sql.append("    ),                                                                         ");
        sql.append("   (                                                                           ");
        sql.append("      REPLACE(uuid(), '-', ''),                                                ");
        sql.append("      '学生',                                                                   ");
        sql.append("      '0',                                                                     ");
        sql.append("      '4028826a1db7bb01011db7e52d130001',                                      ");
        sql.append("      NULL,                                                                    ");
        sql.append("      NULL,                                                                    ");
        sql.append("      '" + site.getCode() + "'                                                 ");
        sql.append("    )                                                                          ");
        this.controlGeneralDao.executeBySQL(sql.toString());
        MasterSlaveRoutingDataSource.setDbType(SiteConstant.SITE_CODE_CONTROL);
    }

    /**
     * 删除站点
     *
     * @param siteInfo
     */
    private void deleteSite(Map<String, String> siteInfo) {
        if (siteInfo.containsKey("id")) {
            MasterSlaveRoutingDataSource.setDbType(SiteConstant.SITE_CODE_CONTROL);
            this.controlGeneralDao
                    .executeBySQL("delete from pe_pri_category where fk_web_site_id = '" + siteInfo.get("id") + "'");
            this.controlGeneralDao.executeBySQL("delete from pe_web_site_detail where id = '" + siteInfo.get("id") + "'");
            List<Object> roleIds = this.controlGeneralDao
                    .getBySQL("select id from pe_pri_role where fk_web_site_id = '" + siteInfo.get("id") + "'");
            this.controlGeneralDao
                    .executeBySQL("delete from pe_pri_role where fk_web_site_id = '" + siteInfo.get("id") + "'");
            this.controlGeneralDao
                    .executeBySQL("delete from operate_guide_description where fk_web_site_id = '"
                            + siteInfo.get("id") + "'");
            this.controlGeneralDao
                    .executeBySQL("delete from pe_web_site_config where fk_web_site_id = '"
                            + siteInfo.get("id") + "'");
            BasicConfigHelperManagement.ALL_HELPER
                    .forEach(e -> e.updateRemoteVersion(System.currentTimeMillis()));
            ((SiteConfigHelper) SpringUtil.getBean("siteConfigHelper")).updateRemoteVersion(System.currentTimeMillis());
            this.controlGeneralDao.executeBySQL("delete from pe_web_site where id = '" + siteInfo.get("id") + "'");
            MasterSlaveRoutingDataSource.setDbType(siteInfo.get("code"));
            this.controlGeneralDao.executeBySQL("delete from pe_pri_role where "
                    + CommonUtils.madeSqlIn(roleIds.toArray(new String[roleIds.size()]), "id"));
            this.controlGeneralDao.executeBySQL("delete from pe_print_template_group");
            this.controlGeneralDao.executeBySQL("delete from pe_print_template_group");
            this.controlGeneralDao.executeBySQL("delete from pe_print_template");
            File logo = new File(CommonUtils.getRealPath("/incoming/public/logo/" + siteInfo.get("code")));
            logo.delete();
        }
    }

    /**
     * 保存站点logo
     *
     * @param site
     * @param logo
     */
    private void saveSiteLogo(PeWebSite site, Map<String, File> logo) throws IOException {
        String logoPath = String.format(SITE_MANAGE_LOGO_PATH, site.getCode());
        MasterSlaveRoutingDataSource.setDbType(SiteConstant.SITE_CODE_CONTROL);
        Session session = this.controlGeneralDao.getMyHibernateTemplate().getSessionFactory().openSession();
        session.beginTransaction();
        try {
            String logoRealPath = CommonUtils.getRealPath(logoPath);
            CommonUtils.mkDir(logoRealPath);
            if (logo.containsKey("manage_logo")) {
                session.createSQLQuery("insert into pe_web_site_detail (id, pc_logo) values ('"
                        + site.getId() + "', '" + logoPath + "')").executeUpdate();
                CommonUtils.copyFile(new FileInputStream(logo.get("manage_logo")), logoRealPath);
            }
            if (logo.containsKey("login_logo")) {
                CommonUtils.copyFile(new FileInputStream(logo.get("login_logo")),
                        CommonUtils.getRealPath(String.format(SITE_LOGIN_LOGO_PATH, site.getCode())));
            }
            if (logo.containsKey("workspace_logo")) {
                CommonUtils.copyFile(new FileInputStream(logo.get("workspace_logo")),
                        CommonUtils.getRealPath(String.format(SITE_WORKSPACE_LOGO_PATH, site.getCode())));
            }
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
            throw new UncheckException(e);
        } finally {
            session.close();
        }
    }


    /**
     * 生成基础角色
     *
     * @param site
     */
    private void generateBasicRole(PeWebSite site) {
        List<CorePePriRole> roles = new LinkedList<>();
        CorePePriRole superManager = new CorePePriRole();
        superManager.setName("站点超级管理员");
        roles.add(superManager);
        CorePePriRole manager = new CorePePriRole();
        manager.setName("总站管理员");
        roles.add(manager);
        CorePePriRole siteManager = new CorePePriRole();
        siteManager.setName("分站管理员");
        roles.add(siteManager);
        roles.forEach(e -> this.addRole(e, site.getCode()));
        MasterSlaveRoutingDataSource.setDbType(SiteConstant.SITE_CODE_CONTROL);
        // 站点管理员角色类型更改
        String superManageType = (String) this.controlGeneralDao
                .getBySQL("select id from enum_const where namespace = 'FlagRoleType' and code = '9998'").get(0);
        this.controlGeneralDao.executeBySQL("update pe_pri_role set flag_role_type = '" + superManageType
                + "' where fk_web_site_id = '" + site.getId() + "' and name = '站点超级管理员'");

        MasterSlaveRoutingDataSource.setDbType(site.getCode());
        superManageType = (String) this.controlGeneralDao
                .getBySQL("select id from enum_const where namespace = 'FlagRoleType' and code = '9998'").get(0);
        this.controlGeneralDao.executeBySQL("update pe_pri_role set flag_role_type = '" + superManageType
                + "' where name = '站点超级管理员'");
    }

    /**
     * 添加角色
     *
     * @param bean
     * @param siteCode
     */
    private void addRole(CorePePriRole bean, String siteCode) {
        CorePePriRole corePePriRole = new CorePePriRole();
        corePePriRole.setName(bean.getName());
        //保存角色到管理平台
        MasterSlaveRoutingDataSource.setDbType(SiteConstant.SITE_CODE_CONTROL);
        corePePriRole = this.managerControlDataOperateService.savePePriRole(corePePriRole, siteCode);
        try {
            MasterSlaveRoutingDataSource.setDbType(siteCode);
            EnumConst roleType = (EnumConst) this.controlGeneralDao
                    .getByHQL("from EnumConst where namespace = 'FlagRoleType' and code = '3'").get(0);
            EnumConst flagSiteSuperAdmin = (EnumConst) this.controlGeneralDao
                    .getByHQL("from EnumConst where namespace = 'FlagSiteSuperAdmin' and code = '0'").get(0);
            bean.setId(corePePriRole.getId());
            bean.setEnumConstByFlagRoleType(roleType);
            bean.setCode(corePePriRole.getCode());
            StringBuilder sql = new StringBuilder();
            sql.append("  insert into pe_pri_role (             ");
            sql.append("    id,                                 ");
            sql.append("    name,                               ");
            sql.append("    code,                               ");
            sql.append("    flag_role_type,                     ");
            sql.append("    flag_site_super_admin,              ");
            sql.append("    site_code                           ");
            sql.append("  )                                     ");
            sql.append("  VALUES                                ");
            sql.append("  (                                     ");
            sql.append("    '" + corePePriRole.getId() + "',    ");
            sql.append("    '" + corePePriRole.getName() + "',  ");
            sql.append("    '" + corePePriRole.getCode() + "',  ");
            sql.append("    '" + roleType.getId() + "',         ");
            sql.append("    '" + flagSiteSuperAdmin.getId() + "',");
            sql.append("    '" + siteCode + "'                  ");
            sql.append("  )                                     ");
            this.controlGeneralDao.executeBySQL(sql.toString());
        } catch (Exception e) {
            //保存角色到业务库失败，从管理平台删除该角色
            try {
                MasterSlaveRoutingDataSource.setDbType(SiteConstant.SITE_CODE_CONTROL);
                this.managerControlDataOperateService.deletePePriRoleByIds(Arrays.asList(corePePriRole.getId()));
            } catch (EntityException e1) {
                throw new ServiceException(e1.getMessage());
            }
            throw new UncheckException(e);
        }
    }

    /**
     * 将站点保存进数据库N
     *
     * @param siteInfo
     */
    private PeWebSite saveSiteToDataBase(Map<String, String> siteInfo) {
        MasterSlaveRoutingDataSource.setDbType(SiteConstant.SITE_CODE_CONTROL);
        List<Object> siteExists = this.controlGeneralDao.getBySQL("select 1 from pe_web_site where code = '"
                + siteInfo.get("code") + "'");
        if (CollectionUtils.isNotEmpty(siteExists)) {
            throw new ServiceException("已存在此编号的站点");
        }
        PeWebSite site = new PeWebSite();
        site.setName(siteInfo.get("name"));
        site.setDomain(siteInfo.get("domain"));
        site.setCode(siteInfo.get("code"));
        site.setActiveStatus(1);
        site.setDatasourceCode(siteInfo.get("code"));
        site.setAppId(siteInfo.get("appId"));
        site.setCreateTime(new Date());
        site.setSsoAppId(siteInfo.get("ssoAppId"));
        site.setSsoAppSecret(siteInfo.get("ssoAppSecret"));
        site.setSsoBasePath(siteInfo.get("ssoBasePath"));

        PeWebSiteDetail detail = new PeWebSiteDetail();
        detail.setTitle(site.getName());
        detail.setPeWebSite(site);
        detail.setId(site.getId());

        Session session = this.controlGeneralDao.getMyHibernateTemplate().getSessionFactory().openSession();
        session.beginTransaction();
        try {
            session.saveOrUpdate(site);
            session.saveOrUpdate(detail);
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
            throw new UncheckException(e);
        } finally {
            session.close();
        }
        siteInfo.put("id", site.getId());
        return site;
    }

    /**
     * 提取文件
     *
     * @param upload
     * @return
     * @throws IOException
     */
    private Map<String, Object> extractFiles(File upload) throws IOException {
        Map<String, Object> files = new HashMap<>(16);
        try (ZipInputStream input = new ZipInputStream(new FileInputStream(upload))) {
            ZipEntry entry;
            String uuid = UUID.randomUUID().toString().replace("-", "");
            while ((entry = input.getNextEntry()) != null) {
                if (entry.isDirectory()) {
                    continue;
                }
                String path = CommonUtils.getRealPath(UNZIP_TARGET_DIRECTORY + uuid + "/" + entry.getName());
                CommonUtils.copyFile(input, path);
                if (entry.getName().contains("/")) {
                    if (!entry.getName().contains(".png")) {
                        throw new ServiceException("logo图片格式为png");
                    }
                    String directory = entry.getName().substring(0, entry.getName().lastIndexOf("/"));
                    files.putIfAbsent(directory, new HashMap<>(4));
                    ((Map) files.get(directory))
                            .put(entry.getName().substring(entry.getName().lastIndexOf("/") + 1,
                                    entry.getName().lastIndexOf(".")), new File(path));
                } else {
                    files.put("excel", new File(path));
                }
            }
        }
        return files;
    }

    /**
     * 收集站点信息
     *
     * @param excelFile
     * @return
     */
    private List<Map<String, String>> collectSites(File excelFile) throws StreamOperateException {
        Workbook book = null;
        List<Map<String, String>> sites = new LinkedList<>();
        try {
            book = ExcelStreamUtils.loadFile(excelFile);
            ExcelStreamUtils.checkExcelFile(book, CREATE_SITE_HEADERS, 0);
            int rowNum = book.getSheetAt(0).getLastRowNum();
            for (int i = 1; i <= rowNum; i++) {
                Map<String, String> site = new LinkedHashMap<>();
                Row row = book.getSheetAt(0).getRow(i);
                site.put("name", row.getCell(0).getStringCellValue());
                site.put("code", row.getCell(1).getStringCellValue());
                site.put("domain", row.getCell(2).getStringCellValue());
                site.put("datasource", row.getCell(3).getStringCellValue());
                site.put("appId", row.getCell(4).getStringCellValue());
                site.put("templateCode", row.getCell(5).getStringCellValue());
                site.put("jdbcConfig", row.getCell(6).getStringCellValue());
                site.put("learnSpaceConfig", row.getCell(7).getStringCellValue());
                site.put("platformConfig", row.getCell(8).getStringCellValue());
                site.put("workSpaceConfig", row.getCell(9).getStringCellValue());
                site.put("examSystemConfig", row.getCell(10).getStringCellValue());
                site.put("ssoAppId", row.getCell(11).getStringCellValue());
                site.put("ssoAppSecret", row.getCell(12).getStringCellValue());
                site.put("ssoBasePath", row.getCell(13).getStringCellValue());
                sites.add(site);
            }
        } finally {
            ExcelStreamUtils.closeBook(book);
        }
        return sites;
    }

    /**
     * 复制站点菜单
     *
     * @param sourceSiteCode
     * @param destSite
     * @throws MessageException
     */
    private void copySiteMenu(String sourceSiteCode, PeWebSite destSite) {
        MasterSlaveRoutingDataSource.setDbType(SiteConstant.SITE_CODE_CONTROL);
        List<Object> sourceSiteIdList = this.controlGeneralDao
                .getBySQL("select id from pe_Web_site where code = '" + sourceSiteCode + "'");
        if (CollectionUtils.isEmpty(sourceSiteIdList)) {
            throw new ServiceException("样例站点不存在");
        }
        String sourceSiteId = (String) sourceSiteIdList.get(0);
        List destSiteMenus = this.controlGeneralDao
                .getBySQL("select 1 from pe_pri_category where fk_web_site_id = '" + destSite.getId() + "'");
        if (CollectionUtils.isNotEmpty(destSiteMenus)) {
            throw new ServiceException("目标站点已经存在菜单");
        }
        StringBuilder sql = new StringBuilder();
        sql.append(" INSERT INTO pe_pri_category (                   ");
        sql.append("    ID,                                          ");
        sql.append("    NAME,                                        ");
        sql.append("    FK_PARENT_ID,                                ");
        sql.append("    CODE,                                        ");
        sql.append("    PATH,                                        ");
        sql.append("    FLAG_LEFT_MENU,                              ");
        sql.append("    serial_Number,                               ");
        sql.append("    isActive,                                    ");
        sql.append("    LEVEL,                                       ");
        sql.append("    fk_grid_id,                                  ");
        sql.append("    fk_web_site_id,                              ");
        sql.append("    icon,                                        ");
        sql.append("    fk_base_category_id,                         ");
        sql.append("    base_id,                                     ");
        sql.append("    show_in_left_menu                            ");
        sql.append(" )                                               ");
        sql.append(" SELECT                                          ");
        sql.append("    CONCAT(id, '" + destSite.getCode() + "'),    ");
        sql.append("    NAME,                                        ");
        sql.append("    IF(FK_PARENT_ID IS NULL, NULL, CONCAT(FK_PARENT_ID, '"
                + destSite.getCode() + "')), ");
        sql.append("    CODE,                                        ");
        sql.append("    PATH,                                        ");
        sql.append("    FLAG_LEFT_MENU,                              ");
        sql.append("    serial_Number,                               ");
        sql.append("    isActive,                                    ");
        sql.append("    LEVEL,                                       ");
        sql.append("    fk_grid_id,                                  ");
        sql.append("    '" + destSite.getId() + "',                  ");
        sql.append("    icon,                                        ");
        sql.append("    fk_base_category_id,                         ");
        sql.append("    base_id,                                     ");
        sql.append("    show_in_left_menu                            ");
        sql.append(" FROM pe_pri_category                            ");
        sql.append(" WHERE fk_web_site_id = '" + sourceSiteId + "'   ");
        this.controlGeneralDao.executeBySQL(sql.toString());
    }

}
