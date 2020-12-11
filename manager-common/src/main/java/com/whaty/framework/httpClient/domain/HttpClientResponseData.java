package com.whaty.framework.httpClient.domain;

import java.io.Serializable;
import java.util.Objects;

/**
 * httpClient的返回结果
 *
 * @author weipengsen
 */
public class HttpClientResponseData implements Serializable {

    private Integer status;

    private String content;

    public HttpClientResponseData(Integer status, String content) {
        this.status = status;
        this.content = content;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        HttpClientResponseData that = (HttpClientResponseData) o;
        return Objects.equals(status, that.status) &&
                Objects.equals(content, that.content);
    }

    @Override
    public int hashCode() {
        return Objects.hash(status, content);
    }

    @Override
    public String toString() {
        return "HttpClientResponseData{" +
                "status=" + status +
                ", content='" + content + '\'' +
                '}';
    }
}
