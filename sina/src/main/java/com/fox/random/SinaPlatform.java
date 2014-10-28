package com.fox.random;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.fox.random.ui.AuthActivity;
import com.sina.weibo.sdk.api.TextObject;
import com.sina.weibo.sdk.api.WeiboMessage;
import com.sina.weibo.sdk.api.WeiboMultiMessage;
import com.sina.weibo.sdk.api.share.BaseResponse;
import com.sina.weibo.sdk.api.share.IWeiboHandler;
import com.sina.weibo.sdk.api.share.IWeiboShareAPI;
import com.sina.weibo.sdk.api.share.SendMessageToWeiboRequest;
import com.sina.weibo.sdk.api.share.SendMultiMessageToWeiboRequest;
import com.sina.weibo.sdk.api.share.WeiboShareSDK;
import com.sina.weibo.sdk.auth.WeiboAuth;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.sina.weibo.sdk.constant.WBConstants;
import com.sina.weibo.sdk.exception.WeiboException;

import fox.random.core.Platform;
import fox.random.core.api.params.ShareParams;
import fox.random.core.callback.AuthListener;
import fox.random.core.callback.ShareListener;
import fox.random.core.constants.SNS;
import fox.random.core.exception.SnsException;
import fox.random.core.keep.OAuthHelper;
import fox.random.core.utils.ShareLog;

/**
 * 新浪平台
 * Created by w_q on 14-10-14.
 */
public class SinaPlatform extends Platform implements IWeiboHandler.Response {
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

    private boolean shareAfterLogin = false;


    public String TAG = "SinaPlatform";

    public SinaPlatform() {

    }

    public SinaPlatform(String appKey) {
        this.appKey = appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    @Override
    public void doOauthVerify(Activity context, final AuthListener authListener) {
        WeiboAuth weiboAuth = new WeiboAuth(context, appKey, redirectUrl, scope);
        WeiboAuthListener listener = new WeiboAuthListener() {
            @Override
            public void onComplete(Bundle bundle) {
                authListener.onComplete(bundle, getSns());
            }

            @Override
            public void onWeiboException(WeiboException e) {
                Log.d("myShare", e.toString());
                authListener.onError(new SnsException(e), getSns());
            }

            @Override
            public void onCancel() {
                authListener.onCancel(getSns());
            }
        };
        authListener.onStart();
        if (useSSO) {
            ssoHandler = new SsoHandler(context, weiboAuth);
            ssoHandler.authorize(listener);
        } else {
            if (isActivity) {
                new AuthActivity(context, weiboAuth, listener).show();
            } else {
                weiboAuth.anthorize(listener);
            }
        }
    }

    @Override
    public void share(Activity activity, ShareParams shareParams, ShareListener shareListener) {
        SNS sns = shareParams.getSns();
        if (!OAuthHelper.isAuthenticated(activity, sns)) {

//            doOauthVerify(activity,new AuthListener() {
//                @Override
//                public void onStart() {
//
//                }
//
//                @Override
//                public void onComplete(Bundle bundle, SNS sns) {
//
//                }
//
//                @Override
//                public void onError(SnsException e, SNS sns) {
//
//                }
//
//                @Override
//                public void onCancel(SNS sns) {
//
//                }
//            });
        } else {

        }
        share(activity, shareParams.getBaseShareParams().getContent());


    }

    IWeiboShareAPI weiboAPI;
    Activity activity;

    private void share(final Activity activity, final String content) {
        //不下载 WeiBo apk
        weiboAPI = new WeiboShareAPIImpl(activity, appKey, false);
                //WeiboShareSDK.createWeiboAPI(activity, appKey, false);
        this.activity = activity;
        weiboAPI.registerApp();


        WeiboMessage weiboMessage = new WeiboMessage();

        TextObject textObject = new TextObject();
        textObject.text = content;

        weiboMessage.mediaObject = textObject;

        SendMessageToWeiboRequest request = new SendMessageToWeiboRequest();
        request.message = weiboMessage;

        weiboAPI.sendRequest(request);

    }

    @Override
    public void shareAfterLogin(boolean share) {
        shareAfterLogin = share;
    }

    @Override
    public SNS getSns() {
        return SNS.SINA;
    }

    /**
     * 是否使用SSO调用客户端，默认是false
     *
     * @param useSSO
     */
    public void setSSO(boolean useSSO) {
        this.useSSO = useSSO;
    }

    public void authorizeCallBack(int requestCode, int resultCode, Intent data) {
        if (ssoHandler != null) {
            ssoHandler.authorizeCallBack(requestCode, resultCode, data);
        }
    }

    @Override
    public void onResponse(BaseResponse baseResp) {
        switch (baseResp.errCode) {
            case WBConstants.ErrorCode.ERR_OK:
                ShareLog.i(TAG, "分享完成了");
                break;
            case WBConstants.ErrorCode.ERR_CANCEL:
                ShareLog.e(TAG, "取消了");
                break;
            case WBConstants.ErrorCode.ERR_FAIL:
                ShareLog.e(TAG, baseResp.errMsg);
                break;
        }
    }

    public void handleWeiboResponse(Intent intent){
        if (weiboAPI != null) {
            weiboAPI.handleWeiboResponse(intent, this);
        }
    }
}
