package com.whaty.framework.idocv;

import com.whaty.core.commons.httpClient.WhatyHttpClient;
import com.whaty.framework.asserts.TycjAssert;
import net.sf.json.JSONObject;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.httpclient.methods.multipart.StringPart;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;

/**
 * whaty idocv文档服务对接sdk
 *
 * @author weipengsen
 */
public class WhatyIdocvSdk {

    private static final String TOKEN = "d9y8SD9hCWJZ62ix";

    private static final Set<String> UPLOAD_WHITE_LIST = new TreeSet<>(Arrays.asList("doc", "docx",
            "xls", "xlsx", "ppt", "pptx", "pdf", "txt"));

    /**
     * 上传
     * @param file
     * @param fileName
     * @return
     */
    public String upload(File file, String fileName) throws IOException {
        TycjAssert.fileAllExists(file);
        TycjAssert.isAllNotBlank(fileName);
        if (UPLOAD_WHITE_LIST.stream().noneMatch(fileName::endsWith)) {
            return null;
        }
        PostMethod postMethod = new PostMethod(IdocvConstants.IDOCV_DOMAIN + IdocvConstants.IDOCV_UPLOAD_URL);
        Part[] parts;
        try {
            parts = new Part[]{ new FilePart(IdocvConstants.IDOCV_UPLOAD_PARAM_FILE, file),
                    new StringPart(IdocvConstants.IDOCV_UPLOAD_PARAM_TOKEN, TOKEN),
                    new StringPart(IdocvConstants.IDOCV_UPLOAD_PARAM_NAME, fileName, "UTF-8"),
                    new StringPart(IdocvConstants.IDOCV_UPLOAD_PARAM_MODE, IdocvConstants.IDOCV_MODE_PUBLIC)};
        } catch (FileNotFoundException e) {
            throw new IllegalArgumentException(e);
        }
        postMethod.setRequestEntity(new MultipartRequestEntity(parts, postMethod.getParams()));
        HttpClient httpClient = new WhatyHttpClient().initHttpClient();
        httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(120000);
        httpClient.getHttpConnectionManager().getParams().setSoTimeout(120000);
        int status = httpClient.executeMethod(postMethod);
        if (status != HttpStatus.SC_OK) {
            throw new HttpException(String.format("upload file to idocv error, status: %s, response: %s",
                    status, postMethod.getResponseBodyAsString()));
        }
        JSONObject response = JSONObject.fromObject(postMethod.getResponseBodyAsString());
        if ("1".equals(response.getString("code"))) {
            return JSONObject.fromObject(postMethod.getResponseBodyAsString()).getString("uuid");
        }
        return null;
    }

    /**
     * 获取预览地址
     * @param uuid
     * @return
     */
    public static String getReviewUrl(String uuid) {
        return IdocvConstants.IDOCV_DOMAIN + IdocvConstants.IDOCV_VIEW_URL + uuid;
    }

}
