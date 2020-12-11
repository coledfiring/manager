package com.whaty.products.service.enroll.impl;

import com.whaty.constant.CommonConstant;
import com.whaty.core.commons.exception.EntityException;
import com.whaty.core.framework.bean.EnumConst;
import com.whaty.dao.GeneralDao;
import com.whaty.domain.bean.enroll.EnrollColumn;
import com.whaty.framework.grid.adapter.service.impl.TycjGridServiceAdapter;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Objects;

/**
 * 报名项管理
 *
 * @author weipengsen
 */
@Lazy
@Service("enrollColumnManageService")
public class EnrollColumnManageServiceImpl extends TycjGridServiceAdapter<EnrollColumn> {

    @Resource(name = CommonConstant.GENERAL_DAO_BEAN_NAME)
    private GeneralDao generalDao;

    @Override
    public void checkBeforeAdd(EnrollColumn bean) throws EntityException {
        this.checkBeforeAddOrUpdate(bean);
    }

    @Override
    public void checkBeforeUpdate(EnrollColumn bean) throws EntityException {
        this.checkBeforeAddOrUpdate(bean);
    }

    private void checkBeforeAddOrUpdate(EnrollColumn bean) throws EntityException {
        EnumConst type = this.generalDao.getById(EnumConst.class, bean.getEnumConstByFlagEnrollColumnType().getId());
        if ("3".equals(type.getCode()) || "4".equals(type.getCode())) {
            if (Objects.isNull(bean.getEnumConstByFlagIsAttachFile())) {
                throw new EntityException("上传类型的是否附件字段不得为空");
            }
            if (Objects.isNull(bean.getFileNum())) {
                throw new EntityException("上传类型的文件数量字段不得为空");
            }
        }
    }
}
