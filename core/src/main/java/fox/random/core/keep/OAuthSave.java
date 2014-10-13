package fox.random.core.keep;

import android.content.Context;

/**
 * 第三方登录状态抽象操作接口
 * Created by wangqu on 13-7-25.
 */
interface OAuthSave {
    /**
     * 第三方状态保存
     * @param context
     * @param obj
     */
    public void save(Context context,Object obj);

    /**
     * 第三方认证参数或对象获取
     * @return
     */
    public Object get(Context context);

    /**
     * 第三方本地认证状态
     * @param context
     * @return
     */
    public  boolean isAuthenticated(Context context);

    /**
     * 删除第三方认证本地存储
     * @param context
     */
    public  void del(Context context);
}
