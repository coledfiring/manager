package com.whaty.products.service.enroll.domain;

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
 * @author weipengsen
 */
@Data
public class EnrollInfoModel implements Serializable {

    private static final long serialVersionUID = 1427571381808905688L;

    private String itemName;

    private String status;

    private Set<EnrollGroupModel> info;

    public static EnrollInfoModel convert(String itemName, List<Map<String, Object>> columnList) {
        EnrollInfoModel enrollInfoModel = new EnrollInfoModel();
        enrollInfoModel.setItemName(itemName);
        enrollInfoModel.setInfo(columnList.stream()
                .sorted(Comparator.comparingInt(e -> Integer.parseInt(String.valueOf(e.get("groupCode")))))
                .map(e -> new EnrollGroupModel((String) e.get("groupCode"), (String) e.get("groupName")))
                .collect(Collectors.toCollection(TreeSet::new)));
        Map<String, EnrollGroupModel> groupMap = enrollInfoModel.getInfo().stream()
                .collect(Functions.map(EnrollGroupModel::getName));
        columnList.stream().sorted(Comparator.comparingInt(e -> (Integer) e.get("serialNumber")))
                .forEach(e -> {
                    EnrollColumnModel columnModel = EnrollColumnModel.convert(e);
                    if (!columnModel.isEmpty()) {
                        groupMap.get(e.get("groupName")).getColumns().add(columnModel);
                    }
                });
        enrollInfoModel.setInfo(enrollInfoModel.getInfo().stream()
                .filter(e -> CollectionUtils.isNotEmpty(e.getColumns()))
                .collect(Collectors.toCollection(TreeSet::new)));
        return enrollInfoModel;
    }
}
