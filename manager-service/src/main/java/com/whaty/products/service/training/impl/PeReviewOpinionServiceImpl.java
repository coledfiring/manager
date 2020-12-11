package com.whaty.products.service.training.impl;

import com.whaty.constant.CommonConstant;
import com.whaty.core.commons.exception.EntityException;
import com.whaty.core.framework.grid.bean.GridConfig;
import com.whaty.dao.GeneralDao;
import com.whaty.domain.bean.PeReview;
import com.whaty.domain.bean.PeReviewOpinion;
import com.whaty.framework.grid.builder.MenuBuilder;
import com.whaty.framework.grid.twolevellist.service.impl.AbstractTwoLevelListGridServiceImpl;
import com.whaty.utils.UserUtils;
import org.apache.commons.collections.MapUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 附件管理服务类
 *
 * @author weipengsen
 */
@Lazy
@Service("peReviewOpinionService")
public class PeReviewOpinionServiceImpl extends AbstractTwoLevelListGridServiceImpl<PeReviewOpinion> {

    @Resource(name = CommonConstant.GENERAL_DAO_BEAN_NAME)
    private GeneralDao generalDao;

    @Override
    public void initGrid(GridConfig gridConfig, Map<String, Object> mapParam) {
        if (MapUtils.isNotEmpty(mapParam) && (mapParam.get("isShowBack") == null ||
                Boolean.valueOf(mapParam.get("isShowBack").toString()))) {
            gridConfig.getGridMenuList().add(0, MenuBuilder.buildBackMenu());
        }
        if (gridConfig.gridConfigSource() != null) {
            gridConfig.gridConfigSource()
                    .setSql(String.format(gridConfig.gridConfigSource().getSql(),
                            mapParam.get(CommonConstant.PARAM_PARENT_ID)));
        }
    }

    @Override
    public void checkBeforeAdd(PeReviewOpinion bean, Map<String, Object> params) throws EntityException {
        bean.setPeReview(this.generalDao.getById(PeReview.class, (String) params.get(CommonConstant.PARAM_PARENT_ID)));
        bean.setReviewUser(UserUtils.getCurrentUser());
        bean.setReviewTime(new Date());
    }

    @Override
    public void checkBeforeDelete(List idList) throws EntityException {
        super.checkBeforeDelete(idList);
    }

    @Override
    protected String getParentIdSearchParamName() {
        return "reviewId";
    }

    @Override
    protected String getParentIdBeanPropertyName() {
        return "reviewId";
    }
}
