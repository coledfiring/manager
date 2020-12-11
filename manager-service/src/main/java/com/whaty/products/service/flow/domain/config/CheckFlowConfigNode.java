package com.whaty.products.service.flow.domain.config;

import com.whaty.framework.asserts.Verifiable;
import com.whaty.constant.EnumConstConstants;
import com.whaty.domain.bean.flow.CheckFlowAuditor;
import com.whaty.domain.bean.flow.CheckFlowGroup;
import com.whaty.domain.bean.flow.CheckFlowNode;
import com.whaty.util.CommonUtils;
import com.whaty.utils.StaticBeanUtils;
import com.whaty.utils.UserUtils;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 流程配置节点
 *
 * @author weipengsen
 */
@Data
@NoArgsConstructor
public class CheckFlowConfigNode implements Serializable, Verifiable {

    private static final long serialVersionUID = -5850680907930951444L;

    @NotNull
    private String id;

    @NotNull
    private String nodeType;

    private String checkType;

    private String auditorType;

    private List<CheckFlowConfigAuditor> auditors;

    @Transient
    private transient String groupId;

    private CheckFlowConfigNode(Map<String, Object> origin) {
        this.id = (String) origin.get("id");
        this.nodeType = (String) origin.get("nodeType");
        if ("1".equals(nodeType)) {
            this.checkType = (String) origin.get("checkType");
            this.auditorType = (String) origin.get("auditorType");
            if (StringUtils.isNotBlank((String) origin.get("auditor"))) {
                this.auditors = CheckFlowConfigAuditor
                        .listAuditors((String) origin.get("auditor"), this.auditorType);
            }
        }
    }

    /**
     * 列举节点
     *
     * @param configId
     * @return
     */
    public static List<CheckFlowConfigNode> listNodes(String configId) {
        // 审核人配置
        List<Map<String, Object>> nodes = StaticBeanUtils.getGeneralDao()
                .getMapBySQL("SELECT node.id AS id, ct. code AS checkType, " +
                        " at.code AS auditorType, au.auditor as auditor, nt.code as nodeType" +
                        " FROM check_flow_node node LEFT JOIN enum_const ct ON ct.id = node.flag_check_type " +
                        " LEFT JOIN enum_const at ON at.id = node.flag_auditor_type " +
                        " INNER JOIN enum_const nt on nt.id = node.flag_node_type " +
                        " LEFT JOIN check_flow_auditor au ON au.id = node.fk_auditor_detail_id" +
                        " WHERE node.fk_flow_group_id = ? ORDER BY node.serial", configId);
        return nodes.stream().map(CheckFlowConfigNode::new).collect(Collectors.toList());
    }

    /**
     * 转化为Do
     * @return
     */
    public CheckFlowNode convertToDo() {
        CheckFlowNode node = StaticBeanUtils.getGeneralDao().getById(CheckFlowNode.class, this.id);
        node.setUpdateDate(new Date());
        node.setUpdateBy(UserUtils.getCurrentUser());
        node.setEnumConstByFlagNodeType(StaticBeanUtils.getGeneralDao()
                .getEnumConstByNamespaceCode(EnumConstConstants.ENUM_CONST_NAMESPACE_NODE_TYPE, nodeType));
        if ("1".equals(this.nodeType)) {
            node.setEnumConstByFlagAuditorType(StaticBeanUtils.getGeneralDao()
                    .getEnumConstByNamespaceCode(EnumConstConstants.ENUM_CONST_NAMESPACE_AUDITOR_TYPE, auditorType));
            node.setEnumConstByFlagCheckType(StaticBeanUtils.getGeneralDao()
                    .getEnumConstByNamespaceCode(EnumConstConstants.ENUM_CONST_NAMESPACE_CHECK_TYPE, checkType));
            CheckFlowAuditor auditor = node.getCheckFlowAuditor() == null ? new CheckFlowAuditor()
                    : node.getCheckFlowAuditor();
            node.setCheckFlowAuditor(auditor);
            auditor.setAuditor(CommonUtils.join(this.auditors.stream()
                    .map(CheckFlowConfigAuditor::getId)
                    .collect(Collectors.toList()), ",", ""));
        } else {
            node.setEnumConstByFlagAuditorType(null);
            node.setEnumConstByFlagCheckType(null);
            if (Objects.nonNull(node.getCheckFlowAuditor())) {
                StaticBeanUtils.getGeneralDao().delete(node.getCheckFlowAuditor());
            }
            node.setCheckFlowAuditor(null);
        }
        return node;
    }

    /**
     * 保存节点到数据库
     * @return
     */
    public CheckFlowNode save() {
        CheckFlowNode nodeDo = this.convertToDo();
        Optional.ofNullable(nodeDo.getCheckFlowAuditor()).ifPresent(StaticBeanUtils.getGeneralDao()::save);
        StaticBeanUtils.getGeneralDao().save(nodeDo);
        return nodeDo;
    }

    /**
     * 增加第一个节点
     * @param groupId
     * @return
     */
    public static CheckFlowNode addFirstNode(String groupId) {
        CheckFlowNode node = newNode(groupId);
        node.setSerial(1);
        StaticBeanUtils.getGeneralDao().executeBySQL("update check_flow_node set serial = serial + 1" +
                " where fk_flow_group_id = ?", groupId);
        return StaticBeanUtils.getGeneralDao().save(node);
    }

    /**
     * 在节点后增加新节点
     * @param groupId
     * @param previousId
     * @return
     */
    public static CheckFlowNode addNodeAfter(String groupId, String previousId) {
        Map<String, Object> previousNode = StaticBeanUtils.getGeneralDao()
                .getOneMapBySQL("select fk_flow_group_id as groupId, serial as serial from check_flow_node " +
                        "where id = ?", previousId);
        CheckFlowNode node = newNode(groupId);
        node.setSerial(((Integer) previousNode.get("serial")) + 1);
        StaticBeanUtils.getGeneralDao().save(node);
        StaticBeanUtils.getGeneralDao().executeBySQL("update check_flow_node set serial = serial + 1 " +
                "where serial > ? and fk_flow_group_id = ?", previousNode.get("serial"), previousNode.get("groupId"));
        return node;
    }

    /**
     * 创建新节点对象
     * @param groupId
     * @return
     */
    private static CheckFlowNode newNode(String groupId) {
        CheckFlowNode node = new CheckFlowNode();
        node.setCheckFlowGroup(StaticBeanUtils.getGeneralDao().getById(CheckFlowGroup.class, groupId));
        node.setEnumConstByFlagNodeType(StaticBeanUtils.getGeneralDao()
                .getEnumConstByNamespaceCode(EnumConstConstants.ENUM_CONST_NAMESPACE_NODE_TYPE, "1"));
        node.setEnumConstByFlagAuditorType(StaticBeanUtils.getGeneralDao()
                .getEnumConstByNamespaceCode(EnumConstConstants.ENUM_CONST_NAMESPACE_AUDITOR_TYPE, "1"));
        node.setEnumConstByFlagCheckType(StaticBeanUtils.getGeneralDao()
                .getEnumConstByNamespaceCode(EnumConstConstants.ENUM_CONST_NAMESPACE_CHECK_TYPE, "1"));
        node.setCreateBy(UserUtils.getCurrentUser());
        node.setCreateDate(new Date());
        return node;
    }

    /**
     * 删除节点
     * @param nodeId
     */
    public static void deleteNode(String groupId, String nodeId) {
        Integer serial = StaticBeanUtils.getGeneralDao()
                .getOneBySQL("select serial from check_flow_node where id = ?", nodeId);
        StaticBeanUtils.getGeneralDao().executeBySQL("delete from check_flow_node where id = ?", nodeId);
        StaticBeanUtils.getGeneralDao().executeBySQL("update check_flow_node set serial = serial - 1 " +
                "where serial > ? and fk_flow_group_id = ?", serial, groupId);
    }

    @Override
    public boolean verify() {
        return !("1".equals(this.nodeType)
                && (StringUtils.isBlank(this.auditorType) || StringUtils.isBlank(this.checkType)
                || CollectionUtils.isEmpty(this.auditors)));
    }

    /**
     * 流程配置审批人
     */
    @Data
    @NoArgsConstructor
    private static class CheckFlowConfigAuditor implements Serializable {

        private static final long serialVersionUID = -3586831543515888292L;

        private String id;

        private String name;

        private CheckFlowConfigAuditor(Map<String, Object> origin) {
            this.id = (String) origin.get("id");
            this.name = (String) origin.get("name");
        }

        /**
         * 列举审批人
         *
         * @param auditor
         * @param auditorType
         * @return
         */
        private static List<CheckFlowConfigAuditor> listAuditors(String auditor, String auditorType) {
            return StaticBeanUtils.getGeneralDao()
                    .getMapBySQL(AuditorType.getSql(auditorType) + CommonUtils.madeSqlIn(auditor, "id"))
                    .stream().filter(Objects::nonNull).map(CheckFlowConfigAuditor::new)
                    .collect(Collectors.toList());
        }

        private enum AuditorType {

            ROLE("1", "SELECT id, name as name FROM pe_pri_role WHERE "),

            MANAGER("2", "SELECT id, true_name AS name FROM pe_manager WHERE "),;

            private String code;

            private String sql;

            AuditorType(String code, String sql) {
                this.code = code;
                this.sql = sql;
            }

            private static String getSql(String code) {
                return Arrays.stream(values()).filter(e -> code.equals(e.getCode())).findFirst()
                        .orElseThrow(IllegalArgumentException::new).getSql();
            }

            public String getCode() {
                return code;
            }

            public String getSql() {
                return sql;
            }
        }

    }
}