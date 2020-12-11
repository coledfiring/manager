package com.whaty.products.service.classmaster.utils;

import com.whaty.framework.asserts.TycjParameterAssert;
import com.whaty.utils.StaticBeanUtils;
import com.whaty.utils.UserUtils;

/**
 * 班主任工作室工具类
 *
 * @author weipengsen
 */
public class ClassMasterUtils {

    /**
     * 检查是否是此班级的班主任
     * @param classId
     * @return
     */
    public static boolean checkIsClassMaster(String classId) {
        TycjParameterAssert.isAllNotBlank(classId);
        return StaticBeanUtils.getGeneralDao()
                .checkNotEmpty("select 1 from pe_class c inner join pe_manager m on m.id = c.fk_class_master_id " +
                        "where c.id = ? and m.fk_sso_user_id = ?", classId, UserUtils.getCurrentUserId());
    }

}
