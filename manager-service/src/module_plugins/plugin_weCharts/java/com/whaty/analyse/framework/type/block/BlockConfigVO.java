package com.whaty.analyse.framework.type.block;

import com.whaty.analyse.framework.AnalyseType;
import com.whaty.analyse.framework.domain.AbstractConvertConfigVO;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 文本块配置VO
 *
 * [
 *      {
 *          label: string,
 *          value: string,
 *          isMaster: boolean(unique)
 *      }
 * ]
 *
 * @author weipengsen
 */
@Data
public class BlockConfigVO extends AbstractConvertConfigVO<BlockConfigDO> {

    private static final long serialVersionUID = 1394502503311871510L;

    private List<BlockItemConfigVO> items;

    public BlockConfigVO(BlockConfigDO configDO) {
        super(configDO);
    }

    @Override
    protected void convert(BlockConfigDO configDO) {
        this.setItems(configDO.getItems().stream()
                .map(e -> new BlockItemConfigVO(e.getLabel(), configDO.getData().get(e.getAlias()), e.getSerial()))
                .sorted(Comparator.comparingInt(BlockItemConfigVO::getSerial))
                .collect(Collectors.toList()));
    }

    @Override
    protected AnalyseType getAnalyseType() {
        return AnalyseType.BLOCK_ANALYSE;
    }

    /**
     * 文本块项配置VO
     */
    @Data
    @AllArgsConstructor
    public static class BlockItemConfigVO implements Serializable {

        private static final long serialVersionUID = 676512180525747380L;

        private String label;

        private Object value;

        private Integer serial;

    }

}
