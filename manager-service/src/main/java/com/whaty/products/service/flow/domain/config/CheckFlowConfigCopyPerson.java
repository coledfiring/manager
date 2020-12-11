package com.whaty.products.service.flow.domain.config;

import com.whaty.util.CommonUtils;
import com.whaty.utils.StaticBeanUtils;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang.StringUtils;

import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 流程配置抄送人
 *
 * @author weipengsen
 */
@Data
@NoArgsConstructor
public class CheckFlowConfigCopyPerson implements Serializable {

    private static final long serialVersionUID = -5007124215700106879L;

    @NotNull
    private String id;

    private String name;

    @Transient
    private transient String groupId;

    private CheckFlowConfigCopyPerson(Map<String, Object> origin) {
        this.id = (String) origin.get("id");
        this.name = (String) origin.get("name");
    }

    /**
     * 列举抄送人
     *
     * @param configId
     * @return
     */
    public static List<CheckFlowConfigCopyPerson> listCopyPersons(String configId) {
        String copyPersonConfig = StaticBeanUtils.getGeneralDao()
                .getOneBySQL("SELECT cfcp.copy_person " +
                        "FROM check_flow_copy_person cfcp " +
                        "INNER JOIN check_flow_group cfg ON cfg.fk_copy_person_detail_id = cfcp.id " +
                        "WHERE cfg.id = ?", configId);
        if (StringUtils.isBlank(copyPersonConfig)) {
            return null;
        }
        return StaticBeanUtils.getGeneralDao()
                .getMapBySQL("select id, concat(TRUE_NAME, '(', login_id, ')') as name from pe_manager where "
                        + CommonUtils.madeSqlIn(copyPersonConfig, "id"))
                .stream().filter(Objects::nonNull).map(CheckFlowConfigCopyPerson::new)
                .collect(Collectors.toList());
    }

}