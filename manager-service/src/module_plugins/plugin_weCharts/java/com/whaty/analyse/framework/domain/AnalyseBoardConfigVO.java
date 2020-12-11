package com.whaty.analyse.framework.domain;

import com.whaty.analyse.framework.domain.bean.AnalyseBlockConfig;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * AnalyseBoardConfig配置vo
 *
 * @author pingzhihao
 */
@Data
@NoArgsConstructor
public class AnalyseBoardConfigVO implements Serializable {

    private static final long serialVersionUID = -7361005147370865466L;

    private String id;

    private String title;

    private String code;

    private String canExport;

    private String label;

    private Date createDate;

    private List<BoardBlockVO> blockConfigs;

    private List<AnalyseConditionVO> conditions;

    @Data
    @AllArgsConstructor
    public static class BoardBlockVO implements Serializable {

        private static final long serialVersionUID = 6470425454010102360L;

        private Number colNum;

        private String id;

        private AnalyseBlockConfig blockConfig;

    }
}
