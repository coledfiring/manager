package com.whaty.products.service.enroll.domain;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 报名组
 *
 * @author weipengsen
 */
@Data
public class EnrollGroupModel implements Serializable, Comparable<EnrollGroupModel> {

    private static final long serialVersionUID = -5646292910489295384L;

    private String name;

    private String code;

    private List<EnrollColumnModel> columns;

    public EnrollGroupModel(String code, String name) {
        this.code = code;
        this.name = name;
        this.columns = new ArrayList<>();
    }

    @Override
    public int compareTo(EnrollGroupModel o) {
        if (Objects.isNull(o)) {
            return 1;
        }
        return this.code.compareTo(o.code);
    }
}
