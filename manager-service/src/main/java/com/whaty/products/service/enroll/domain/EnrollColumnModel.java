package com.whaty.products.service.enroll.domain;

import com.whaty.core.commons.datasource.MasterSlaveRoutingDataSource;
import com.whaty.domain.bean.PeStudent;
import com.whaty.framework.config.util.SiteUtil;
import com.whaty.function.Tuple;
import com.whaty.schedule.util.CommonUtils;
import com.whaty.utils.StaticBeanUtils;
import lombok.Data;
import org.springframework.util.CollectionUtils;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 报名字段
 *
 * @author weipengsen
 */
@Data
public class EnrollColumnModel implements Serializable {

    private static final long serialVersionUID = -2511639270830413696L;

    private String name;

    private String type;

    private Object value;

    private static final Field[] FIELDS;

    private static final Map<String, Tuple<Boolean, Field>> SQL_COLUMN_MAP;

    static {
        FIELDS = PeStudent.class.getDeclaredFields();
        SQL_COLUMN_MAP = Arrays.stream(FIELDS).filter(e -> !Modifier.isTransient(e.getModifiers()))
                .filter(e -> Objects.isNull(e.getAnnotation(Transient.class)))
                .filter(e -> !"id".equals(e.getName()))
                .filter(e -> !Modifier.isStatic(e.getModifiers()))
                .collect(Collectors.toMap(EnrollColumnModel::getColumn, EnrollColumnModel::predicateIsNormalColumn));
    }

    /**
     * 判断是否是普通字段
     * @param field
     * @return
     */
    private static Tuple<Boolean, Field> predicateIsNormalColumn(Field field) {
        Column column = field.getAnnotation(Column.class);
        return new Tuple<>(Objects.nonNull(column), field);
    }

    /**
     * 获取字段名
     * @param field
     * @return
     */
    private static String getColumn(Field field) {
        Column column = field.getAnnotation(Column.class);
        if (Objects.nonNull(column)) {
            return column.name();
        } else {
            return field.getAnnotation(JoinColumn.class).name();
        }
    }

    public static EnrollColumnModel convert(Map<String, Object> origin) {
        EnrollColumnModel columnModel = new EnrollColumnModel();
        columnModel.setName((String) origin.get("name"));
        ColumnType type = ColumnType.getType((String) origin.get("columnType"));
        columnModel.setType(type.getType());
        String currentSiteCode = MasterSlaveRoutingDataSource.getDbType();
        try {
            MasterSlaveRoutingDataSource.setDbType(SiteUtil.getSiteCode());
            Tuple<Boolean, Field> persistField = SQL_COLUMN_MAP.get(origin.get("dataIndex"));
            if (type == ColumnType.TEXT || type == ColumnType.SELECT) {
                if (Objects.isNull(persistField)) {
                    throw new IllegalArgumentException();
                }
                if (persistField.t0) {
                    columnModel.setValue(StaticBeanUtils.getOpenGeneralDao()
                            .getOneBySQL(String.format("select %s from pe_student where id = ?",
                                    origin.get("dataIndex")), origin.get("studentId")));
                } else {
                    Class<?> clazz = persistField.t1.getType();
                    Table table = clazz.getAnnotation(Table.class);
                    String sql = String.format("select i.name from pe_student stu " +
                            "inner join %s i on i.id = stu.%s where stu.id = ?", table.name(), origin.get("dataIndex"));
                    columnModel.setValue(StaticBeanUtils.getOpenGeneralDao().getOneBySQL(sql, origin.get("studentId")));
                }
            } else if (type == ColumnType.DATE){
                if (Objects.isNull(persistField)) {
                    throw new IllegalArgumentException();
                }
                if (persistField.t0) {
                    columnModel.setValue(StaticBeanUtils.getOpenGeneralDao()
                            .getOneBySQL("select date_format(" + origin.get("dataIndex") + ",'%Y-%m-%d') " +
                                    " from pe_student where id = ?", origin.get("studentId")));
                }
            }else {
                if ("0".equals(origin.get("isAttachFile"))) {
                    if (Objects.isNull(persistField)) {
                        throw new IllegalArgumentException();
                    }
                    String webPath = StaticBeanUtils.getOpenGeneralDao()
                            .getOneBySQL(String.format("select %s from pe_student where id = ?",
                                    origin.get("dataIndex")), origin.get("studentId"));
                    columnModel.setValue(Collections.singletonList(CommonUtils.getContextUrl() + webPath));
                } else {
                    List<String> pathList = StaticBeanUtils.getOpenGeneralDao()
                            .getBySQL("select url from attach_file where link_id = ? AND namespace = ?",
                                    origin.get("studentId"), "student-" + origin.get("dataIndex"));
                    columnModel.setValue(pathList.stream().map(e -> CommonUtils.getContextUrl() + e)
                            .collect(Collectors.toList()));
                }
            }
        } finally {
            MasterSlaveRoutingDataSource.setDbType(currentSiteCode);
        }
        return columnModel;
    }

    public boolean isEmpty() {
        if (type.equals(ColumnType.PICTURE.getType())) {
            return CollectionUtils.isEmpty((List) this.value);
        } else {
            // ColumnType.TEXT 、ColumnType.SELECT、ColumnType.DATE
            return Objects.isNull(this.value);
        }
    }
}
