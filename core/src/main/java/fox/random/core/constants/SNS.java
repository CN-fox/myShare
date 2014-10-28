package fox.random.core.constants;

import fox.random.core.R;

/**
 *  社会化分享平台
 *  该枚举用于列举自带的平台
 *  若需要手动拓展平台，后续将另写一个类，因为枚举无法实现修改
 * Created by w_q on 14-10-13.
 */
public enum SNS {
    /** sina平台 */
    SINA(R.string.sns_sina_name,"com.fox.random.SinaPlatform");
    int id;
    /**
     * 该字段用于 PlatformFactory 生成对应平台实体
     */
    String className;
    SNS(int id,String className) {
        this.id = id;
        this.className = className;
    }

    public String getClassName(){
        return className;
    }

}
