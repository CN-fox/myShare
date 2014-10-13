package fox.random.core.keep;

import android.content.Context;

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
     * 删除对应平台的授权
     * @param context
     * @param sns
     */
    public static final void remove(Context context,SNS sns){
        switch (sns){
            case SINA:
                new SinaOAuthSave().del(context);
                break;
        }
    }
}
