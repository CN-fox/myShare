package fox.random.core.callback;

import android.os.Bundle;

import fox.random.core.constants.SNS;
import fox.random.core.exception.SnsException;

/**
 * Created by w_q on 14-10-14.
 */
public interface AuthListener {
    /**
     * 开始授权
     */
    public void onStart();

    /**
     * 授权完成
     * @param bundle
     * @param sns
     */
    public void onComplete(Bundle bundle,SNS sns);

    public void onError(SnsException e,SNS sns);

    public void onCancel(SNS sns);
}
