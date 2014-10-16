package fox.random.core.keep;

import android.content.Context;
import android.os.Bundle;

import fox.random.core.constants.SNS;

/**
 * 授权管理类
 * Created by w_q on 14-10-13.
 */
public class OAuthHelper {
    private OAuthHelper(){}

    /**
     * 对应平台是否已经授权
     * @param context
     * @param sns
     * @return
     */
    public static final boolean isAuthenticated(Context context,SNS sns){
        boolean authenticated = false;
        switch (sns){
            case SINA:
                authenticated = new SinaOAuthSave().isAuthenticated(context);
                break;
        }
        return authenticated;
    }

    /**
     * 保存授权信息
     * @param context
     * @param sns 对应的平台
     * @param bundle 授权返回数据
     */
    public static final void save(Context context,SNS sns,Bundle bundle){
        switch (sns){
            case SINA:
                new SinaOAuthSave().save(context,bundle);
                break;
        }
    }

    /**
     * 删除对应平台的授权
     * @param context
     * @param sns 对应的平台
     */
    public static final void remove(Context context,SNS sns){
        switch (sns){
            case SINA:
                new SinaOAuthSave().del(context);
                break;
        }
    }
}
