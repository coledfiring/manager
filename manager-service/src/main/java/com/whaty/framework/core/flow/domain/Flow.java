package com.whaty.framework.core.flow.domain;

import com.alibaba.fastjson.JSON;
import com.whaty.domain.bean.FlowConfig;
import com.whaty.framework.core.flow.constant.FlowNodeType;
import lombok.Data;

import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 流程图领域模型
 *
 * @author weipengsen
 */
@Data
public class Flow implements Serializable {

    private static final long serialVersionUID = -8991248480333606862L;

    private String id;

    private String name;

    @NotNull
    private String workspaceWidth;

    @NotNull
    private String workspaceHeight;

    @NotNull
    private Map<String, FlowNode> nodes;

    @NotNull
    private List<FlowLine> lines;

    public FlowConfig convertToDo() {
        return new FlowConfig(this.id, this.name, JSON.toJSONString(this));
    }

    /**
     * 处理权限数据
     * @param categoryDTOS
     * @return
     */
    public Flow handlePriority(List<CategoryPriorityDTO> categoryDTOS) {
        Map<Integer, CategoryPriorityDTO> priorityMap = categoryDTOS.stream()
                .collect(Collectors.toMap(CategoryPriorityDTO::getBaseId, e -> e));
        this.nodes = this.nodes.values().stream().filter(e -> e.canShow(priorityMap.get(e.getCategoryId())))
                .peek(e -> e.setCategoryPriorityDTO(priorityMap.get(e.getCategoryId())))
                .peek(FlowNode::handlePriority).collect(Collectors.toMap(FlowNode::getId, e -> e));
        return this;
    }

    /**
     * 获取相关的资源节点id
     * @return
     */
    public Set<Integer> listCategoryIds() {
        return this.getNodes().values().stream().map(Flow.FlowNode::getCategoryId)
                .collect(Collectors.toSet());
    }

    /**
     * 流程图节点
     */
    @Data
    public static class FlowNode implements Serializable {

        private static final long serialVersionUID = 2279578946536399637L;

        @NotNull
        private String id;

        @NotNull
        private String top;

        @NotNull
        private String left;

        @NotNull
        private String name;

        @NotNull
        private Boolean canDelete;

        @NotNull
        private String type;

        private String categoryName;

        private Integer categoryId;

        private Boolean canTurn;

        private String priCategoryId;

        @Transient
        private CategoryPriorityDTO categoryPriorityDTO;

        /**
         * 是否可显示
         * @param categoryPriorityDTO
         * @return
         */
        public boolean canShow(CategoryPriorityDTO categoryPriorityDTO) {
            FlowNodeType type = FlowNodeType.getNodeType(this.getType());
            return  (type == FlowNodeType.CUSTOM || type == FlowNodeType.START_OR_END)
                    || (Objects.nonNull(categoryPriorityDTO) && categoryPriorityDTO.getIsActive());
        }

        /**
         * 处理权限数据
         */
        public void handlePriority() {
            if (Objects.isNull(this.categoryPriorityDTO)) {
                return;
            }
            this.setCanTurn(this.categoryPriorityDTO.getCanTurn());
            this.setPriCategoryId(this.categoryPriorityDTO.getCategoryId());
        }
    }

    /**
     * 流程图连线
     */
    @Data
    public static class FlowLine implements Serializable {

        private static final long serialVersionUID = -4670346886751216418L;

        @NotNull
        private String id;

        @NotNull
        private String node;

        @NotNull
        private String nodeSide;

        @NotNull
        private String nextNode;

        @NotNull
        private String nextNodeSide;

        private String label;

    }

}
