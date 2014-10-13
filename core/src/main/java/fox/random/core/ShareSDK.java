package fox.random.core;

import android.app.Activity;
import android.content.Context;

import fox.random.core.callback.AuthListener;

/**
 * Created by w_q on 14-10-14.
 */
public class ShareSDK {

    public static void doOauthVerify(Activity context,Platform platform,AuthListener authListener){
        if (platform == null){
            //TODO 这里应该是异常处理
            return;
        }
        if (authListener != null){
            platform.doOauthVerify(context,authListener);
        }
    };
}
