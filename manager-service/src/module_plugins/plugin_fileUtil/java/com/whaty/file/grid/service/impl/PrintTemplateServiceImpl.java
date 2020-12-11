package com.whaty.file.grid.service.impl;

import com.alibaba.fastjson.JSON;
import com.whaty.constant.CommonConstant;
import com.whaty.dao.GeneralDao;
import com.whaty.file.domain.bean.PePrintTemplate;
import com.whaty.file.grid.constant.PrintConstant;
import com.whaty.file.grid.service.PrintTemplateService;
import com.whaty.file.grid.service.TemplateDataHandler;
import com.whaty.file.grid.service.domain.TemplateSignVO;
import com.whaty.file.grid.service.domain.TemplateVO;
import com.whaty.file.grid.util.PrintUtils;
import com.whaty.file.template.strategy.AbstractTemplateStrategy;
import com.whaty.framework.aop.notice.annotation.LogAndNotice;
import com.whaty.framework.config.util.SiteUtil;
import com.whaty.framework.exception.ParameterIllegalException;
import com.whaty.framework.exception.ServiceException;
import com.whaty.framework.exception.UncheckException;
import com.whaty.function.Functions;
import com.whaty.util.CommonUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import static com.whaty.constant.CommonConstant.NO_SIGN_DATETIME_TO_SECOND_FORMAT;

/**
 * 打印模板服务类
 *
 * @author weipengsen
 */
@Lazy
@Service("printTemplateService")
public class PrintTemplateServiceImpl implements PrintTemplateService {

    /**
     * word文件打印每个文档单位限制
     */
    private static final int LIMIT_PAGE_SIZE = 300;

    @Resource(name = CommonConstant.GENERAL_DAO_BEAN_NAME)
    private GeneralDao myGeneralDao;

    @Override
    public void printAndDown(Map<String, String> args, HttpServletResponse response) {
        if (MapUtils.isEmpty(args)) {
            throw new ParameterIllegalException();
        }
        PePrintTemplate template = PrintUtils.getPrintTemplateByCode(args.get(PrintConstant.PARAM_PRINT_ID));
        if (StringUtils.isBlank(template.getDefaultPath())) {
            throw new ServiceException("未设置默认模板，无法进行打印");
        }
        List<Map<String, Object>> data = this.buildTemplateHandler(template).getTemplateData(args);
        try {
            int pageSize = template.getPrintPageSize() == null ? LIMIT_PAGE_SIZE : template.getPrintPageSize();
            if (data.size() > pageSize) {
                this.exportZipFile(template, data, response, pageSize);
            } else {
                this.exportSingleFile(template, data, response);
            }
            response.getOutputStream().flush();
            response.getOutputStream().close();
        } catch (ServiceException e) {
            throw e;
        } catch (Exception e) {
            throw new UncheckException(e);
        }
    }

    /**
     * 钩子方法
     * @param template
     * @return
     */
    protected TemplateDataHandler buildTemplateHandler(PePrintTemplate template) {
        return new TemplateDataHandler(template);
    }

    /**
     * 导出zip包文档
     *
     * @param template
     * @param data
     * @param response
     * @param pageSize
     */
    private void exportZipFile(PePrintTemplate template, List<Map<String, Object>> data,
                               HttpServletResponse response, int pageSize) throws Exception {
        CommonUtils.setContentToDownload(response, template.getTitle() + ".zip");
        try (ZipOutputStream out = new ZipOutputStream(response.getOutputStream())) {
            int wordSize = data.size() % pageSize == 0 ? data.size() / pageSize :
                    (data.size() / pageSize + 1);
            for (int i = 0; i < wordSize; i++) {
                AbstractTemplateStrategy helper = PrintUtils.getTemplateHelper(template);
                String filePath = CommonConstant.TEMP_GENERATE_FILE_PATH + template.getId() +
                        CommonUtils.changeDateToString(new Date(), NO_SIGN_DATETIME_TO_SECOND_FORMAT)
                        + i + helper.getTypeSuffix();
                //每个word存放的单位数量
                int singleWordCount = pageSize * i + pageSize > data.size() ? data.size() :
                        pageSize * i + pageSize;
                helper.handleTemplateFileByData(data.subList(pageSize * i, singleWordCount));
                helper.exportDocToFile(CommonUtils.getRealPath(filePath));

                File file = new File(CommonUtils.getRealPath(filePath));
                try (InputStream in = new FileInputStream(file)) {
                    ZipEntry zipEntry = new ZipEntry(file.getName());
                    out.putNextEntry(zipEntry);
                    int len;
                    byte[] buffer = new byte[1024];
                    while ((len = in.read(buffer)) != -1) {
                        out.write(buffer, 0, len);
                    }
                    out.closeEntry();
                } finally {
                    if (file.exists()) {
                        file.delete();
                    }
                }
            }
        }
    }

    /**
     * 导出单个文件，不打包
     *
     * @param template
     * @param data
     * @param response
     * @throws Exception
     */
    protected void exportSingleFile(PePrintTemplate template, List<Map<String, Object>> data,
                                    HttpServletResponse response) throws Exception {
        AbstractTemplateStrategy helper = PrintUtils.getTemplateHelper(template);
        CommonUtils.setContentToDownload(response,
                template.getTitle() + helper.getTypeSuffix());
        helper.handleTemplateFileByData(data);
        helper.exportDocToOutputStream(response.getOutputStream());
    }

    @Override
    @LogAndNotice("上传自定义模板")
    public void doUploadTemplate(File file, String printId) throws IOException {
        if (StringUtils.isBlank(printId) || file == null) {
            throw new ParameterIllegalException();
        }
        // 得到printTemplate对象
        PePrintTemplate template = PrintUtils.getPrintTemplateById(printId);
        // 检查模板文件是否符合要求
        PrintUtils.checkTemplateFile(template, file);
        File oldCustomFile = null;
        File tmpFile = null;
        if (StringUtils.isNotBlank(template.getPath())) {
            // 将原模板写为临时文件
            String tmpPath = template.getPath() + ".tmp";
            oldCustomFile = new File(CommonUtils.getRealPath(template.getPath()));
            if (oldCustomFile.exists()) {
                tmpFile = new File(CommonUtils.getRealPath(tmpPath));
                FileUtils.copyFile(oldCustomFile, tmpFile);
                // 删除原模板
                oldCustomFile.delete();
            }
        }
        File newFile = null;
        try {
            // 将文件写到模板路径下
            String path = template.getDefaultPath();
            if (StringUtils.isBlank(path)) {
                throw new ServiceException("系统默认模板不存在不能进行任何修改");
            }
            path = path.replace(".", "_" + SiteUtil.getSiteCode() + "_Custom.");
            path = CommonConstant.TEMP_FILE_PATH + path;
            newFile = new File(CommonUtils.getRealPath(path));
            FileUtils.copyFile(file, newFile);
            // 修改数据库
            String sql = "update pe_print_template set path = '" + path +
                    "',modify_date = now() where id = '" + printId + "'";
            this.myGeneralDao.executeBySQL(sql);
        } catch (Exception e) {
            if (newFile != null) {
                newFile.delete();
            }
            if (tmpFile != null) {
                FileUtils.copyFile(oldCustomFile, tmpFile);
            }
            throw e;
        }
    }

    @Override
    public Map<String, TemplateVO> listTemplate() {
        // 查询所有可自定义模板
        DetachedCriteria templateDc = DetachedCriteria.forClass(PePrintTemplate.class)
                .add(Restrictions.in("code", new String[]{PrintConstant.PRINT_TEMPLATE_TYPE_WORD
                        , PrintConstant.PRINT_TEMPLATE_TYPE_PDF, PrintConstant.PRINT_TEMPLATE_TYPE_EXCEL}))
                .add(Restrictions.eq("siteCode", SiteUtil.getSiteCode()));
        List<PePrintTemplate> templateList = this.myGeneralDao.getList(templateDc);
        if (CollectionUtils.isEmpty(templateList)) {
            throw new ServiceException("没有可编辑的模板");
        }
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT                                                  ");
        sql.append(" 	tg.FK_PRINT_TEMPLATE_ID AS templateId,               ");
        sql.append(" 	si.id AS id,                                         ");
        sql.append(" 	si.name AS name,                                     ");
        sql.append(" 	si.sign AS sign,                                     ");
        sql.append(" 	tg.name AS `group`                                   ");
        sql.append(" FROM                                                    ");
        sql.append(" 	pe_print_template_sign si                            ");
        sql.append(" INNER JOIN pe_print_template_group tg ON tg.id = si.FK_PRINT_TEMPLATE_GROUP_ID      ");
        sql.append(" INNER JOIN enum_const ac ON ac.id = si.FLAG_ACTIVE      ");
        sql.append(" WHERE                                                   ");
        sql.append(CommonUtils.madeSqlIn(templateList.stream()
                .map(PePrintTemplate::getId).collect(Collectors.toList()), "tg.FK_PRINT_TEMPLATE_ID"));
        sql.append(" AND ac. CODE = '1'                                      ");
        sql.append(" AND si. is_show = '1'                                   ");
        sql.append(" ORDER BY                                                ");
        sql.append(" 	tg.serial_number,                                    ");
        sql.append(" 	si.SERIAL_NUMBER                                     ");
        List<Map<String, Object>> signs = this.myGeneralDao.getMapBySQL(sql.toString());
        // 模板与占位符映射表
        Map<String, List<TemplateSignVO>> signMap = signs.stream()
                .map(e -> JSON.parseObject(JSON.toJSONString(e), TemplateSignVO.class))
                .collect(Collectors.groupingBy(TemplateSignVO::getTemplateId));
        // 占位符分组函数
        Function<List<TemplateSignVO>, Map<String, List<TemplateSignVO>>> groupFun = e -> e.stream()
                .collect(Collectors.groupingBy(TemplateSignVO::getGroup));
        // 模板数据转换
        return templateList.stream()
                .map(e -> JSON.parseObject(JSON.toJSONString(e), TemplateVO.class))
                .peek(e -> e.setPrintTemplateSigns(groupFun.apply(signMap.get(e.getId()))))
                .collect(Functions.map(TemplateVO::getId));
    }



}
