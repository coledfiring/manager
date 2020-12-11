package com.whaty.products.service.enroll.domain;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * 设置报名规则参数
 *
 * @author weipengsen
 */
@Data
public class SetRegulationParams implements Serializable {

    private static final long serialVersionUID = 306787975918179368L;

    @NotNull
    private String id;

    @NotNull
    private List<SetRegulationColumnParams> columns;

    @Data
    public static class SetRegulationColumnParams implements Serializable {

        private static final long serialVersionUID = -6237025031076985846L;

        private String id;

        private Integer serialNumber;

        private Boolean isActive;

        private Boolean isRequired;

    }


}