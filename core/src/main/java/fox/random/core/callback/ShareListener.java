package fox.random.core.callback;

import fox.random.core.constants.SNS;

/**
 * 分享sdk
 * Created by 渠 on 2014/10/22.
 */
public interface ShareListener {
    void success(SNS sns);
    void error(Throwable e,SNS sns);
}
