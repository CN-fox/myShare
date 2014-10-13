package fox.random.core.keep;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

/**
 * sina的OAuth保存
 * Created by w_q on 14-10-13.
 */
class SinaOAuthSave extends BaseOAuthSave  {

    @Override
    public void save(Context context, Object obj) {
        SharedPreferences sp = context.getSharedPreferences(oauthSaveFileName,Context.MODE_PRIVATE);
        Bundle bundle = (Bundle) obj;
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("sina_uid", bundle.get("uid") + "");
        editor.putString("sina_accessToken", bundle.get("access_token") + "");
        editor.putString("sina_expires_in", bundle.get("expires_in") + "");
        editor.commit();
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
        SharedPreferences sp = context.getSharedPreferences(oauthSaveFileName,Context.MODE_PRIVATE);
        sp.edit().remove("sina_uid").remove("sina_accessToken")
                .remove("sina_expires_in").commit();
    }
}
