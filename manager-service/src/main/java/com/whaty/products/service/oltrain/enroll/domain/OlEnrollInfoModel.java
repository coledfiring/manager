package com.whaty.products.service.oltrain.enroll.domain;

import com.whaty.function.Functions;
import lombok.Data;
import org.apache.commons.collections.CollectionUtils;

import java.io.Serializable;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

/**
 * 报名信息
 *
 * @author suoqiangqiang
 */
@Data
public class OlEnrollInfoModel implements Serializable {

    private static final long serialVersionUID = 8370098600584171969L;

    private String itemName;

    private String status;

    private Set<OlEnrollGroupModel> info;

    public static OlEnrollInfoModel convert(String itemName, List<Map<String, Object>> columnList) {
        OlEnrollInfoModel olEnrollInfoModel = new OlEnrollInfoModel();
        olEnrollInfoModel.setItemName(itemName);
        olEnrollInfoModel.setInfo(columnList.stream()
                .sorted(Comparator.comparingInt(e -> Integer.parseInt(String.valueOf(e.get("groupCode")))))
                .map(e -> new OlEnrollGroupModel((String) e.get("groupCode"), (String) e.get("groupName")))
                .collect(Collectors.toCollection(TreeSet::new)));
        Map<String, OlEnrollGroupModel> groupMap = olEnrollInfoModel.getInfo().stream()
                .collect(Functions.map(OlEnrollGroupModel::getName));
        columnList.stream().sorted(Comparator.comparingInt(e -> (Integer) e.get("serialNumber")))
                .forEach(e -> {
                    OlEnrollColumnModel columnModel = OlEnrollColumnModel.convert(e);
                    if (!columnModel.isEmpty()) {
                        groupMap.get(e.get("groupName")).getColumns().add(columnModel);
                    }
                });
        olEnrollInfoModel.setInfo(olEnrollInfoModel.getInfo().stream()
                .filter(e -> CollectionUtils.isNotEmpty(e.getColumns()))
                .collect(Collectors.toCollection(TreeSet::new)));
        return olEnrollInfoModel;
    }
}
