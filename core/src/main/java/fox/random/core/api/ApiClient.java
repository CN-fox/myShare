package fox.random.core.api;

import android.app.Activity;
import android.os.Bundle;

import java.lang.reflect.Field;

import fox.random.core.Platform;
import fox.random.core.ShareSDK;
import fox.random.core.api.params.ShareParams;
import fox.random.core.callback.AuthListener;
import fox.random.core.callback.ShareListener;
import fox.random.core.constants.SNS;
import fox.random.core.exception.SnsException;
import fox.random.core.keep.OAuthHelper;
import fox.random.core.utils.NetUtil;
import fox.random.core.utils.ShareLog;

/**
 * Created by 渠 on 2014/10/22.
 */
public class ApiClient implements AuthApi, ShareApi {
    private static String TAG = "ApiClient";

    /**
     * 授权接口的实现
     * @param context
     * @param platform 需要授权的平台
     * @param authListener 授权结果回调接口
     */
    @Override
    public void doOauthVerify(final Activity context, final Platform platform, final AuthListener authListener) {

        if (authListener == null) {
            ShareLog.e(TAG, "authListener can't be NULL");
            return;
        }

        if (platform == null) {
            ShareLog.e(TAG, "platform can't be NULL");
            return;
        }

        //context 判断
        if (context == null) {
            ShareLog.e(TAG, "Context can't be NULL");
            SnsException e = new SnsException("Context can't be NULL");
            authListener.onError(e, platform.getSns());
            return;
        }

        //是否有网判断
        if (!NetUtil.hasNetwork(context)) {
            SnsException e = new SnsException("no network");
            authListener.onError(e, platform.getSns());
            return;
        }

        //用自己的监听，可以在回调过程中进行提示，记录等
        AuthListener mAuthListener = new AuthListener() {
            @Override
            public void onStart() {
                authListener.onStart();
            }

            @Override
            public void onComplete(Bundle bundle, SNS sns) {
                //保存授权信息
                OAuthHelper.save(context, sns, bundle);
                authListener.onComplete(bundle, sns);
            }

            @Override
            public void onError(SnsException e, SNS sns) {
                authListener.onError(e, sns);
            }

            @Override
            public void onCancel(SNS sns) {
                authListener.onCancel(sns);
            }
        };
        platform.doOauthVerify(context, mAuthListener);
    }

    /**
     * 分享接口的实现
     * @param activity
     * @param shareParams
     * @param shareListener
     */
    @Override
    public void share(Activity activity, ShareParams shareParams, final ShareListener shareListener) {
        //获得对应的平台
        SNS sns = shareParams.getSns();
        Platform platform = ShareSDK.getPlatform(sns);

        if (!OAuthHelper.isAuthenticated(activity, sns)) {
            platform.shareAfterLogin(true);
        }

        //调用平台的分享方法
        ShareListener mShareListener = new ShareListener() {
            @Override
            public void success(SNS sns) {
                shareListener.success(sns);
            }

            @Override
            public void error(Throwable e, SNS sns) {
                shareListener.error(e,sns);
            }
        };

        platform.share(activity,shareParams,mShareListener);
    }
}
