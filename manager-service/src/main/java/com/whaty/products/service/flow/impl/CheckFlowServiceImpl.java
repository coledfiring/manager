package com.whaty.products.service.flow.impl;

import com.alibaba.fastjson.JSON;
import com.whaty.common.string.StringUtils;
import com.whaty.constant.CommonConstant;
import com.whaty.core.commons.datasource.MasterSlaveRoutingDataSource;
import com.whaty.dao.GeneralDao;
import com.whaty.framework.asserts.TycjParameterAssert;
import com.whaty.framework.exception.ServiceException;
import com.whaty.products.service.flow.CheckFlowService;
import com.whaty.products.service.flow.constant.CheckFlowType;
import com.whaty.products.service.flow.domain.CustomCheckFlowParams;
import com.whaty.products.service.flow.domain.config.CheckFlowConfig;
import com.whaty.products.service.flow.domain.flow.CheckFlowDTO;
import com.whaty.products.service.flow.domain.flow.CheckFlowVO;
import com.whaty.products.service.flow.strategy.check.AbstractCheckFlowStrategy;
import com.whaty.utils.UserUtils;
import org.apache.commons.collections.MapUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * 审核流程操作
 *
 * @author weipengsen
 */
@Lazy
@Service("checkFlowService")
public class CheckFlowServiceImpl implements CheckFlowService {

    @Resource(name = CommonConstant.GENERAL_DAO_BEAN_NAME)
    private GeneralDao generalDao;

    private final static String TITLE_FORMAT_STRING = "%s提交的%s%s";

    @Override
    public void doApply(String itemId, String type) throws Exception {
        TycjParameterAssert.isAllNotBlank(itemId, type);
        String groupId = this.getConfigIdByType(type);
        if (StringUtils.isBlank(groupId)) {
            throw new ServiceException("没有设置此类型的审核流程或流程不是有效的");
        }
        new CheckFlowConfig(groupId).apply(itemId, type);
    }

    /**
     * 通过类型获取配置
     * @param type
     * @return
     */
    private String getConfigIdByType(String type) {
        return this.generalDao.getOneBySQL("SELECT ty.fk_flow_group_id FROM check_flow_type ty" +
                        " INNER JOIN check_flow_group g ON g.id = ty.fk_flow_group_id " +
                        "INNER JOIN enum_const ft ON ft.id = ty.flag_flow_type " +
                        "INNER JOIN enum_const ac on ac.id = g.flag_active " +
                        "WHERE ft. CODE = ? AND g.site_code = ? AND ac.code = '1'",
                type, MasterSlaveRoutingDataSource.getDbType());
    }

    /**
     * 根据checkDetail查询sql 生成审核结果
     *
     * @param checkDetail
     * @param type
     * @return
     */
    private  Map<String, Object> getCheckResult(Map<String, Object> checkDetail, String type){
        if (MapUtils.isEmpty(checkDetail)) {
            throw new ServiceException("审核流程不存在");
        }
        Map<String, Object> resultMap = new HashMap<>(4);
        AbstractCheckFlowStrategy strategy = CheckFlowType.getStrategy(type);
        // 业务信息
        resultMap.put("business", strategy.getBusiness((String) checkDetail.get("itemId")));
        resultMap.put("id", checkDetail.get("id"));
        resultMap.put("applyStatus", checkDetail.get("applyStatus"));
        resultMap.put("currentNode", checkDetail.get("currentNode"));
        CheckFlowDTO checkFlowDO = JSON.parseObject((String) checkDetail.get("checkDetail"), CheckFlowDTO.class);
        boolean canCheck = checkDetail.get("currentNode") != null
                && checkFlowDO.getCheckFlowAuditors().get(checkDetail.get("currentNode"))
                .canCheck(UserUtils.getCurrentManager().getId());
        resultMap.put("canCheck", canCheck);
        resultMap.put("checkDetail", CheckFlowVO.convertVo(checkFlowDO));
        // 提交信息
        String sql = "select m.fk_sso_user_id as id, m.true_name as name, re.item_name as itemName, " +
                " date_format(re.check_time, '%Y-%m-%d %H:%i:%s') as applyTime" +
                " from check_flow_record re inner join enum_const ty on ty.id = re.flag_check_operate" +
                " inner join pe_manager m on m.id = operate_user where fk_check_flow_id = ? and ty.code = '3'";
        Map<String, Object> itemInfo = this.generalDao.getOneMapBySQL(sql, checkDetail.get("id"));
        itemInfo.put("selfApplied", UserUtils.getCurrentUserId().equals(itemInfo.get("id")));
        resultMap.put("title", String.format(TITLE_FORMAT_STRING, itemInfo.get("name"), itemInfo.get("itemName"),
                CheckFlowType.getTypeName(type)));
        resultMap.put("applyPerson", itemInfo);
        return resultMap;
    }

    @Override
    public Map<String, Object> getCheckFlowConfig(String id, String type) {
        TycjParameterAssert.isAllNotBlank(id, type);
        // 配置
        String sql = "SELECT cf.id as id, cf.current_node AS currentNode, cfd.check_detail AS checkDetail," +
                " st.code as applyStatus, cf.fk_item_id as itemId FROM check_flow cf " +
                "INNER JOIN check_flow_detail cfd ON cfd.id = cf.fk_check_detail_id " +
                "INNER JOIN enum_const st ON st.id = cf.flag_check_status " +
                "INNER JOIN enum_const ty ON ty.id = cf.flag_flow_type WHERE cf.id = ? " +
                "AND ty.code = ?";
        return this.getCheckResult(this.generalDao.getOneMapBySQL(sql, id, type), type);
    }

    @Override
    public Map<String, Object> getCheckFlowConfigByItemId(String fkItemId, String type) {
        TycjParameterAssert.isAllNotBlank(fkItemId, type);
        String sql = "SELECT cf.id as id, cf.current_node AS currentNode, cfd.check_detail AS checkDetail," +
                " st.code as applyStatus, cf.fk_item_id as itemId " +
                " FROM check_flow cf " +
                " INNER JOIN check_flow_detail cfd ON cfd.id = cf.fk_check_detail_id " +
                " INNER JOIN enum_const st ON st.id = cf.flag_check_status " +
                " INNER JOIN enum_const fa ON fa.id = cf.flag_active AND fa.code = '1' " +
                " INNER JOIN enum_const ty ON ty.id = cf.flag_flow_type " +
                " WHERE cf.fk_item_id = ? AND ty.code = ? AND cf.site_code = ? limit 1";
        return this.getCheckResult(
                this.generalDao.getOneMapBySQL(sql, fkItemId, type, MasterSlaveRoutingDataSource.getDbType()), type);
    }

    @Override
    public void doPassCheck(String id, String currentNode, String note) throws Exception {
        TycjParameterAssert.isAllNotBlank(id, currentNode);
        new CheckFlowDTO(id).passCurrentNode(currentNode, note);
    }

    @Override
    public void doNoPassCheck(String id, String currentNode, String note) throws Exception {
        TycjParameterAssert.isAllNotBlank(id, currentNode);
        new CheckFlowDTO(id).rejectCurrentNode(currentNode, note);
    }

    @Override
    public void doCancelCheck(String id) {
        TycjParameterAssert.isAllNotBlank(id);
        new CheckFlowDTO(id).cancelCheck();
    }

    @Override
    public CheckFlowConfig getCheckFlowConfigWithCustom(String type) {
        TycjParameterAssert.isAllNotBlank(type);
        String groupId = this.getConfigIdByType(type);
        if (StringUtils.isBlank(groupId)) {
            throw new ServiceException("没有设置此类型的审核流程或流程不是有效的");
        }
        return new CheckFlowConfig(groupId).build();
    }

    @Override
    public void doApplyWithCustom(CustomCheckFlowParams params) throws Exception {
        TycjParameterAssert.validatePass(params);
        String groupId = this.getConfigIdByType(params.getType());
        if (StringUtils.isBlank(groupId)) {
            throw new ServiceException("没有设置此类型的审核流程或流程不是有效的");
        }
        new CheckFlowConfig(groupId).applyWithCustom(params);
    }

    @Override
    public void canApply(String itemId, String type) {
        TycjParameterAssert.isAllNotBlank(itemId, type);
        if (!CheckFlowType.getStrategy(type).checkCanApply(itemId)) {
            throw new ServiceException("只有未申请和审批不通过的数据才可以发起申请");
        }
    }

}
