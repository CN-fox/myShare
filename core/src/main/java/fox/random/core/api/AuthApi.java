package fox.random.core.api;

import android.app.Activity;

import fox.random.core.Platform;
import fox.random.core.callback.AuthListener;

/**
 * 登录相关接口
 * Created by w_q on 14-10-17.
 */
public interface AuthApi {
    /**
     * 平台授权
     * @param context
     * @param platform 需要授权的平台
     * @param authListener 授权结果回调接口
     */
    void doOauthVerify(Activity context, Platform platform,AuthListener authListener);
}
