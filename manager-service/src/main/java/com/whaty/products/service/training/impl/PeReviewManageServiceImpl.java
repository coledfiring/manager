package com.whaty.products.service.training.impl;

import com.whaty.constant.CommonConstant;
import com.whaty.constant.EnumConstConstants;
import com.whaty.core.commons.exception.EntityException;
import com.whaty.dao.GeneralDao;
import com.whaty.domain.bean.PeReview;
import com.whaty.framework.asserts.TycjParameterAssert;
import com.whaty.framework.config.util.SiteUtil;
import com.whaty.framework.grid.adapter.service.impl.TycjGridServiceAdapter;
import com.whaty.util.CollectionDivider;
import com.whaty.util.CommonUtils;
import com.whaty.utils.UserUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

/**
 * 项目评审管理
 *
 * @author weipengsen
 */
@Lazy
@Service("peReviewManageService")
public class PeReviewManageServiceImpl extends TycjGridServiceAdapter<PeReview> {

    @Resource(name = CommonConstant.GENERAL_DAO_BEAN_NAME)
    private GeneralDao generalDao;


    /**
     * 获取系统用户
     *
     * @return
     */
    public List<Map<String, Object>> listManager() {
      return generalDao.getMapBySQL("select id, name from pe_manager " +
                "where name is not null AND name <> '' AND site_code = ?", SiteUtil.getSiteCode());

    }

    /**
     * 添加项目评审
     *
     * @param peReview
     */
    public void addReview(PeReview peReview) {
        TycjParameterAssert.isAllNotNull(peReview);
        peReview.setEnumConstByFlagProjectStatus(this.generalDao
                .getEnumConstByNamespaceCode(EnumConstConstants.ENUM_CONST_NAMESPACE_FLAG_PROJECT_STATUS, "1"));
        peReview.setCreateTime(new Date());
        peReview.setUnit(UserUtils.getCurrentUnit());
        peReview.setCreateUser(UserUtils.getCurrentUser());
        peReview.setSiteCode(SiteUtil.getSiteCode());
        this.generalDao.save(peReview);
        this.generalDao.flush();
        Stream.of(peReview.getReviewExpert().split(CommonConstant.SPLIT_ID_SIGN)).forEach(x ->
                this.generalDao.executeBySQL("INSERT INTO pri_review_manager (fk_review_id, fk_manager_id, role) " +
                        "VALUES (?, ?, ?)", peReview.getId(), x, "2"));
        Stream.of(peReview.getPrincipal().split(CommonConstant.SPLIT_ID_SIGN)).forEach(x ->
                this.generalDao.executeBySQL("INSERT INTO pri_review_manager (fk_review_id, fk_manager_id, role) " +
                        "VALUES (?, ?, ?)", peReview.getId(), x, "1"));
    }

    /**
     * 修改项目评审
     *
     * @param peReview
     */
    public void updateReview(PeReview peReview) {
        TycjParameterAssert.isAllNotNull(peReview);
        this.generalDao.executeBySQL("UPDATE pe_review SET name = ?, background = ?, review_requirement = ?," +
                        " note = ?, opinion = ? where id = ?", peReview.getName(), peReview.getBackground(),
                peReview.getReviewRequirement(), peReview.getNote(), peReview.getOpinion(), peReview.getId());
        updateManager(peReview.getId(), "1",
                Arrays.asList( peReview.getPrincipal().split(CommonConstant.SPLIT_ID_SIGN)));
        updateManager(peReview.getId(), "2",
                Arrays.asList( peReview.getReviewExpert().split(CommonConstant.SPLIT_ID_SIGN)));
    }

    /**
     * 更新项目评审中的管理员
     *
     * @param id
     * @param role
     * @param userList
     */
    private void updateManager (String id, String role, List<String> userList) {
        TycjParameterAssert.isAllNotNull(id, role, userList);
        List<String> listManager = this.generalDao.getBySQL("select fk_manager_id from pri_review_manager" +
                " where fk_review_id = ? AND role = ?", id, role);
        CollectionDivider<String> divide = new CollectionDivider<>(listManager, userList);
        divide.getAdd().forEach(x ->
                    this.generalDao.executeBySQL("INSERT INTO pri_review_manager (fk_review_id, fk_manager_id, role) " +
                            "VALUES (?, ?, ?)", id, x, role));
        this.generalDao.executeBySQL("delete from pri_review_manager where fk_review_id = ? AND"
                    + CommonUtils.madeSqlIn(divide.getDelete(), "fk_manager_id"), id);
    }

    @Override
    public void checkBeforeDelete(List idList) throws EntityException {
        this.generalDao.executeBySQL("delete from pri_review_manager where "
                + CommonUtils.madeSqlIn(idList, "fk_review_id"));
        this.generalDao.executeBySQL("delete from pe_review_opinion where " +
                CommonUtils.madeSqlIn(idList, "fk_review_id"));
    }

    /**
     * 获取项目评审数据
     *
     * @param id
     */
    public PeReview getReviewData(String id) {
        TycjParameterAssert.isAllNotNull(id);
        PeReview peReview = this.generalDao.getById(PeReview.class, id);
        peReview.setReviewExpertList(this.generalDao.getBySQL("SELECT pm.id AS id" +
                " FROM pri_review_manager prm " +
                "INNER JOIN pe_review  pr ON pr.id = prm.fk_review_id " +
                "INNER JOIN pe_manager pm ON pm.id = prm.fk_manager_id WHERE pr.id = ? AND prm.role = ?", id, "2"));
        peReview.setPrincipalList(this.generalDao.getBySQL("SELECT pm.id AS id" +
                " FROM pri_review_manager prm " +
                "INNER JOIN pe_review  pr ON pr.id = prm.fk_review_id " +
                "INNER JOIN pe_manager pm ON pm.id = prm.fk_manager_id WHERE pr.id = ? AND prm.role = ?", id, "1"));
        return peReview;
    }

    /**
     * 设置项目评审状态
     *
     * @param ids
     * @param projectStatus
     */
    public void setProjectStatus(String ids, String projectStatus) {
        TycjParameterAssert.isAllNotNull(ids, projectStatus);
        this.generalDao.executeBySQL("UPDATE pe_review SET flag_project_status = ?  where " +
                CommonUtils.madeSqlIn(ids.split(CommonConstant.SPLIT_ID_SIGN), "id"), projectStatus);
    }
}