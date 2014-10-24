package fox.random.core;

import android.app.Activity;

import fox.random.core.callback.AuthListener;
import fox.random.core.constants.SNS;

/**
 * 平台类
 * Created by w_q on 14-10-14.
 */
public abstract class Platform{
    private boolean useSSO = false;

    /**
     * 开启OAuth授权
     * @param context
     * @param authListener
     */
    public abstract void doOauthVerify(Activity context,AuthListener authListener);

    /**
     * 获得平台的类型
     * @return
     */
    public abstract SNS getSns();
}
