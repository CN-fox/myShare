package fox.random.core.constants;

import fox.random.core.R;

/**
 *  社会化分享平台
 * Created by w_q on 14-10-13.
 */
public enum SNS {
    /** sina平台 */
    SINA(R.string.sns_sina_name);
    int id;

    SNS(int id) {
        this.id = id;
    }
}
