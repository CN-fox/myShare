package fox.random.core.keep;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by w_q on 14-10-13.
 */
public class SinaOAuthSave extends BaseOAuthSave  {

    @Override
    public void save(Context context, Object obj) {
        SharedPreferences sp = context.getSharedPreferences(oauthSaveFileName,Context.MODE_PRIVATE);
    }

    @Override
    public Object get(Context context) {
        return null;
    }

    @Override
    public boolean isAuthenticated(Context context) {
        return false;
    }

    @Override
    public void del(Context context) {

    }
}
