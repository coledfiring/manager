package com.whaty.file.grid.service.domain;

import lombok.Data;

import java.io.Serializable;

/**
 * 模板占位符VO
 *
 * @author weipengsen
 */
@Data
public class TemplateSignVO implements Serializable {

    private static final long serialVersionUID = -3401019953985211240L;

    private String id;

    private String name;

    private String sign;

    private String group;

    private transient String templateId;

}
