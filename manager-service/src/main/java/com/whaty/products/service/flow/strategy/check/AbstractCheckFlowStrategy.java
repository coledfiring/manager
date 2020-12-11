package com.whaty.products.service.flow.strategy.check;

import com.whaty.products.service.flow.domain.tab.AbstractBaseTab;

import java.util.List;

/**
 * 抽象的审核流程策略
 *
 * @author weipengsen
 */
public abstract class AbstractCheckFlowStrategy {

    /**
     * 检查是否可申请
     * @param itemId
     * @return
     */
    public abstract Boolean checkCanApply(String itemId);

    /**
     * 发起审核
     * @param itemId
     * @return
     */
    public abstract void applyCheck(String itemId);

    /**
     * 审核通过
     * @param itemId
     * @return
     */
    public abstract void passCheck(String itemId);

    /**
     * 审核驳回
     * @param itemId
     * @return
     */
    public abstract void noPassCheck(String itemId);

    /**
     * 获取项目名称
     * @param itemId
     * @return
     */
    public abstract String getItemName(String itemId);

    /**
     * 获取业务配置
     * @param itemId
     * @return
     */
    public abstract List<AbstractBaseTab> getBusiness(String itemId);

    /**
     * 获取数据范围id
     *
     * @param itemId
     * @return
     */
    public abstract String getScopeId(String itemId);

    /**
     * 撤回申请
     * @param itemId
     */
    public abstract void cancelCheck(String itemId);
}
