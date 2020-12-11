package com.whaty.products.service.flow.builder;

import com.alibaba.fastjson.JSONObject;
import com.whaty.common.string.StringUtils;
import com.whaty.constant.CommonConstant;
import com.whaty.constant.EnumConstConstants;
import com.whaty.core.commons.datasource.MasterSlaveRoutingDataSource;
import com.whaty.core.framework.bean.EnumConst;
import com.whaty.domain.bean.flow.CheckFlow;
import com.whaty.domain.bean.flow.CheckFlowDetail;
import com.whaty.domain.bean.flow.CheckFlowGroup;
import com.whaty.products.service.flow.domain.CustomCheckFlowParams;
import com.whaty.products.service.flow.domain.flow.CheckFlowDTO;
import com.whaty.utils.StaticBeanUtils;
import com.whaty.utils.UserUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;

import java.util.Arrays;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 审核流程构建器
 *
 * @author weipengsen
 */
public class CheckFlowBuilder {

    private CheckFlow checkFlow;

    private CheckFlowDTO checkFlowDTO;

    public CheckFlowBuilder() {
        this.checkFlow = new CheckFlow();
        this.initCheckFlow();
    }

    /**
     * 初始化流程配置
     */
    private void initCheckFlow() {
        this.checkFlow.setSiteCode(MasterSlaveRoutingDataSource.getDbType());
        this.checkFlow.setCreateBy(UserUtils.getCurrentUser());
        this.checkFlow.setCreateDate(new Date());
        this.checkFlow.setEnumConstByFlagActive(StaticBeanUtils.getGeneralDao()
                .getEnumConstByNamespaceCode(CommonConstant.ENUM_CONST_NAMESPACE_FLAG_ACTIVE, "1"));
        this.checkFlow.setEnumConstByFlagCheckStatus(StaticBeanUtils.getGeneralDao()
                .getEnumConstByNamespaceCode(EnumConstConstants.ENUM_CONST_NAMESPACE_CHECK_STATUS, "1"));
    }

    /**
     * 新建构建
     * @return
     */
    public static CheckFlowBuilder newCheckFlow() {
        return new CheckFlowBuilder();
    }

    /**
     * 增加审核类型
     * @param type
     * @return
     */
    public CheckFlowBuilder withFlowType(String type) {
        this.checkFlow.setEnumConstByFlagFlowType(StaticBeanUtils.getGeneralDao()
                .getEnumConstByNamespaceCode(EnumConstConstants.ENUM_CONST_NAMESPACE_FLOW_TYPE, type));
        return this;
    }

    /**
     * 增加审核流程关联组
     * @param groupId
     * @return
     */
    public CheckFlowBuilder withFlowGroup(String groupId) {
        this.checkFlow.setCheckFlowGroup(new CheckFlowGroup());
        this.checkFlow.getCheckFlowGroup().setId(groupId);
        String needCheckId = StaticBeanUtils.getGeneralDao()
                .getOneBySQL("select flag_need_check from check_flow_group where id = ?", groupId);
        this.checkFlow.setEnumConstByFlagNeedCheck(StaticBeanUtils.getGeneralDao()
                .getById(EnumConst.class, needCheckId));
        this.initCheckFlowDetail();
        return this;
    }

    private void initCheckFlowDetail() {
        String copyPersonStr = StaticBeanUtils.getGeneralDao()
                .getOneBySQL("select cp.copy_person from check_flow_group g " +
                        "inner join check_flow_copy_person cp on cp.id = g.fk_copy_person_detail_id where g.id = ?",
                        this.checkFlow.getCheckFlowGroup().getId());
        List<String> copyPersons = null;
        if (StringUtils.isNotBlank(copyPersonStr)) {
            copyPersons = this.convertStringToList(copyPersonStr);
        }

        List<Map<String, Object>> auditorStr = StaticBeanUtils.getGeneralDao()
                .getMapBySQL("SELECT node.id AS id, ct. CODE AS checkType, aut. CODE AS auditorType," +
                        " aud.auditor AS auditor, nt.code as nodeType FROM check_flow_node node " +
                        "LEFT JOIN check_flow_auditor aud ON aud.id = node.fk_auditor_detail_id " +
                        "LEFT JOIN enum_const ct ON ct.id = node.flag_check_type " +
                        "LEFT JOIN enum_const aut ON aut.id = node.flag_auditor_type " +
                        "INNER JOIN enum_const nt on nt.id = node.flag_node_type " +
                        "WHERE node.fk_flow_group_id = ? order by node.serial",
                        this.checkFlow.getCheckFlowGroup().getId());
        LinkedHashMap<String, CheckFlowDTO.CheckFlowAuditorNodeDTO> auditorList = new LinkedHashMap<>();
        for (Map<String, Object> elem : auditorStr) {
            auditorList.put((String) elem.get("id"), new CheckFlowDTO.CheckFlowAuditorNodeDTO((String) elem.get("id"),
                    this.convertStringToList((String) elem.get("auditor")),
                    (String) elem.get("checkType"), (String) elem.get("auditorType"), (String) elem.get("nodeType")));
        }
        this.checkFlowDTO = new CheckFlowDTO(auditorList, copyPersons);
    }

    /**
     * 将带,的字符串转换成list且过滤空白字符
     * @param origin
     * @return
     */
    private List<String> convertStringToList(String origin) {
        if (StringUtils.isBlank(origin)) {
            return null;
        }
        return Arrays.stream(origin.split(CommonConstant.SPLIT_ID_SIGN)).map(String::trim).collect(Collectors.toList());
    }

    /**
     * 关联审核业务数据
     * @param itemId
     * @return
     */
    public CheckFlowBuilder withItemId(String itemId) {
        this.checkFlow.setItemId(itemId);
        return this;
    }

    /**
     * 构建流程
     * @return
     */
    public CheckFlow build() {
        this.checkFlowDTO.filterEmptyAuditor();
        if ("1".equals(this.checkFlow.getEnumConstByFlagNeedCheck().getCode())
                && MapUtils.isNotEmpty(this.checkFlowDTO.getCheckFlowAuditors())) {
            this.checkFlow.setCurrentNode(this.checkFlowDTO.getCheckFlowAuditors().keySet().iterator().next());
        }
        this.checkFlow.setCheckFlowDetail(new CheckFlowDetail(JSONObject
                .toJSONString(this.checkFlowDTO)));
        return this.checkFlow;
    }

    /**
     * 设置名称
     * @param itemName
     * @return
     */
    public CheckFlowBuilder withName(String itemName) {
        this.checkFlow.setName(itemName);
        return this;
    }

    /**
     * 设置权限范围
     * @param scopeId
     * @return
     */
    public CheckFlowBuilder withScopeId(String scopeId) {
        this.checkFlow.setScopeId(scopeId);
        return this;
    }

    /**
     * 设置自定义配置
     * @param params
     * @return
     */
    public CheckFlowBuilder withCustomConfig(CustomCheckFlowParams params) {
        if (MapUtils.isNotEmpty(params.getNodes())) {
            params.getNodes().entrySet().stream()
                    .filter(e -> CollectionUtils.isNotEmpty(e.getValue().getAuditors()))
                    .forEach(e -> this.checkFlowDTO.putCustomNode(e.getValue()));
        }
        if (CollectionUtils.isNotEmpty(params.getCopyPersons())) {
            this.checkFlowDTO.setCopyPersons(params.getCopyPersons());
        }
        return this;
    }
}
