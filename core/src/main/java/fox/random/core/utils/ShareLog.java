package fox.random.core.utils;

import android.util.Log;

import fox.random.core.config.DefaultConfig;

/**
 * 日志打印工具
 * Created by w_q on 14-10-17.
 */
public class ShareLog {
    private ShareLog(){};

    /**
     * 打印info信息
     * @param TAG
     * @param log
     */
    public static void i(String TAG,String log){
        //TODO 后续将完成自动判断是否为发布版本
        if (DefaultConfig.DEBUG){
            Log.i(TAG,log);
        }
    }

    /**
     * 打印debug信息
     * @param TAG
     * @param log
     */
    public static void d(String TAG,String log){
        if (DefaultConfig.DEBUG){
            Log.d(TAG,log);
        }
    }

    /**
     * 打印错误信息
     * @param TAG
     * @param log
     */
    public static void e(String TAG,String log){
        if (DefaultConfig.DEBUG){
            Log.e(TAG,log);
        }
    }

}
