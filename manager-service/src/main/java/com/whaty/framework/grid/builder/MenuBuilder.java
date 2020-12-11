package com.whaty.framework.grid.builder;

import com.whaty.core.framework.grid.bean.menu.AbstractGridMenu;
import com.whaty.core.framework.grid.bean.menu.BackGridMenu;

/**
 * 菜单构造器
 *
 * @author weipengsen
 */
public class MenuBuilder {

    /**
     * 构建返回按钮
     * @return
     */
    public static AbstractGridMenu buildBackMenu() {
        AbstractGridMenu target = new BackGridMenu();
        target.setText("返回");
        target.setShowType("top");
        target.setMustSelectRow(false);
        return target;
    }

}
