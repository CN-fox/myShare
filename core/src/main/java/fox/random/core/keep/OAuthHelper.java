package fox.random.core.keep;

import android.content.Context;

import fox.random.core.constants.SNS;

/**
 * Created by w_q on 14-10-13.
 */
public class OAuthHelper {
    private OAuthHelper(){}


    public static final boolean isAuthenticated(Context context,SNS sns){
        switch (sns){
            case SINA:
                break;
        }
        return false;
    }
}
