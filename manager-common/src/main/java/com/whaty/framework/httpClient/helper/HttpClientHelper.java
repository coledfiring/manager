package com.whaty.framework.httpClient.helper;

import com.whaty.framework.httpClient.domain.HttpClientResponseData;
import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * httpClient辅助类
 *
 * @author weipengsen
 */
public class HttpClientHelper {

	private final HttpClient httpClient;

	/**
	 * 状态参数
	 */
	public static final String STATUS = "status";
	/**
	 * 返回内容参数
	 */
	public static final String CONTENT = "content";
	/**
	 * 失败情况下返回信息
	 */
	public static final String FAILURE = "failure";

	private static final String USER_AGENT_DEFAULT = "Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; Trident/5.0)";

	private static final String CHARSET_UTF_8 = "UTF-8";

	private static final String CONTENT_TYPE_APPLICATION_JSON = "application/json";

	private static final String CONTENT_TYPE_WWW_FORM = "application/x-www-form-urlencoded;charset=UTF-8";

	public HttpClientHelper() {
		this(USER_AGENT_DEFAULT, CHARSET_UTF_8);
	}

	public HttpClientHelper(String userAgent, String contentCharset) {
		this.httpClient = new HttpClient();
		httpClient.getParams().setParameter(HttpMethodParams.USER_AGENT, userAgent);
		httpClient.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, contentCharset);
	}

	/**
	 * 提供json参数的post方法
	 * @param url
	 * @param json
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public HttpClientResponseData doPostJSON(String url, String json) throws IOException {
		PostMethod method = new PostMethod(url);
		method.setRequestEntity(new StringRequestEntity(json, CONTENT_TYPE_APPLICATION_JSON, CHARSET_UTF_8));
		method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler());
		return this.executeRequest(method, this.httpClient);
	}
	
	/**
	 * form方式提交的post方法
	 * 
	 * @param url
	 * @return
	 * @throws IOException
	 */
	public HttpClientResponseData doPostForm(String url, Map<String, String> parameter) throws IOException {
		PostMethod post = new PostMethod(url);
		post.setRequestHeader("Content-Type", CONTENT_TYPE_WWW_FORM);
		NameValuePair[] params = parameter.entrySet().stream()
				.map(e -> new NameValuePair(e.getKey(), e.getValue()))
				.toArray(NameValuePair[]::new);
		post.setRequestBody(params);
		post.releaseConnection();
		return this.executeRequest(post, this.httpClient);
	}

	/**
	 * 填充请求体的post方法
	 * @param url
	 * @param entity
	 * @return
	 * @throws IOException
	 */
	public HttpClientResponseData doPostEntity(String url, String entity) throws IOException {
		PostMethod post = new PostMethod(url);
		post.setRequestHeader("Content-Type", CONTENT_TYPE_WWW_FORM);
		post.setRequestEntity(new StringRequestEntity(entity, null, CHARSET_UTF_8));
		post.releaseConnection();
		return this.executeRequest(post, this.httpClient);
	}

	/**
	 * 执行方法并返回结果
	 * @param method
	 * @param httpClient
	 * @return status -> 200,400,500等
	 * 			content - > 200对应响应信息,其他错误信息对应fail
	 *
	 * @throws IOException
	 */
	public HttpClientResponseData executeRequest(HttpMethod method, HttpClient httpClient) throws IOException {
		int status = httpClient.executeMethod(method);
		return new HttpClientResponseData(status, status == HttpStatus.OK.value() ?
				new String(method.getResponseBody()) : FAILURE);
	}

	/**
	 * 执行get方法
	 * @param url
	 * @return
	 * @throws IOException
	 */
	public HttpClientResponseData doGet(String url) throws IOException {
		GetMethod get = new GetMethod(url);
		return this.executeRequest(get, this.httpClient);
	}
}
