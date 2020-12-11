package com.whaty.products.service.record.impl;

import com.whaty.constant.CommonConstant;
import com.whaty.dao.GeneralDao;
import com.whaty.framework.aop.operatelog.constant.DataDiffType;
import com.whaty.framework.asserts.TycjParameterAssert;
import com.whaty.framework.grid.adapter.service.impl.TycjGridServiceAdapter;
import com.whaty.products.service.record.domain.OperateRecordStatusVO;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 操作日志记录查询服务类
 *
 * @author weipengsen
 */
@Lazy
@Service("operateRecordSearchService")
public class OperateRecordSearchServiceImpl extends TycjGridServiceAdapter {

    @Resource(name = CommonConstant.GENERAL_DAO_BEAN_NAME)
    private GeneralDao generalDao;

    /**
     * 列举操作记录
     * @param namespace
     * @param id
     * @return
     */
    public List<OperateRecordStatusVO> listOperateRecordStatus(String namespace, String id) {
        TycjParameterAssert.isAllNotBlank(namespace, id);
        String sql = "SELECT osr.operate_name AS operateName, " +
                "DATE_FORMAT( osr.operate_time, '%Y-%m-%d %H:%i:%s' ) AS operateTime, " +
                "ifnull( ss.true_name, '未知用户' ) AS operateUser, de.status_data AS statusData " +
                "FROM operate_status_record osr " +
                "INNER JOIN operate_status_record_detail de ON de.fk_operate_record_status_id = osr.id " +
                "LEFT JOIN sso_user ss ON ss.id = osr.fk_operate_user_id " +
                "WHERE osr.name_space = ? AND osr.fk_link_id = ? AND de.type = ? order by osr.operate_time desc";
        List<Map<String, Object>> statusData = this.generalDao.getMapBySQL(sql, namespace, id,
                DataDiffType.CHANGE.getType());
        if (CollectionUtils.isEmpty(statusData)) {
            return null;
        }
        return statusData.stream().map(OperateRecordStatusVO::convert).collect(Collectors.toList());
    }

    /**
     * 列举删除操作记录
     * @param namespace
     * @return
     */
    public List<OperateRecordStatusVO> listDeleteOperateRecordStatus(String namespace) {
        TycjParameterAssert.isAllNotBlank(namespace);
        String sql = "SELECT osr.operate_name AS operateName, " +
                "DATE_FORMAT( osr.operate_time, '%Y-%m-%d %H:%i:%s' ) AS operateTime, " +
                "ifnull( ss.true_name, '未知用户' ) AS operateUser, de.status_data AS statusData " +
                "FROM operate_status_record osr " +
                "INNER JOIN operate_status_record_detail de ON de.fk_operate_record_status_id = osr.id " +
                "LEFT JOIN sso_user ss ON ss.id = osr.fk_operate_user_id " +
                "WHERE osr.name_space = ? AND de.type = ? order by osr.operate_time desc";
        List<Map<String, Object>> statusData = this.generalDao.getMapBySQL(sql, namespace,
                DataDiffType.DELETE.getType());
        if (CollectionUtils.isEmpty(statusData)) {
            return null;
        }
        return statusData.stream().map(OperateRecordStatusVO::convert).collect(Collectors.toList());
    }

    @Override
    protected String getOrderColumnIndex() {
        return "operateTime";
    }

    @Override
    protected String getOrderWay() {
        return "desc";
    }
}
