package fox.random.core;

import android.app.Activity;

import fox.random.core.api.params.ShareParams;
import fox.random.core.callback.AuthListener;
import fox.random.core.callback.ShareListener;
import fox.random.core.constants.SNS;

/**
 * 平台类
 * Created by w_q on 14-10-14.
 */
public abstract class Platform {

    /**
     * 开启OAuth授权
     *
     * @param context
     * @param authListener
     */
    public abstract void doOauthVerify(Activity context, AuthListener authListener);


    /**
     * 分享
     * @param activity
     * @param shareParams
     * @param shareListener
     */
    public abstract void share(Activity activity, ShareParams shareParams, ShareListener shareListener);

    /**
     * 登陆后即刻分享，用于调用分享方法时，未授权的情况
     * @param share
     */
    public abstract void shareAfterLogin(boolean share);

    /**
     * 考虑到很多平台都需要该字段，故将其提取成方法
     * @param appKey
     */
    public abstract void setAppKey(String appKey);

    /**
     * 获得平台的类型
     *
     * @return
     */
    public abstract SNS getSns();
}
