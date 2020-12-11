package com.whaty.products.service.common;

import java.io.File;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

/**
 * 业务通用服务类
 *
 * @author weipengsen
 */
public interface UtilService {

    /**
     * 调用通用导入工具类一站式导入操作
     * @param file
     * @param headers
     * @param checkList
     * @param sqlList
     * @param titleRowNum
     * @param infoPrefix
     * @return
     */
    Map<String, Object> doUploadFile(File file, Object[] headers, List<String[]> checkList,
                                     List<String> sqlList, int titleRowNum, String infoPrefix);

    /**
     * 生成excel文件并写入流
     * @param out
     * @param selectMap
     * @param headers
     */
    void generateExcelAndWrite(OutputStream out, Map<String, List<String>> selectMap, Object[] headers);

    /**
     * 生成excel文件并写入流
     * @param out
     * @param selectMap
     * @param headers
     * @param title
     */
    void generateExcelAndWrite(OutputStream out, Map<String, List<String>> selectMap, Object[] headers, String title);

    /**
     * 生成excel文件并写入流
     * @param out
     * @param selectMap
     * @param headers
     * @param title
     * @param data
     */
    void generateExcelAndWrite(OutputStream out, Map<String, List<String>> selectMap, Object[] headers, String title,
                               List<Object[]> data);

    /**
     * 根据用户角色id和siteCode获取基础功能id和对应菜单id
     * @param roleId
     * @param siteCode
     * @return
     */
    List<Map<String, Object>> getBaseCategoryIdsBySiteRole(String roleId, String siteCode);
}
