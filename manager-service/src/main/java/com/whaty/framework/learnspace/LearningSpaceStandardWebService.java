package com.whaty.framework.learnspace;



/**
 * 学习空间标准接口，由其他平台自动实现
 * 
 * @author yx
 * 
 */
public interface LearningSpaceStandardWebService {
	
	boolean validateUserOnlineStatus(String loginId, String siteCode) throws Exception;
	
}
