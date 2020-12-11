package com.whaty.products.service.common.constant;

import java.util.HashMap;
import java.util.Map;

/**
 * 常量类
 * @author weipengsen
 */
public interface ComConstant {

    /**
     * 参数，文件路径对应key值
     */
    String PARAM_FILE_KEY = "fileKey";

    /**
     * 文件下载根路径
     */
    String DOWNLOAD_FILE_BASE_PATH = "/templatefile";
    /**
     * 文件key与路径映射
     */
    Map<String, String> FILE_PATH_LINK_MAP = new HashMap<>();
    /**
     * 参数，命名空间
     */
    String PARAM_NAMESPACE = "namespace";

    /**
     * 参数，命名空间
     */
    String PARAM_SCENE = "flagScene";

    /**
     * 附件地址，站点编号/命名空间/连接id/文件名
     */
    String ATTACH_FILE_PATH = "/incoming/attachFile/%s/%s/%s/%s";
    /**
     * 常量命名空间，附件类型
     */
    String ENUM_CONST_NAMESPACE_ATTACH_TYPE = "flagAttachType";
    /**
     * 产量命名空间，附件操作类型
     */
    String ENUM_CONST_NAMESPACE_OPERATE_TYPE = "flagFileOperateType";
}
