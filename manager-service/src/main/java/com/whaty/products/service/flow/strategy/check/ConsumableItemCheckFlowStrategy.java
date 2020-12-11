package com.whaty.products.service.flow.strategy.check;

import com.whaty.constant.CommonConstant;
import com.whaty.dao.GeneralDao;
import com.whaty.products.service.flow.domain.tab.AbstractBaseTab;
import com.whaty.products.service.flow.domain.tab.BaseInfoTab;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 易耗品流程策略
 *
 * @author pingzhihao
 */
@Lazy
@Component("consumableItemCheckFlowStrategy")
public class ConsumableItemCheckFlowStrategy extends AbstractCheckFlowStrategy {

    @Resource(name = CommonConstant.GENERAL_DAO_BEAN_NAME)
    private GeneralDao generalDao;

    /**
     * 更新领用状态
     *
     * @param id
     * @param statusCode
     */
    private void updateUseStatus(String id, String statusCode) {
        this.generalDao.executeBySQL("UPDATE pe_consumable pc INNER JOIN enum_const us " +
                " ON us.namespace = 'flagUseStatus' AND us.CODE = ? " +
                " SET pc.flag_use_status = us.id WHERE pc.id = ?", statusCode, id);
    }

    @Override
    public Boolean checkCanApply(String itemId) {
        return this.generalDao.checkNotEmpty("SELECT 1 FROM pe_consumable pc " +
                "INNER JOIN enum_const us ON us.id = pc.flag_use_status " +
                "WHERE us.CODE IN ('0', '3') AND pc.id = ?", itemId);
    }

    @Override
    public void applyCheck(String itemId) {
        this.updateUseStatus(itemId, "1");
    }

    @Override
    public void passCheck(String itemId) {
        this.updateUseStatus(itemId, "2");
    }

    @Override
    public void cancelCheck(String itemId) {
        this.updateUseStatus(itemId, "0");
    }

    @Override
    public void noPassCheck(String itemId) {
        this.updateUseStatus(itemId, "3");
    }

    @Override
    public String getItemName(String itemId) {
        return "易耗品领用申请";
    }

    @Override
    public List<AbstractBaseTab> getBusiness(String itemId) {
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT                                         ");
        sql.append("    fup.`NAME` AS `领用用途`,                   ");
        sql.append("    fup.code AS code,                           ");
        sql.append(" 	pc.note AS `领用备注`,                      ");
        sql.append(" 	pm1.`name` AS `申请人`,                     ");
        sql.append(" 	pc.applicant_user_tel AS `申请人电话`,      ");
        sql.append(" 	date_format(pc.applicant_time,'%Y-%m-%d') AS `申请时间`, ");
        sql.append("    pu.name AS `申请人单位`                     ");
        sql.append(" FROM                                           ");
        sql.append(" 	pe_consumable pc                                                        ");
        sql.append(" INNER JOIN enum_const fup ON fup.ID = pc.flag_use_purpose                  ");
        sql.append(" INNER JOIN pe_unit pu ON pu.id = pc.fk_unit_id                             ");
        sql.append(" INNER JOIN pe_manager pm1 ON pc.fk_applicant_user_id = pm1.fk_sso_user_id  ");
        sql.append(" where                                                                      ");
        sql.append("   pc.id = ?                                                                ");
        Map<String, Object> baseInfo = this.generalDao.getOneMapBySQL(sql.toString(), itemId);
        String code = (String) baseInfo.remove("code");
        String unit = "";
        if ("0".equals(code)) {
            //班级领用
            List<String> result = this.generalDao.getBySQL("select cl.name AS name from pe_consumable pc " +
                    " inner join pe_class cl on cl.id = pc.item_id where pc.id = ? ", itemId);
            unit = result.size() > 0 ? result.get(0) : "";
        } else if ("1".equals(code)) {
            //个人领用
            unit = (String) baseInfo.get("申请人");
        }
        baseInfo.put("领用单位", unit);
        String[] baseHeader = {"领用单位", "领用用途", "领用备注", "申请人", "申请人电话", "申请时间", "申请人单位"};
        sql.setLength(0);
        sql.append(" SELECT                                         ");
        sql.append(" concat(' 种类：',fct.name,'，名称：',pci.name, '，数量：', pcd.use_number) ");
        sql.append(" FROM                                           ");
        sql.append(" 	pe_consumable pc                                                        ");
        sql.append(" INNER JOIN pe_consumable_detail pcd ON pcd.fk_consumable_id = pc.id    ");
        sql.append(" INNER JOIN pe_consumable_item pci ON pcd.fk_consumable_item_id = pci.id    ");
        sql.append(" INNER JOIN enum_const fct on pci.flag_consumable_type = fct.id    ");
        sql.append(" where                                                                      ");
        sql.append("   pc.id = ?                                                                ");
        List<String> detailList = this.generalDao.getBySQL(sql.toString(), itemId);
        String[] detailInfoHeader = new String[detailList.size()];
        Map<String, Object> detailInfoMap = new HashMap<>(32);
        for (int i = 0; i < detailList.size(); i++) {
            detailInfoHeader[i] = "物品" + (i + 1) + ": ";
            detailInfoMap.put(detailInfoHeader[i], detailList.get(i));
        }
        return Arrays.asList(new BaseInfoTab("基础信息", baseHeader, baseInfo),
                new BaseInfoTab("申请物品详情", detailInfoHeader, detailInfoMap));
    }

    @Override
    public String getScopeId(String itemId) {
        return this.generalDao.getOneBySQL("select fk_unit_id from pe_consumable where id = ?", itemId);
    }
}
