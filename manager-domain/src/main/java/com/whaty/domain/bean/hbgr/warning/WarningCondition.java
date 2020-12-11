package com.whaty.domain.bean.hbgr.warning;

import com.alibaba.fastjson.JSON;
import lombok.Data;
import java.util.Map;

/**
 * 报警预警条件
 *
 * @author weipengsen
 */
@Data
public class WarningCondition {
    /**
     * 一网1#水泵
     */
    private Integer w1b1gz;
    /**
     * 一网2#泵
     */
    private Integer w1b2gz;
    /**
     * 一网3#泵
     */
    private Integer w1b3gz;
    /**
     * 一网4#泵
     */
    private Integer w1b4gz;
    /**
     * 二网空调1#泵
     */
    private Integer w2b1gz;
    /**
     * 二网空调2#泵
     */
    private Integer w2b2gz;
    /**
     * 二网空调3#泵
     */
    private Integer w2b3gz;
    /**
     * 二网空调4#泵
     */
    private Integer w2b4gz;
    /**
     * 二网5#地暖泵
     */
    private Integer w2b5gz;
    /**
     * 二网6#地暖泵
     */
    private Integer w2b6gz;
    /**
     * 1#锅炉故障
     */
    private Integer gl1gz;
    /**
     * 2#锅炉故障
     */
    private Integer gl2gz;
    /**
     * 3#锅炉故障
     */
    private Integer gl3gz;
    /**
     * 4#锅炉故障
     */
    private Integer gl4gz;
    /**
     * 5#锅炉故障
     */
    private Integer gl5gz;
    /**
     * 6#锅炉故障
     */
    private Integer gl6gz;
    /**
     * 7#锅炉故障
     */
    private Integer gl7gz;

    public WarningCondition() {
    }

    /**
     * 从字符串类型的xml构造对象
     *
     * @author 杨子尧
     */
    public WarningCondition(String json) {
        try {
            Map<String, Object> map = (Map<String, Object>) JSON.parse(json);
            if (map.get(WarningConstant.W1_B1_GZ) != null) {
                w1b1gz = Integer.valueOf((String)map.get(WarningConstant.W1_B1_GZ));
            }
            if (map.get(WarningConstant.W1_B2_GZ) != null) {
                w1b2gz = Integer.valueOf((String)map.get(WarningConstant.W1_B2_GZ));
            }
            if (map.get(WarningConstant.W1_B3_GZ) != null) {
                w1b3gz = Integer.valueOf((String)map.get(WarningConstant.W1_B3_GZ));
            }
            if (map.get(WarningConstant.W1_B4_GZ) != null) {
                w1b4gz = Integer.valueOf((String)map.get(WarningConstant.W1_B4_GZ));
            }
            if (map.get(WarningConstant.W2_B1_GZ) != null) {
                w2b1gz = Integer.valueOf((String)map.get(WarningConstant.W2_B1_GZ));
            }
            if (map.get(WarningConstant.W2_B2_GZ) != null) {
                w2b2gz = Integer.valueOf((String)map.get(WarningConstant.W2_B2_GZ));
            }
            if (map.get(WarningConstant.W2_B3_GZ) != null) {
                w2b3gz = Integer.valueOf((String)map.get(WarningConstant.W2_B3_GZ));
            }
            if (map.get(WarningConstant.W2_B4_GZ) != null) {
                w2b4gz = Integer.valueOf((String)map.get(WarningConstant.W2_B4_GZ));
            }
            if (map.get(WarningConstant.W2_B5_GZ) != null) {
                w2b5gz = Integer.valueOf((String)map.get(WarningConstant.W2_B5_GZ));
            }
            if (map.get(WarningConstant.W2_B6_GZ) != null) {
                w2b6gz = Integer.valueOf((String)map.get(WarningConstant.W2_B6_GZ));
            }
            if (map.get(WarningConstant.GL1_GZ) != null) {
                gl1gz = Integer.valueOf((String)map.get(WarningConstant.GL1_GZ));
            }
            if (map.get(WarningConstant.GL2_GZ) != null) {
                gl2gz = Integer.valueOf((String)map.get(WarningConstant.GL2_GZ));
            }
            if (map.get(WarningConstant.GL3_GZ) != null) {
                gl3gz = Integer.valueOf((String)map.get(WarningConstant.GL3_GZ));
            }
            if (map.get(WarningConstant.GL4_GZ) != null) {
                gl4gz = Integer.valueOf((String)map.get(WarningConstant.GL4_GZ));
            }
            if (map.get(WarningConstant.GL5_GZ) != null) {
                gl5gz = Integer.valueOf((String)map.get(WarningConstant.GL5_GZ));
            }
            if (map.get(WarningConstant.GL6_GZ) != null) {
                gl6gz = Integer.valueOf((String)map.get(WarningConstant.GL6_GZ));
            }
            if (map.get(WarningConstant.GL7_GZ) != null) {
                gl7gz = Integer.valueOf((String)map.get(WarningConstant.GL7_GZ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
