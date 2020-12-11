package com.whaty.products.service.wechat;

/**
 * accessToken服务类接口
 * @author weipengsen
 */
public interface WeChatAccessTokenService {

	/**
	 * 获取当前站点token
	 * @return
	 * @throws Exception
	 */
	String getToken() throws Exception;

	/**
	 * 根据站点获取accessToken
	 * @param siteId
	 * @param siteCode
     * @return
	 * @throws Exception
	 */
	String getToken(String siteId, String siteCode) throws Exception;

	/**
	 * 获取指定站点的token
	 * @param siteCode
	 * @return
	 */
    String getTokenForSite(String siteCode) throws Exception;
}
