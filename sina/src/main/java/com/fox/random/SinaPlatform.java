package com.fox.random;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.sina.weibo.sdk.auth.WeiboAuth;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.sina.weibo.sdk.exception.WeiboException;

import fox.random.core.Platform;
import fox.random.core.callback.AuthListener;
import fox.random.core.constants.SNS;
import fox.random.core.exception.SnsException;

/**
 * 新浪平台
 * Created by w_q on 14-10-14.
 */
public class SinaPlatform extends Platform {
    private boolean useSSO = false;
    private String appKey;
    private String redirectUrl = " "; //https://api.weibo.com/oauth2/default.html
    private String scope = "";

    public SinaPlatform(String appKey){
        this.appKey = appKey;
    }

    @Override
    public void doOauthVerify(Activity context, final AuthListener authListener) {
        WeiboAuth weiboAuth = new WeiboAuth(context,appKey,redirectUrl,scope);
        WeiboAuthListener listener = new WeiboAuthListener() {
            @Override
            public void onComplete(Bundle bundle) {
                authListener.onComplete(bundle, getSns());
            }

            @Override
            public void onWeiboException(WeiboException e) {
                Log.d("myShare",e.toString());
                authListener.onError(new SnsException(), getSns());
            }

            @Override
            public void onCancel() {
                authListener.onCancel(getSns());
            }
        };
        authListener.onStart();
        if (useSSO){
            SsoHandler ssoHandler = new SsoHandler(context,weiboAuth);
            ssoHandler.authorize(listener);
        }else {
            weiboAuth.anthorize(listener);
        }
    }

    @Override
    protected SNS getSns() {
        return SNS.SINA;
    }

    /**
     * 是否使用SSO调用客户端，默认是false
     * @param useSSO
     */
    public void setSSO(boolean useSSO){
        this.useSSO = useSSO;
    }
}
