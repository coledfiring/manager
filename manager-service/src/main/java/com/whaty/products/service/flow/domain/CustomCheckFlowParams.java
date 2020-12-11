package com.whaty.products.service.flow.domain;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 自定义审批发起参数
 *
 * @author weipengsen
 */
@Data
public class CustomCheckFlowParams implements Serializable {

    private static final long serialVersionUID = -558966367117925417L;

    @NotNull
    private String itemId;

    @NotNull
    private String type;

    @NotNull
    private List<String> copyPersons;

    @NotNull
    private Map<String, CustomCheckFlowNode> nodes;

    /**
     * 自定义审批节点
     *
     * @author weipengsen
     */
    @Data
    public static class CustomCheckFlowNode implements Serializable {

        private static final long serialVersionUID = -5485566116774035600L;

        private String id;

        private String checkType;

        private String auditorType;

        private List<String> auditors;

        private String nodeType;
    }

}
