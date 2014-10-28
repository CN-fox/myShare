package fox.random.core;

import fox.random.core.constants.SNS;

/**
 * 平台实体创建工厂
 * Created by w_q on 14-10-26.
 */
class PlatformFactory {
    private static PlatformFactory platformFactory = new PlatformFactory();

    static PlatformFactory getInstance(){
        return platformFactory;
    }

    Platform createPlatform(SNS sns){

        Platform platform = null;
        switch (sns){
            case SINA:
                Class clazz = null;
                try {
                    //动态获取类名，从而创建实例
                    clazz = Class.forName(sns.getClassName());
                    platform = (Platform) clazz.newInstance();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }

                break;
        }
        return platform;
    }

    Platform getExtPlatform(Object object){
        //TODO 计划在完成两三个平台实现后，会去实现该方法，用于手动拓展平台
        return null;
    }
}
