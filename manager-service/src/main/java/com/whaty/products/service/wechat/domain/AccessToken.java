package com.whaty.products.service.wechat.domain;

import java.io.Serializable;
import java.util.Objects;

/**
 * accessToken实体类
 */
public class AccessToken implements Serializable {

    private static final long serialVersionUID = 1220650013454570411L;
    /**
     * accessToken
     */
    public String accessToken;
    /**
     * 取出时间
     */
    public Long readTime;
    /**
     * 过期时间
     */
    public Integer expiresIn;

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public Long getReadTime() {
        return readTime;
    }

    public void setReadTime(Long readTime) {
        this.readTime = readTime;
    }

    public Integer getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(Integer expiresIn) {
        this.expiresIn = expiresIn;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AccessToken that = (AccessToken) o;
        return Objects.equals(accessToken, that.accessToken) &&
                Objects.equals(readTime, that.readTime) &&
                Objects.equals(expiresIn, that.expiresIn);
    }

    @Override
    public int hashCode() {
        return Objects.hash(accessToken, readTime, expiresIn);
    }

    @Override
    public String toString() {
        return "AccessToken{" +
                "accessToken='" + accessToken + '\'' +
                ", readTime=" + readTime +
                ", expiresIn=" + expiresIn +
                '}';
    }
}