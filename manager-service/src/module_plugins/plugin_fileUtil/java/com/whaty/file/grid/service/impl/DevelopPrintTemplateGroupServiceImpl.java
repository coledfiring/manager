package com.whaty.file.grid.service.impl;

import com.whaty.constant.CommonConstant;
import com.whaty.core.commons.exception.EntityException;
import com.whaty.dao.GeneralDao;
import com.whaty.file.domain.bean.PePrintTemplateGroup;
import com.whaty.framework.grid.adapter.service.impl.TycjGridServiceAdapter;
import com.whaty.util.CommonUtils;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 超管打印模板分组管理服务类
 * @author weipengsen
 */
@Lazy
@Service("developPrintTemplateGroupService")
public class DevelopPrintTemplateGroupServiceImpl extends TycjGridServiceAdapter<PePrintTemplateGroup> {

    @Resource(name = CommonConstant.GENERAL_DAO_BEAN_NAME)
    private GeneralDao myGeneralDao;

    @Override
    public void checkBeforeAdd(PePrintTemplateGroup bean) throws EntityException {
        this.checkBeforeAddOrUpdate(bean);
    }

    @Override
    public void checkBeforeUpdate(PePrintTemplateGroup bean) throws EntityException {
        this.checkBeforeAddOrUpdate(bean);
    }

    /**
     * 检查bean是否符合要求
     * @param bean
     */
    private void checkBeforeAddOrUpdate(PePrintTemplateGroup bean) throws EntityException {
        // 模板id是否存在
        List<Object> list = this.myGeneralDao.getBySQL("select 1 from pe_print_template where id = '"
                + bean.getPePrintTemplate().getId() + "'");
        if (CollectionUtils.isEmpty(list)) {
            throw new EntityException("模板不存在");
        }
        // 名称重复
        String sql = "select 1 from pe_print_template_group where fk_print_template_id = '"
                + bean.getPePrintTemplate().getId() + "' and name = '" + bean.getName() + "'";
        if (bean.getId() != null) {
            sql += " and id <> '" + bean.getId() + "'";
        }
        list = this.myGeneralDao.getBySQL(sql);
        if (CollectionUtils.isNotEmpty(list)) {
            throw new EntityException("同一模板中不能存在同样名称的组");
        }
    }

    @Override
    public void checkBeforeDelete(List idList) throws EntityException {
        StringBuilder sql = new StringBuilder();
        sql.append(" DELETE gr,                                                                       ");
        sql.append("  si                                                                              ");
        sql.append(" FROM                                                                             ");
        sql.append(" 	pe_print_template_group gr                                                    ");
        sql.append(" INNER JOIN pe_print_template_sign si ON si.fk_print_template_group_id = gr.id    ");
        sql.append(" WHERE                                                                            ");
        sql.append(CommonUtils.madeSqlIn(idList, "gr.id"));
        this.getGeneralDao().executeBySQL(sql.toString());
    }
}
