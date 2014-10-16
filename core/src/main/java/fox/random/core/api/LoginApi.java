package fox.random.core.api;

import android.app.Activity;

import fox.random.core.callback.AuthListener;

/**
 * 登录相关接口
 * Created by w_q on 14-10-17.
 */
public interface LoginApi {
    void doOauthVerify(Activity context,AuthListener authListener);
}
