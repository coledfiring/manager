package com.whaty.products.service.enroll.impl;

import com.whaty.constant.CommonConstant;
import com.whaty.dao.GeneralDao;
import com.whaty.framework.asserts.TycjParameterAssert;
import com.whaty.products.service.enroll.SetEnrollColumnRegulationService;
import com.whaty.products.service.enroll.domain.SetRegulation;
import com.whaty.products.service.enroll.domain.SetRegulationParams;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.stream.Collectors;

/**
 * 设置报名规则
 *
 * @author weipengsen
 */
@Lazy
@Service("setEnrollColumnRegulationService")
public class SetEnrollColumnRegulationServiceImpl implements SetEnrollColumnRegulationService {

    @Resource(name = CommonConstant.GENERAL_DAO_BEAN_NAME)
    private GeneralDao generalDao;

    @Override
    public SetRegulation getRegulation(String id) {
        TycjParameterAssert.isAllNotBlank(id);
        return SetRegulation.buildById(id);
    }

    @Override
    public void saveRegulation(SetRegulationParams param) {
        TycjParameterAssert.validatePass(param);
        this.generalDao.batchExecuteSql(param.getColumns().stream()
                .map(e -> this.generateSql(param.getId(), e)).collect(Collectors.toList()));
    }

    private String generateSql(String id, SetRegulationParams.SetRegulationColumnParams column) {
        String sql = "insert into enroll_column_regulation_detail (fk_regulation_id, fk_column_id, " +
                "is_active, is_required, serial_number) values ('%s', '%s', '%s', '%s', %s) " +
                "on duplicate key update is_active = '%s', is_required = '%s', serial_number = %s";
        return String.format(sql, id, column.getId(), column.getIsActive() ? "1" : "0",
                column.getIsRequired() ? "1" : "0", column.getSerialNumber(), column.getIsActive() ? "1" : "0",
                column.getIsRequired() ? "1" : "0", column.getSerialNumber());
    }
}
