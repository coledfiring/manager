package com.whaty.framework.core.flow.service.impl;

import com.alibaba.fastjson.JSON;
import com.whaty.constant.CommonConstant;
import com.whaty.core.framework.user.service.UserService;
import com.whaty.core.framework.util.BeanNames;
import com.whaty.dao.GeneralDao;
import com.whaty.domain.bean.FlowConfig;
import com.whaty.framework.asserts.TycjParameterAssert;
import com.whaty.framework.config.util.SiteUtil;
import com.whaty.framework.core.flow.domain.Flow;
import com.whaty.framework.core.flow.domain.CategoryPriorityDTO;
import com.whaty.framework.core.flow.service.FlowConfigService;
import com.whaty.framework.exception.ServiceException;
import com.whaty.util.CommonUtils;
import com.whaty.utils.HibernatePluginsUtil;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 流程配置服务类
 *
 * @author weipengsen
 */
@Lazy
@Service("flowConfigService")
public class FlowConfigServiceImpl implements FlowConfigService {

    @Resource(name = CommonConstant.GENERAL_DAO_BEAN_NAME)
    private GeneralDao generalDao;

    @Resource(name = BeanNames.USER_SERVICE)
    private UserService userService;

    @Override
    public List<Map<String, Object>> listFlowConfig() {
        return this.generalDao.getMapBySQL("select id, name from flow_config");
    }

    @Override
    public Flow getFlowConfig(String id) {
        TycjParameterAssert.isAllNotBlank(id);
        Map<String, Object> flowConfig = this.generalDao
                .getOneMapBySQL("select id, name, flow_config as flowConfig from flow_config where id = ?", id);
        Flow flow = JSON.parseObject((String) flowConfig.get("flowConfig"), Flow.class);
        flow.setId(String.valueOf(flowConfig.get("id")));
        flow.setName((String) flowConfig.get("name"));
        return flow;
    }

    @Override
    public Map<String, Map> listCategoryAndMenu() {
        // 获取菜单节点数据
        List<Map<String, Object>> categories = this.generalDao
                .getMapBySQL("SELECT min(id) as id, NAME as name FROM pe_base_category GROUP BY url,fk_grid_id");
        StringBuilder sql = new StringBuilder();
        // 获取按钮数据
        sql.delete(0, sql.length());
        sql.append(" SELECT                                                             ");
        sql.append(" 	me.id as id,                                                    ");
        sql.append(" 	me.text as name,                                                ");
        sql.append(" 	cate.name as categoryName,                                      ");
        sql.append(" 	cate.id as categoryId                                           ");
        sql.append(" FROM                                                               ");
        sql.append(" 	grid_menu_config me                                             ");
        sql.append(" inner join grid_basic_config gr on gr.id = me.fk_grid_id           ");
        sql.append(" inner join pe_base_category cate ON cate.fk_grid_id = gr.id        ");
        sql.append(" GROUP BY                                                           ");
        sql.append(" 	me.id,                                                          ");
        sql.append(" 	flag_menu_type,                                                 ");
        sql.append(" 	text                                                            ");
        List<Map<String, Object>> menus = this.generalDao.getMapBySQL(sql.toString());
        Map<String, Map> resultData = new HashMap<>(4);
        resultData.put("categories", categories.stream()
                .collect(Collectors.toMap(e -> String.valueOf(e.get("id")), e -> e)));
        resultData.put("menus", menus.stream()
                .collect(Collectors.toMap(e -> String.valueOf(e.get("id")), e -> e)));
        return resultData;
    }

    @Override
    public Flow saveFlowConfig(Flow flow) {
        TycjParameterAssert.validatePass(flow);
        FlowConfig config = flow.convertToDo();
        this.generalDao.save(config);
        this.generalDao.flush();
        flow.setId(config.getId());
        return flow;
    }

    @Override
    public void deleteFlowConfig(String id) {
        TycjParameterAssert.isAllNotBlank(id);
        if (HibernatePluginsUtil.validateReferencingCurrentSession(FlowConfig.class, id)) {
            throw new ServiceException("存在引用数据，无法删除");
        }
        this.generalDao.deleteByIds(FlowConfig.class, Collections.singletonList(id));
    }

    @Override
    public Flow getShowFlowConfig(String flowId) {
        Flow flow = this.getFlowConfig(flowId);
        return flow.handlePriority(this.listPriority(flow.listCategoryIds()));
    }

    /**
     * 列举权限信息
     * @param categoryIds
     * @return
     */
    private List<CategoryPriorityDTO> listPriority(Set<Integer> categoryIds) {
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT                                       ");
        sql.append(" 	base.id AS id,                            ");
        sql.append(" 	if(base.router_name is not null,base.router_name,cate.id) AS categoryId,");
        sql.append(" 	cate.isActive AS isActive,                ");
        sql.append(" 	IF (prr.id IS NULL, '0', '1') AS canTurn  ");
        sql.append(" FROM                                         ");
        sql.append(" 	pe_base_category base                     ");
        sql.append(" INNER JOIN pe_pri_category cate ON cate.fk_base_category_id = base.id ");
        sql.append(" LEFT JOIN pr_role_menu prr ON prr.fk_menu_id = cate.ID ");
        sql.append(" AND prr.fk_role_id = ?                       ");
        sql.append(" WHERE                                        ");
        sql.append(" 	cate.fk_web_site_id = ?                   ");
        sql.append(" AND " + CommonUtils.madeSqlIn(categoryIds, "base.id"));
        return this.generalDao.getMapBySQL(sql.toString(),
                this.userService.getCurrentUser().getRole().getId(), SiteUtil.getSiteId())
                .stream().map(CategoryPriorityDTO::convert).collect(Collectors.toList());
    }

}
