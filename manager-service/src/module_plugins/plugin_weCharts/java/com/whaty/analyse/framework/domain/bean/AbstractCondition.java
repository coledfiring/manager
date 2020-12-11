package com.whaty.analyse.framework.domain.bean;

import com.whaty.core.bean.AbstractBean;
import com.whaty.core.framework.bean.EnumConst;
import lombok.Data;

import java.util.Arrays;

/**
 * 抽象的条件bean
 *
 * @author weipengsen
 */
@Data
public class AbstractCondition extends AbstractBean {

    private static final long serialVersionUID = -5245302814466104341L;

    protected String id;

    protected String label;

    protected EnumConst enumConstByFlagBlockConditionType;

    protected String sql;

    protected String dataIndex;

    protected Integer serial;

    protected String defaultSql;

    protected String reflectMethod;

    private static final String[] NEED_OPTIONS_TYPE = new String[] {"2", "4", "5"};

    public boolean needOptions() {
        return Arrays.binarySearch(NEED_OPTIONS_TYPE, this.getEnumConstByFlagBlockConditionType().getCode()) >= 0;
    }

}
