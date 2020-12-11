package com.whaty.products.service.oltrain.enroll.domain;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 报名组
 *
 * @author suoqiangqiang
 */
@Data
public class OlEnrollGroupModel implements Serializable, Comparable<OlEnrollGroupModel> {

    private static final long serialVersionUID = 2852862075935446360L;

    private String name;

    private String code;

    private List<OlEnrollColumnModel> columns;

    public OlEnrollGroupModel(String code, String name) {
        this.code = code;
        this.name = name;
        this.columns = new ArrayList<>();
    }

    @Override
    public int compareTo(OlEnrollGroupModel o) {
        if (Objects.isNull(o)) {
            return 1;
        }
        return this.code.compareTo(o.code);
    }
}
