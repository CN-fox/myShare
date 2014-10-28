package fox.random.core;

import android.app.Activity;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

import fox.random.core.api.ApiClient;
import fox.random.core.api.params.ShareParams;
import fox.random.core.callback.AuthListener;
import fox.random.core.callback.ShareListener;
import fox.random.core.constants.SNS;

/**
 * Created by w_q on 14-10-14.
 */
public class ShareSDK {
    private final static String TAG = "ShareSDK";
    //用于存储 手动获取的平台信息
    private static Map<SNS, Platform> platforms = new HashMap<SNS, Platform>();

    /**
     * 实现所有api接口的类
     */
    private static ApiClient apiClient = new ApiClient();

    /**
     * 开启授权
     *
     * @param context      必须是一个Activity
     * @param platform     对应的平台信息
     * @param authListener 回调接口为必须参数
     */
    public static void doOauthVerify(Activity context, Platform platform, AuthListener authListener) {
        apiClient.doOauthVerify(context, platform, authListener);
    }

    /**
     * 分享
     *
     * @param activity
     * @param shareParams
     * @param shareListener
     */
    public static void share(Activity activity, ShareParams shareParams, ShareListener shareListener) {
        apiClient.share(activity, shareParams, shareListener);
    }

    /**
     * 获得对应的平台,但暂时也不考虑将各平台的构造 修饰符 进行修改
     *
     * @param sns
     * @return
     */
    public static Platform getPlatform(SNS sns) {
        if (sns == null)
            return null;

        if (platforms.containsKey(sns)) {
            return platforms.get(sns);
        }

        PlatformFactory platformFactory = PlatformFactory.getInstance();
        Platform platform = platformFactory.createPlatform(sns);
        if (platform == null)
            return null;

        platforms.put(sns, platform);
        return platform;
    }

    public static Platform getPlatform(SNS sns, String appkey) {
        Platform platform = getPlatform(sns);
        if (platform != null)
            platform.setAppKey(appkey);
        return platform;
    }
}
