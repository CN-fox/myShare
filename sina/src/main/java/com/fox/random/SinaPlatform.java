package com.fox.random;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.fox.random.ui.AuthActivity;
import com.sina.weibo.sdk.auth.WeiboAuth;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.utils.LogUtil;
import com.sina.weibo.sdk.utils.NetworkHelper;
import com.sina.weibo.sdk.utils.ResourceManager;
import com.sina.weibo.sdk.utils.UIUtils;

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
    /**
     * 回调地址使用的是新浪官方默认的，可以在官网进行修改，同时也需要修改此处。
     * 后续看是否对此处进行优化
     */
    private String redirectUrl = "https://api.weibo.com/oauth2/default.html";
    private String scope = "";

    private SsoHandler ssoHandler;

    /**
     * 是否是Activity样式，默认值是true
     * TODO 后续进行设置优化
     */
    private boolean isActivity = true;

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
            ssoHandler = new SsoHandler(context,weiboAuth);
            ssoHandler.authorize(listener);
        }else {
            if (isActivity){
                new AuthActivity(context,weiboAuth,listener).show();
            }else {
                weiboAuth.anthorize(listener);
            }
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

    public void authorizeCallBack(int requestCode, int resultCode, Intent data){
        if (ssoHandler!=null){
            ssoHandler.authorizeCallBack(requestCode,resultCode,data);
        }
    }
}
