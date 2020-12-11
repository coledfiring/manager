package com.whaty.file.grid.service.impl;

import com.whaty.constant.CommonConstant;
import com.whaty.core.commons.exception.EntityException;
import com.whaty.dao.GeneralDao;
import com.whaty.file.domain.bean.PePrintTemplate;
import com.whaty.file.grid.constant.PrintConstant;
import com.whaty.file.grid.util.PrintUtils;
import com.whaty.framework.exception.ParameterIllegalException;
import com.whaty.framework.grid.adapter.service.impl.TycjGridServiceAdapter;
import com.whaty.util.CommonUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * 超管打印模板管理服务类
 * @author weipengsen
 */
@Lazy
@Service("developPrintTemplateManageService")
public class DevelopPrintTemplateManageServiceImpl extends TycjGridServiceAdapter<PePrintTemplate> {

    @Resource(name = CommonConstant.GENERAL_DAO_BEAN_NAME)
    private GeneralDao myGeneralDao;

    @Override
    public void checkBeforeAdd(PePrintTemplate bean) throws EntityException {
        // 校验id不重复
        StringBuilder sql = new StringBuilder();
        sql.append("select 1 from pe_print_template where id = '" + bean.getId() + "'");
        List<Object> list = this.myGeneralDao.getBySQL(sql.toString());
        if (CollectionUtils.isNotEmpty(list)) {
            throw new EntityException("已存在的id");
        }
        this.checkBeforeAddOrUpdate(bean);
    }

    @Override
    public void checkBeforeUpdate(PePrintTemplate bean) throws EntityException {
        this.checkBeforeAddOrUpdate(bean);
    }

    /**
     * 检查bean是否符合要求
     * @param bean
     */
    private void checkBeforeAddOrUpdate(PePrintTemplate bean) throws EntityException {
        // 校验模板类型
        if (!PrintConstant.PRINT_TEMPLATE_TYPE_WORD.equals(bean.getCode())
                && !PrintConstant.PRINT_TEMPLATE_TYPE_EXCEL.equals(bean.getCode())
                && !PrintConstant.PRINT_TEMPLATE_TYPE_FREEMARKER_TO_PDF.equals(bean.getCode())
                && !PrintConstant.PRINT_TEMPLATE_TYPE_PDF.equals(bean.getCode())) {
            throw new EntityException("模板类型不正确");
        }
        // 校验查询类型
        if (!PrintConstant.SEARCH_TYPE_ONE_SQL.equals(bean.getSearchType())
                && !PrintConstant.SEARCH_TYPE_MORE_SQL.equals(bean.getSearchType())) {
            throw new EntityException("查询类型不正确");
        }
    }

    @Override
    public void checkBeforeDelete(List idList) throws EntityException {
        //删除模板时同时删除占位符组和占位符
        StringBuilder sql = new StringBuilder();
        sql.append(" DELETE si,                                                                        ");
        sql.append("  gr                                                                               ");
        sql.append(" FROM                                                                              ");
        sql.append(" 	pe_print_template prt                                                          ");
        sql.append(" INNER JOIN pe_print_template_group gr ON gr.fk_print_template_id = prt.id         ");
        sql.append(" INNER JOIN pe_print_template_sign si ON si.FK_PRINT_TEMPLATE_GROUP_ID = gr.id     ");
        sql.append(" WHERE                                                                             ");
        sql.append(CommonUtils.madeSqlIn(idList, "prt.id"));
        this.getGeneralDao().executeBySQL(sql.toString());
        //删除默认模板和当前模板
        List<Object[]> filePaths = this.myGeneralDao
                .getBySQL("select path, default_path from pe_print_template where "
                        + CommonUtils.madeSqlIn(idList, "id"));
        filePaths.forEach(e -> {
            String path = (String) e[0];
            String defaultPath = (String) e[1];
            if (StringUtils.isNotBlank(path)) {
                this.deleteWebPathFile(path);
            }
            if (StringUtils.isNotBlank(defaultPath)) {
                this.deleteWebPathFile(defaultPath);
            }
        });
    }

    /**
     * 删除web路径对应的文件
     * @param webPath
     */
    private void deleteWebPathFile(String webPath) {
        File file = new File(CommonUtils.getRealPath(webPath));
        file.delete();
    }

    /**
     * 更换默认模板
     * @param ids
     * @param upload
     * @param namespace
     */
    public void doUploadDefaultTemplate(String ids, File upload, String namespace) throws IOException {
        // 判断参数非空
        if (StringUtils.isBlank(ids)
                || StringUtils.isBlank(namespace)
                || !upload.exists()) {
            throw new ParameterIllegalException();
        }
        // 获取模板对象
        PePrintTemplate template = PrintUtils.getPrintTemplateById(ids);
        // 校验模板正确性
        PrintUtils.checkTemplateFile(template, upload);
        // 将模板文件放到指定命名空间
        String path = "/templatefile/" + namespace + "/" + template.getCode()
                + "/" + ids + "." + CommonUtils.getFileType(upload.getName());
        String realPath = CommonUtils.getRealPath(path);
        CommonUtils.mkDir(realPath);
        File file = new File(realPath);
        FileUtils.copyFile(upload, file);
        try {
            String sql = "update pe_print_template set default_path = '" + path + "' where id = '" + ids + "'";
            this.myGeneralDao.executeBySQL(sql);
        } catch (Exception e) {
            file.delete();
            throw e;
        }
    }
}
