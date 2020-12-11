package com.whaty.file.grid.service.domain;

import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 打印模板VO
 *
 * @author weipengsen
 */
@Data
public class TemplateVO implements Serializable {

    private static final long serialVersionUID = -754673221653109030L;

    private String id;

    private String code;

    private String title;

    private String path;

    private String defaultPath;

    private Map<String, List<TemplateSignVO>> printTemplateSigns;

}
