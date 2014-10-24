package fox.random.core.api.params;

import fox.random.core.constants.SNS;

/**
 * Created by 渠 on 2014/10/22.
 */
public class ShareParams {
    /**
     * 基础的分享参数
     */
    private BaseShareParams baseShareParams;
    /**
     * 三方平台
     */
    private SNS sns;


    public SNS getSns() {
        return sns;
    }

    public void setSns(SNS sns) {
        this.sns = sns;
    }

    public BaseShareParams getBaseShareParams() {
        return baseShareParams;
    }

    public void setBaseShareParams(BaseShareParams baseShareParams) {
        this.baseShareParams = baseShareParams;
    }
}
