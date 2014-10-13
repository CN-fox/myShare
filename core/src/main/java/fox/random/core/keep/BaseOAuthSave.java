package fox.random.core.keep;

import fox.random.core.config.DefaultConfig;

/**
 * Created by w_q on 14-10-13.
 */
public abstract class BaseOAuthSave implements OAuthSave {
    /**
     * 认证信息存放的SharedPreferences名
     */
    protected String oauthSaveFileName;
    protected BaseOAuthSave(){
        oauthSaveFileName = DefaultConfig.OAUTHSAVENAME;
    }
}
