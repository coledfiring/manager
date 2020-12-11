package com.whaty.file.grid.util;

import com.whaty.constant.CommonConstant;
import com.whaty.file.domain.bean.PePrintTemplate;
import com.whaty.file.domain.bean.PePrintTemplateSign;
import com.whaty.framework.config.util.SiteUtil;
import com.whaty.framework.exception.ServiceException;
import com.whaty.file.grid.constant.PrintConstant;
import com.whaty.file.template.factory.TemplateStrategySimpleFactory;
import com.whaty.file.template.strategy.AbstractTemplateStrategy;
import com.whaty.util.CommonUtils;
import com.whaty.utils.StaticBeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Set;

/**
 * 打印模块工具类
 * @author weipengsen
 */
public class PrintUtils {

    /**
     * 检查模板文件是否符合要求
     * @param template
     * @param file
     */
    public static void checkTemplateFile(PePrintTemplate template, File file) throws IOException {
        Set<String> searchSign = getTemplateHelper(template, file).collectSignInTemplate();
        List<PePrintTemplateSign> signs = template.getPePrintTemplateSignList();
        signs.forEach(e -> {
            String signStr = e.getSign().replace(PrintConstant.SEARCH_SQL_ARG_SIGN_PREFIX, "")
                    .replace(PrintConstant.SEARCH_SQL_ARG_SIGN_SUFFIX, "");
            searchSign.remove(signStr);
        });
        if (CollectionUtils.isNotEmpty(searchSign)) {
            throw new ServiceException("文件中存在不在可选占位符范围的${}形式文本:" +
                    CommonUtils.join(searchSign, CommonConstant.SPLIT_ID_SIGN, ""));
        }
    }

    /**
     * 获取打印辅助类
     *
     * @param template
     * @return
     * @throws IOException
     * @throws ServiceException
     */
    public static AbstractTemplateStrategy getTemplateHelper(PePrintTemplate template) throws IOException, ServiceException {
        String filePath;
        if (StringUtils.isNotBlank(template.getPath())
                && new File(CommonUtils.getRealPath(template.getPath())).exists()
                && !PrintConstant.PRINT_TEMPLATE_TYPE_FREEMARKER_TO_PDF.equals(template.getCode())) {
            filePath = template.getPath();
        } else {
            filePath = template.getDefaultPath();
        }
        filePath = CommonUtils.getRealPath(filePath);
        return getTemplateHelper(template, filePath);
    }

    /**
     * 获取打印辅助类
     *
     * @param template
     * @param filePath
     * @return
     * @throws IOException
     */
    public static AbstractTemplateStrategy getTemplateHelper(PePrintTemplate template, String filePath) throws IOException {
        return TemplateStrategySimpleFactory.newInstance(template.getCode(), filePath);
    }

    /**
     * 获取打印辅助类
     *
     * @param template
     * @param file
     * @return
     * @throws IOException
     */
    public static AbstractTemplateStrategy getTemplateHelper(PePrintTemplate template, File file) throws IOException {
        return TemplateStrategySimpleFactory.newInstance(template.getCode(), file);
    }

    /**
     * 获得print对象
     *
     * @param printCode
     * @return
     */
    public static PePrintTemplate getPrintTemplateByCode(String printCode) {
        PePrintTemplate template = (PePrintTemplate) StaticBeanUtils.getGeneralDao()
                .getByHQL(" from PePrintTemplate where templateCode=? and siteCode=?", printCode, SiteUtil.getSiteCode()).get(0);
        DetachedCriteria dc = DetachedCriteria.forClass(PePrintTemplateSign.class)
                .createAlias("pePrintTemplateGroup", "pePrintTemplateGroup")
                .add(Restrictions.eq("pePrintTemplateGroup.pePrintTemplate.id", template.getId()))
                .addOrder(Order.asc("serialNumber"))
                .createCriteria("enumConstByFlagActive")
                .add(Restrictions.eq("code", "1"));
        template.setPePrintTemplateSignList(StaticBeanUtils.getGeneralDao().getList(dc));
        StaticBeanUtils.getGeneralDao().flush();
        return template;
    }

    /**
     * 获得print对象
     *
     * @param printId
     * @return
     */
    public static PePrintTemplate getPrintTemplateById(String printId) {
        PePrintTemplate template = StaticBeanUtils.getGeneralDao().getById(PePrintTemplate.class, printId);
        DetachedCriteria dc = DetachedCriteria.forClass(PePrintTemplateSign.class)
                .createAlias("pePrintTemplateGroup", "pePrintTemplateGroup")
                .add(Restrictions.eq("pePrintTemplateGroup.pePrintTemplate.id", printId))
                .addOrder(Order.asc("serialNumber"))
                .createCriteria("enumConstByFlagActive")
                .add(Restrictions.eq("code", "1"));
        template.setPePrintTemplateSignList(StaticBeanUtils.getGeneralDao().getList(dc));
        StaticBeanUtils.getGeneralDao().flush();
        return template;
    }

}
