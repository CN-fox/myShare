package fox.random.core.api;

import android.app.Activity;

import fox.random.core.api.params.ShareParams;
import fox.random.core.callback.ShareListener;

/**
 * 分享接口
 * Created by 渠 on 2014/10/22.
 */
public interface ShareApi {

    void share(Activity activity,ShareParams shareParams,ShareListener shareListener);
}
