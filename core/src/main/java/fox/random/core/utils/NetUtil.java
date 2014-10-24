package fox.random.core.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by 渠 on 2014/10/22.
 */
public class NetUtil {
    private NetUtil(){}

    /**
     * 获得网络连接是否可用
     *
     * @param context
     * @return
     */
    public static boolean hasNetwork(Context context) {
        if (context == null)
            return false;
        ConnectivityManager connect = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connect == null) {
            return false;
        } else {
            NetworkInfo[] info = connect.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
