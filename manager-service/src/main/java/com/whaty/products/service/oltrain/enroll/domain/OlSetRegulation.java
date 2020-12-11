package com.whaty.products.service.oltrain.enroll.domain;

import com.whaty.function.Functions;
import com.whaty.utils.StaticBeanUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

/**
 * 设置规则VO
 *
 * @author suoqiangqiang
 */
@Data
public class OlSetRegulation implements Serializable {

    private static final long serialVersionUID = -2711890380793613102L;

    private String name;

    private Set<OlSetRegulationColumnGroup> groups;

    /**
     * 通过id构建规则模型
     * @param id
     * @return
     */
    public static OlSetRegulation buildById(String id) {
        OlSetRegulation regulation = new OlSetRegulation();
        regulation.setName(StaticBeanUtils.getGeneralDao()
                .getOneBySQL("select name from enroll_column_regulation where id = ?", id));
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT                                                                   ");
        sql.append("    ec.id as columnId,                                                    ");
        sql.append("    ec.name as name,                                                      ");
        sql.append("    g.name as groupName,                                                  ");
        sql.append("    g.code as groupNumber,                                                ");
        sql.append("    de.is_active as isActive,                                             ");
        sql.append("    de.is_required as isRequired,                                         ");
        sql.append("    de.serial_number as serialNumber                                      ");
        sql.append(" FROM                                                                     ");
        sql.append("    ol_enroll_column ec                                                   ");
        sql.append(" inner join enum_const g on g.id = ec.flag_enroll_column_group            ");
        sql.append(" left join ol_enroll_column_regulation_detail de on de.fk_column_id = ec.id  ");
        sql.append(" AND de.fk_regulation_id = ?                                              ");
        regulation.setGroups(OlSetRegulationColumnGroup
                .convert(StaticBeanUtils.getGeneralDao().getMapBySQL(sql.toString(), id)));
        return regulation;
    }

    /**
     * 设置规则项组VO
     *
     * @author suoqiangqiang
     */
    @Data
    @NoArgsConstructor
    private static class OlSetRegulationColumnGroup implements Serializable, Comparable<OlSetRegulationColumnGroup> {

        private static final long serialVersionUID = -7974843825227964719L;

        private String name;

        private Integer serialNumber;

        private Set<OlSetRegulationColumn> columns;

        public OlSetRegulationColumnGroup(String name, Integer serialNumber) {
            this.name = name;
            this.serialNumber = serialNumber;
            this.columns = new TreeSet<>();
        }

        public static Set<OlSetRegulationColumnGroup> convert(List<Map<String, Object>> origin) {
            Set<OlSetRegulationColumnGroup> groups = origin.stream()
                    .map(e -> new OlSetRegulationColumnGroup((String) e.get("groupName"),
                            Integer.parseInt(String.valueOf(e.get("groupNumber")))))
                    .collect(Collectors.toCollection(TreeSet::new));
            Map<String, OlSetRegulationColumnGroup> groupMap = groups.stream()
                    .collect(Functions.map(OlSetRegulationColumnGroup::getName));
            for (Map<String, Object> elem : origin) {
                groupMap.get(elem.get("groupName")).getColumns().add(OlSetRegulationColumn.convert(elem));
            }
            return groups;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            if (!super.equals(o)) {
                return false;
            }
            OlSetRegulationColumnGroup group = (OlSetRegulationColumnGroup) o;
            return Objects.equals(name, group.name) &&
                    Objects.equals(serialNumber, group.serialNumber);
        }

        @Override
        public int hashCode() {
            return Objects.hash(super.hashCode(), name, serialNumber);
        }

        @Override
        public int compareTo(OlSetRegulationColumnGroup o) {
            if (Objects.isNull(o)) {
                return 1;
            }
            return Integer.compare(this.serialNumber, o.serialNumber);
        }
    }

    /**
     * 设置规则项
     *
     * @author suoqiangqiang
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    private static class OlSetRegulationColumn implements Serializable, Comparable<OlSetRegulationColumn> {

        private static final long serialVersionUID = 4395447112010677333L;

        private String columnId;

        private String name;

        private Boolean isActive;

        private Boolean isRequired;

        private Integer serialNumber;

        public static OlSetRegulationColumn convert(Map<String, Object> origin) {
            return new OlSetRegulationColumn((String) origin.get("columnId"), (String) origin.get("name"),
                    convertToBoolean(origin.get("isActive")), convertToBoolean(origin.get("isRequired")),
                    (Integer) origin.get("serialNumber"));
        }

        private static Boolean convertToBoolean(Object origin) {
            return Objects.nonNull(origin) && ("1".equals(origin) || '1' == (Character) origin);
        }

        @Override
        public int compareTo(OlSetRegulationColumn o) {
            if (Objects.isNull(o) || Objects.isNull(this.serialNumber)) {
                return 1;
            }
            if (Objects.isNull(o.serialNumber)) {
                return 0;
            }
            return Integer.compare(this.serialNumber, o.serialNumber);
        }
    }

}
