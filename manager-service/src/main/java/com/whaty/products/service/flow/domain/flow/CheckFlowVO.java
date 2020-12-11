package com.whaty.products.service.flow.domain.flow;

import com.whaty.constant.EnumConstConstants;
import com.whaty.utils.StaticBeanUtils;
import lombok.Data;
import org.apache.commons.collections.CollectionUtils;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 审批流程vo
 *
 * @author weipengsen
 */
@Data
public class CheckFlowVO implements Serializable {

    private static final long serialVersionUID = 6097443447797668836L;

    private List<CheckFlowAuditorNodeVO> auditors;

    private List<CheckFlowCopyPersonVO> copyPersons;

    public CheckFlowVO(List<CheckFlowAuditorNodeVO> auditors, List<CheckFlowCopyPersonVO> copyPersons) {
        this.auditors = auditors;
        this.copyPersons = copyPersons;
    }

    /**
     * 将do转换成vo
     * @param checkFlowDO
     * @return
     */
    public static CheckFlowVO convertVo(CheckFlowDTO checkFlowDO) {
        List<CheckFlowAuditorNodeVO> nodeVOList = checkFlowDO.getCheckFlowAuditors().entrySet().stream()
                .map(e -> CheckFlowAuditorNodeVO.convertVO(e.getValue())).collect(Collectors.toList());
        List<CheckFlowCopyPersonVO> copyPersonVOS = null;
        if (CollectionUtils.isNotEmpty(checkFlowDO.getCopyPersons())) {
            copyPersonVOS = checkFlowDO.getCopyPersons().stream().map(CheckFlowCopyPersonVO::new)
                    .collect(Collectors.toList());
        }
        return new CheckFlowVO(nodeVOList, copyPersonVOS);
    }

    @Data
    private static class CheckFlowAuditorNodeVO {

        private String id;

        private List<CheckFlowAuditorVO> auditors;

        private String checkType;

        private String auditorType;

        private Boolean isChecked;

        private String applyStatus;

        private String checkedDate;

        private String note;

        public static CheckFlowAuditorNodeVO convertVO(CheckFlowDTO.CheckFlowAuditorNodeDTO auditorDO) {
            CheckFlowAuditorNodeVO nodeVO = new CheckFlowAuditorNodeVO();
            nodeVO.setAuditorType(auditorDO.getAuditorType());
            nodeVO.setCheckType(StaticBeanUtils.getGeneralDao()
                    .getEnumConstByNamespaceCode(EnumConstConstants.ENUM_CONST_NAMESPACE_CHECK_TYPE,
                            auditorDO.getCheckType()).getName());
            nodeVO.setId(auditorDO.getId());
            nodeVO.setApplyStatus(auditorDO.getApplyStatus());
            nodeVO.setCheckedDate(auditorDO.getCheckedDate());
            nodeVO.setIsChecked(auditorDO.getIsChecked());
            nodeVO.setNote(auditorDO.getNote());
            nodeVO.setAuditors(auditorDO.getAuditors().stream()
                    .map(e -> new CheckFlowAuditorVO(e, auditorDO.getAuditorType()))
                    .collect(Collectors.toList()));
            return nodeVO;
        }
    }

    @Data
    static class CheckFlowAuditorVO implements Serializable {

        private static final long serialVersionUID = -2943001284501863579L;

        private String id;

        private String name;

        private String checkOperate;

        private String checkedDate;

        private String note;

        public CheckFlowAuditorVO(CheckFlowDTO.CheckFlowAuditorDTO checkFlowAuditorDO, String auditorType) {
            this.id = checkFlowAuditorDO.getId();
            this.checkOperate = checkFlowAuditorDO.getCheckOperate();
            this.checkedDate = checkFlowAuditorDO.getCheckedDate();
            this.note = checkFlowAuditorDO.getNote();
            if ("1".equals(auditorType)) {
                this.name = StaticBeanUtils.getGeneralDao()
                        .getOneBySQL("select name from pe_pri_role where id = ?", id);
            } else {
                this.name = StaticBeanUtils.getGeneralDao()
                        .getOneBySQL("select true_name from pe_manager where id = ?", id);
            }
        }
    }

    @Data
    static class CheckFlowCopyPersonVO implements Serializable {

        private static final long serialVersionUID = -3037612582966354217L;

        private String id;

        private String name;

        public CheckFlowCopyPersonVO(String id) {
            this.id = id;
            this.name = StaticBeanUtils.getGeneralDao()
                    .getOneBySQL("select true_name from pe_manager where id = ?", id);
        }
    }

}
