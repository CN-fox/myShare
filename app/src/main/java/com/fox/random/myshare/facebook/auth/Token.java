package com.fox.random.myshare.facebook.auth;

/**
 * Created by w_q on 14-9-21.
 */
public class Token {
    private String appId;
    private String access_token;

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getAccessToken() {
        return access_token;
    }

    public void setAccessToken(String access_token) {
        this.access_token = access_token;
    }

}
