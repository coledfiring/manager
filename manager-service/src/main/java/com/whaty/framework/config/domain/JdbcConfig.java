package com.whaty.framework.config.domain;

import com.whaty.framework.annotation.ExcludeField;
import com.whaty.framework.annotation.FieldOrder;
import com.whaty.util.CommonUtils;
import lombok.Data;
import org.apache.commons.lang.StringUtils;

import javax.sql.DataSource;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.lang.reflect.InvocationTargetException;

/**
 * jdbc配置
 *
 * @author weipengsen
 */
@Data
public class JdbcConfig implements IConfig {

    private static final long serialVersionUID = -8631247391334862369L;
    @NotNull
    @Pattern(regexp = "^jdbc:mysql://.*$")
    private String url;
    @NotNull
    private String username;
    @NotNull
    private String password;
    @NotNull
    @ExcludeField
    @Pattern(regexp = "^[a-zA-Z0-9]+(\\.[a-zA-Z0-9]+)+$")
    private String driverName;
    @NotNull
    private Integer maxActive;
    @NotNull
    private Integer initialSize;
    @NotNull
    private Integer minIdle;
    @NotNull
    private Long maxWait;
    @NotNull
    private Integer maxOpenPreparedStatements;
    @NotNull
    private String validationQuery;
    @NotNull
    private Boolean testWhileIdle;
    @NotNull
    private Long timeBetweenEvictionRunsMillis;
    @NotNull
    private Boolean testOnBorrow;
    @NotNull
    @FieldOrder(Integer.MIN_VALUE)
    private Long minEvictableIdleTimeMillis;
    @NotNull
    private Long maxEvictableIdleTimeMillis;
    @NotNull
    private Boolean useUnfairLock;
    @NotNull
    @Pattern(regexp = "^[a-z0-9A-Z]+(,[a-z0-9A-Z]+)*$")
    private String filters;
    @ExcludeField
    private String initMethod;
    @ExcludeField
    private String destroyMethod;
    @ExcludeField
    private DataSource dataSource;

    /**
     * 构建数据源
     * @return
     * @throws ClassNotFoundException
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    public DataSource buildDataSource() throws ClassNotFoundException, NoSuchMethodException,
            InvocationTargetException, IllegalAccessException, InstantiationException {
        this.dataSource = CommonUtils.convertBean(this, (Class<DataSource>) Class.forName(this.getDriverName()));
        this.initDataSource();
        return dataSource;
    }

    /**
     * 初始化数据源
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    public void initDataSource() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        if (StringUtils.isNotBlank(this.getInitMethod())) {
            dataSource.getClass().getMethod(this.getInitMethod()).invoke(dataSource);
        }
    }

    /**
     * 销毁数据源
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    public void destroyDataSource() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        if (StringUtils.isNotBlank(this.getDestroyMethod())) {
            dataSource.getClass().getMethod(this.getDestroyMethod()).invoke(dataSource);
        }
    }

}
