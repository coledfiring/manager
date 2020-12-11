package com.whaty.products.service.oltrain.clazz.impl;

import com.whaty.constant.CommonConstant;
import com.whaty.core.framework.grid.bean.GridConfig;
import com.whaty.dao.GeneralDao;
import com.whaty.domain.bean.online.OlPeStudent;
import com.whaty.framework.asserts.TycjParameterAssert;
import com.whaty.framework.exception.ServiceException;
import com.whaty.framework.grid.adapter.service.impl.TycjGridServiceAdapter;
import com.whaty.util.CommonUtils;
import com.whaty.utils.UserUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;

/**
 * 添加班级学员服务类
 *
 * @author suoqiangqiang
 */
@Lazy
@Service("olAddClassStudentService")
public class OLAddClassStudentServiceImpl extends TycjGridServiceAdapter<OlPeStudent> {

    @Resource(name = CommonConstant.GENERAL_DAO_BEAN_NAME)
    private GeneralDao myGeneralDao;

    @Override
    public void initGrid(GridConfig gridConfig, Map<String, Object> mapParam) {
        String sql = gridConfig.gridConfigSource().getSql();
        String clId = (String) mapParam.get("parentId");
        gridConfig.gridConfigSource().setSql(String.format(sql, clId));
    }

    /**
     * 添加班级学员
     *
     * @param parentId
     * @param ids
     * @return
     */
    public int doAddClassStudent(String parentId, String ids) {
        TycjParameterAssert.isAllNotBlank(parentId, ids);
        if (this.myGeneralDao.checkNotEmpty("SELECT 1 FROM ol_class_student WHERE fk_class_id=? AND " +
                CommonUtils.madeSqlIn(ids, "fk_student_id"), parentId)) {
            throw new ServiceException("勾选条目中存在已添加到此班级的学员");
        }
        StringBuilder sql = new StringBuilder();
        sql.append(" INSERT INTO ol_class_student (                       ");
        sql.append("   id,                                                ");
        sql.append("   fk_student_id,                                     ");
        sql.append("   fk_class_id,                                       ");
        sql.append("   create_time,                                       ");
        sql.append("   create_by                                          ");
        sql.append(" ) SELECT                                             ");
        sql.append("   REPLACE (uuid(), '-', ''),                         ");
        sql.append("   id,                                                ");
        sql.append("   ?,                                                 ");
        sql.append("   now(),                                             ");
        sql.append("   ?                                                  ");
        sql.append(" FROM                                                 ");
        sql.append("   ol_pe_student                                      ");
        sql.append(" WHERE " + CommonUtils.madeSqlIn(ids, "id"));
        return this.myGeneralDao.executeBySQL(sql.toString(), parentId, UserUtils.getCurrentUserId());
    }

}
