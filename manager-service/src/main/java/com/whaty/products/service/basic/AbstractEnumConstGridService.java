package com.whaty.products.service.basic;

import com.whaty.constant.CommonConstant;
import com.whaty.core.commons.exception.EntityException;
import com.whaty.core.framework.bean.EnumConst;
import com.whaty.dao.GeneralDao;
import com.whaty.framework.config.util.SiteUtil;
import com.whaty.framework.grid.adapter.service.impl.TycjGridServiceAdapter;

import javax.annotation.Resource;
import java.math.BigInteger;
import java.util.List;

/**
 * 数据字典grid服务抽象类
 *
 * @author weipengsen
 */
public abstract class AbstractEnumConstGridService<T extends EnumConst> extends TycjGridServiceAdapter<T> {

    @Resource(name = CommonConstant.GENERAL_DAO_BEAN_NAME)
    protected GeneralDao myGeneralDao;

    private final static String ENUM_CONST_TEAM_SING = ",%s,";

    @Override
    public void checkBeforeAdd(EnumConst bean) throws EntityException {
        // 添加siteCode
        bean.setTeam(String.format(ENUM_CONST_TEAM_SING, SiteUtil.getSiteCode()));
        bean.setNamespace(this.getNamespace());
        // 检查name唯一
        if (this.myGeneralDao.checkNotEmpty("select 1 from enum_const where name = ? and namespace = ? and team = ?",
                bean.getName(), bean.getNamespace(), bean.getTeam())) {
            throw new EntityException("此名称已被占用");
        }
    }

    @Override
    protected void afterAdd(T bean) throws EntityException {
        this.myGeneralDao.flush();
        // 自增code
        BigInteger maxCode = this.myGeneralDao
                .getOneBySQL("select max(cast(code as SIGNED INTEGER)) from enum_const where namespace = ? and team = ?",
                        bean.getNamespace(), bean.getTeam());
        int maxCodeInt = maxCode == null ? 1 : (maxCode.intValue() + 1);
        bean.setCode(String.valueOf(maxCodeInt));
        this.myGeneralDao.save(bean);
    }

    @Override
    public void afterExcelImport(List<T> beanList) throws EntityException {
        this.myGeneralDao.flush();
        // 自增code
        BigInteger maxCode = this.myGeneralDao
                .getOneBySQL("select max(cast(code as SIGNED INTEGER)) from enum_const where namespace = ? and team = ?",
                        this.getNamespace(), String.format(ENUM_CONST_TEAM_SING, SiteUtil.getSiteCode()));
        int maxCodeInt = maxCode == null ? 1 : (maxCode.intValue() + 1);
        for (T elem : beanList) {
            elem.setCode(String.valueOf(maxCodeInt ++));
            this.myGeneralDao.save(elem);
        }
    }

    @Override
    public void checkBeforeUpdate(EnumConst bean) throws EntityException {
        // 检查name唯一
        if (this.myGeneralDao.checkNotEmpty("select 1 from enum_const where name = ? and namespace = ? " +
                        "and team = ? and id <> ?", bean.getName(), bean.getNamespace(),
                bean.getTeam(), bean.getId())) {
            throw new EntityException("此名称已被占用");
        }
    }

    @Override
    protected String getOrderColumnIndex() {
        return "name";
    }

    /**
     * 获取命名空间
     * @return
     */
    protected abstract String getNamespace();

}
