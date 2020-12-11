package com.whaty.products.service.superadmin;

import java.io.File;
import java.io.IOException;

/**
 * 超管端开设站点服务接口
 *
 * @author weipengsen
 */
public interface SuperAdminCreateSiteService {

    /**
     * 一键开设站点
     * @param upload
     * @throws IOException
     */
    void createSites(File upload) throws IOException;

    /**
     * 上传站点logo
     * @param upload
     * @throws IOException
     */
    void doUploadSiteLogo(File upload) throws IOException;

    /**
     * 更新站点配置
     */
    void updateSiteConfig();
}
