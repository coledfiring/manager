package com.whaty.framework.idocv;

/**
 * idocv constants
 * 
 * @author zhoudonghua
 * 
 */
public interface IdocvConstants {

	/**
	 * 文档服务域名
	 */
	String IDOCV_DOMAIN = "http://idocv.webtrn.cn";

	/**
	 * 文档服务预览地址
	 */
	String IDOCV_VIEW_URL = "/view/";

	/**
	 * 文档服务上传接口地址
	 */
	String IDOCV_UPLOAD_URL = "/doc/upload";

	/**
	 * 文档服务上传参数(name)
	 */
	String IDOCV_UPLOAD_PARAM_NAME = "name";

	/**
	 * 文档服务上传参数(mode)
	 */
	String IDOCV_UPLOAD_PARAM_MODE = "mode";

	/**
	 * 文档服务上传参数(file)
	 */
	String IDOCV_UPLOAD_PARAM_FILE = "file";

	/**
	 * 文档服务上传参数(token)
	 */
	String IDOCV_UPLOAD_PARAM_TOKEN = "token";

	/**
	 * 文档服务公开模式
	 */
	String IDOCV_MODE_PUBLIC = "public";

}
