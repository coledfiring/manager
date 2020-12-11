package com.whaty.products.service.wechat;

/**
 * 微信基础配置读取服务类接口
 * @author weipengsen
 */
public interface WeChatBasicService {

	/**
	 * 获取配置
	 * @param platform
	 * @param siteCode
	 * @param name
	 * @return
	 */
	String getValue(String platform, String siteCode, String name);

}
