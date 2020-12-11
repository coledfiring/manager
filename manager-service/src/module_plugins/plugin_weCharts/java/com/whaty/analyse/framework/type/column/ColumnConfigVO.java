package com.whaty.analyse.framework.type.column;

import com.whaty.analyse.framework.AnalyseType;
import com.whaty.analyse.framework.domain.AbstractConvertConfigVO;
import com.whaty.framework.asserts.TycjAssert;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.commons.collections.MapUtils;

import java.io.Serializable;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 横列展示配置vo
 *
 * items: [
 *      {
 *          label: string,// 横列项的名称
 *          value: number,// 值
 *          serial: number,// 显示顺序
 *          highlight: boolean// 是否高亮显示
 *      }
 * ]
 *
 * @author weipengsen
 */
@Data
public class ColumnConfigVO extends AbstractConvertConfigVO<ColumnConfigDO> {

    private static final long serialVersionUID = 2000761888647052275L;

    private List<ColumnItemConfigVO> items;

    public ColumnConfigVO(ColumnConfigDO configDO) {
        super(configDO);
    }

    @Override
    protected void convert(ColumnConfigDO configDO) {
        this.setItems(ColumnItemConfigVO.convert(configDO.getColumns(), configDO.getData()));
    }

    @Override
    protected AnalyseType getAnalyseType() {
        return AnalyseType.COLUMN_ANALYSE;
    }

    /**
     * 横列展示项vo
     *
     * @author weipengsen
     */
    @Data
    @AllArgsConstructor
    public static class ColumnItemConfigVO implements Serializable {

        private static final long serialVersionUID = 4284037096389529560L;

        private String label;

        private Object value;

        private Integer serial;

        private Boolean highlight;

        public ColumnItemConfigVO(ColumnConfigDO.ColumnItemConfigDO column, Object data) {
            this.label = column.getLabel();
            this.value = data;
            this.serial = column.getSerial();
            this.highlight = column.getHighlight();
        }

        /**
         * 转换数据类型
         * @param columns
         * @param data
         * @return
         */
        public static List<ColumnItemConfigVO> convert(List<ColumnConfigDO.ColumnItemConfigDO> columns,
                                                       Map<String, Object> data) {
            TycjAssert.isAllNotEmpty(columns);
            if (MapUtils.isEmpty(data)) {
                return null;
            }
            return columns.stream().map(e -> new ColumnItemConfigVO(e, data.get(e.getAlias())))
                    .sorted(Comparator.comparingInt(ColumnItemConfigVO::getSerial))
                    .collect(Collectors.toList());
        }
    }
}
