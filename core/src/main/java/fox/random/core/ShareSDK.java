package fox.random.core;

import android.app.Activity;

import fox.random.core.api.ApiClient;
import fox.random.core.callback.AuthListener;

/**
 * Created by w_q on 14-10-14.
 */
public class ShareSDK {
    private final static String TAG = "ShareSDK";
    /**
     * 实现所有api接口的类
     */
    private static ApiClient apiClient = new ApiClient();

    /**
     * 开启授权
     * @param context 必须是一个Activity
     * @param platform 对应的平台信息
     * @param authListener 回调接口为必须参数
     */
    public static void doOauthVerify(Activity context, Platform platform,AuthListener authListener){
        apiClient.doOauthVerify(context,platform,authListener);
    };
}
