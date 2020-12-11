package com.whaty.framework.core.flow.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Map;

/**
 * 流程权限数据对象，DTO
 *
 * @author weipengsen
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryPriorityDTO implements Serializable {

    private static final long serialVersionUID = -339641393930094505L;

    private Integer baseId;

    private String categoryId;

    private Boolean isActive;

    private Boolean canTurn;

    public static CategoryPriorityDTO convert(Map<String, Object> origin) {
        return new CategoryPriorityDTO(((BigInteger) origin.get("id")).intValue(),
                (String) origin.get("categoryId"), "1".equals(origin.get("isActive")),
                "1".equals(origin.get("canTurn")));
    }
}
