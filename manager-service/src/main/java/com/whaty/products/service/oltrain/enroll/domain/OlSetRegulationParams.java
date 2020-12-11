package com.whaty.products.service.oltrain.enroll.domain;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * 设置报名规则参数
 *
 * @author suoqiangqiang
 */
@Data
public class OlSetRegulationParams implements Serializable {

    private static final long serialVersionUID = -6697792644544362511L;

    @NotNull
    private String id;

    @NotNull
    private List<OlSetRegulationColumnParams> columns;

    @Data
    public static class OlSetRegulationColumnParams implements Serializable {

        private static final long serialVersionUID = 5102634714911581869L;

        private String id;

        private Integer serialNumber;

        private Boolean isActive;

        private Boolean isRequired;

    }


}