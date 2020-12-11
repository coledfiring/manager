package com.whaty.products.service.flow.helper;

import com.whaty.common.string.StringUtils;
import com.whaty.framework.config.util.SiteUtil;
import com.whaty.framework.idocv.WhatyIdocvSdk;
import com.whaty.products.service.flow.domain.tab.AbstractBaseTab;
import com.whaty.products.service.flow.domain.tab.FileListTab;
import com.whaty.utils.StaticBeanUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * tab辅助类 {@link AbstractBaseTab}
 *
 * @author pingzhihao
 *
 */
public class TabHelper {

    /**
     * FileListTab建造器
     * 根据attach_file表中link_id 和namespace 以及当前站点查询数据
     * 返回attach_file表中id，name，idocv_uuid,以及通过文档服务器转化的url字段值
     *
     * @param title     tab标题
     * @param linkId    attach_file表link_id
     * @param namespace attach_file表namespace
     * @return
     */
    public static FileListTab buildFileListTab(String title, String linkId, String namespace) {
        List<Map<String, Object>> result = StaticBeanUtils.getGeneralDao().getMapBySQL(
                " select id as id, name as `name`, idocv_uuid as uuid from attach_file " +
                        " where link_id = ? and namespace = ? and site_code = ? ",
                linkId, namespace, SiteUtil.getSiteCode());
        result = result.stream().peek(e -> {
            e.put("url", StringUtils.isBlank((String) e.get("uuid")) ? "" :
                    WhatyIdocvSdk.getReviewUrl((String) e.get("uuid")));
        }).collect(Collectors.toList());
        return new FileListTab(title, result);
    }
}
