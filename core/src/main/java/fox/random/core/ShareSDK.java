package fox.random.core;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import fox.random.core.callback.AuthListener;
import fox.random.core.constants.SNS;
import fox.random.core.exception.SnsException;
import fox.random.core.keep.OAuthHelper;
import fox.random.core.utils.ShareLog;

/**
 * Created by w_q on 14-10-14.
 */
public class ShareSDK {
    private final static String TAG = "ShareSDK";

    /**
     * 开启授权
     * @param context 必须是一个Activity
     * @param platform 对应的平台信息
     * @param authListener 回调接口为必须参数
     */
    public static void doOauthVerify(final Activity context, final Platform platform,final AuthListener authListener){
        if (context == null){
            ShareLog.e(TAG,"Context can't be NULL");
            return;
        }

        if (authListener == null){
            ShareLog.e(TAG,"authListener can't be NULL");
            return;
        }

        if (platform == null){
            ShareLog.e(TAG,"platform can't be NULL");
            return;
        }

        if (authListener != null){
            //用自己的监听，可以在回调过程中进行提示，记录等
            AuthListener mAuthListener = new AuthListener() {
                @Override
                public void onStart() {
                    authListener.onStart();
                }

                @Override
                public void onComplete(Bundle bundle, SNS sns) {
                    //保存授权信息
                    OAuthHelper.save(context,sns,bundle);
                    authListener.onComplete(bundle,sns);
                }

                @Override
                public void onError(SnsException e, SNS sns) {
                    authListener.onError(e,sns);
                }

                @Override
                public void onCancel(SNS sns) {
                    authListener.onCancel(sns);
                }
            };
            platform.doOauthVerify(context,mAuthListener);
        }
    };
}
