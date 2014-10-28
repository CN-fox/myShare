package com.fox.random;

import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.sina.weibo.sdk.api.share.ApiUtils;
import com.sina.weibo.sdk.api.share.BaseRequest;
import com.sina.weibo.sdk.api.share.BaseResponse;
import com.sina.weibo.sdk.api.share.IWeiboDownloadListener;
import com.sina.weibo.sdk.api.share.IWeiboHandler;
import com.sina.weibo.sdk.api.share.IWeiboShareAPI;
import com.sina.weibo.sdk.api.share.ProvideMessageForWeiboRequest;
import com.sina.weibo.sdk.api.share.ProvideMessageForWeiboResponse;
import com.sina.weibo.sdk.api.share.ProvideMultiMessageForWeiboResponse;
import com.sina.weibo.sdk.api.share.SendMessageToWeiboRequest;
import com.sina.weibo.sdk.api.share.SendMessageToWeiboResponse;
import com.sina.weibo.sdk.api.share.SendMultiMessageToWeiboRequest;
import com.sina.weibo.sdk.api.share.VersionCheckHandler;
import com.sina.weibo.sdk.api.share.WeiboDownloader;
import com.sina.weibo.sdk.exception.WeiboShareException;
import com.sina.weibo.sdk.utils.LogUtil;
import com.sina.weibo.sdk.utils.MD5;
import com.sina.weibo.sdk.utils.Utility;

/**
 * Created by w_q on 14-10-29.
 */
public class WeiboShareAPIImpl implements IWeiboShareAPI {
    private static final String TAG = "WeiboApiImpl";
    private Context mContext;
    private String mAppKey;
    private ApiUtils.WeiboInfo mWeiboInfo = null;
    private boolean mNeedDownloadWeibo = true;
    private IWeiboDownloadListener mDownloadListener;
    private Dialog mDownloadConfirmDialog = null;

    public WeiboShareAPIImpl(Context context, String appKey, boolean needDownloadWeibo) {
        this.mContext = context;
        this.mAppKey = appKey;
        this.mNeedDownloadWeibo = needDownloadWeibo;


        this.mWeiboInfo = ApiUtils.queryWeiboInfo(this.mContext);
        if (this.mWeiboInfo != null) {
            LogUtil.d("WeiboApiImpl", this.mWeiboInfo.toString());
        } else {
            LogUtil.d("WeiboApiImpl", "WeiboInfo: is null");
        }
    }

    public boolean isWeiboAppInstalled() {
        return this.mWeiboInfo != null;
    }

    public boolean isWeiboAppSupportAPI() {
        return ApiUtils.isWeiboAppSupportAPI(getWeiboAppSupportAPI());
    }

    public int getWeiboAppSupportAPI() {
        return this.mWeiboInfo == null ? -1 : this.mWeiboInfo.supportApi;
    }

    public boolean registerApp() {
        try {
            if (!checkEnvironment(this.mNeedDownloadWeibo)) {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
        sendBroadcast(this.mContext, "com.sina.weibo.sdk.Intent.ACTION_WEIBO_REGISTER", this.mAppKey, null, null);
        return true;
    }

    public boolean handleWeiboResponse(Intent intent, IWeiboHandler.Response handler) {
        String appPackage = intent.getStringExtra("_weibo_appPackage");
        if (appPackage == null) {
            LogUtil.e("WeiboApiImpl", "responseListener() faild appPackage is null");
            return false;
        }
//        首先可以确定，一定要handler 为 Activity 没有理由 因为Context 可以是Activity

//        if (!(handler instanceof Activity)) {
//            LogUtil.e("WeiboApiImpl", "responseListener() faild handler is not Activity");
//            return false;
//        }
//        Activity act = (Activity) handler;
//        String callPkg = act.getCallingPackage();
//        LogUtil.d("WeiboApiImpl", "responseListener() callPkg : " + callPkg);

//        下面的一块被注释掉，暂时无法理解，这段代码的意义

//        if (intent.getStringExtra("_weibo_transaction") == null) {
//            LogUtil.e("WeiboApiImpl", "responseListener() faild intent TRAN is null");
//            return false;
//        }

        if (!ApiUtils.validateWeiboSign(this.mContext, appPackage)) {
            LogUtil.e("WeiboApiImpl", "responseListener() faild appPackage validateSign faild");
            return false;
        }
        SendMessageToWeiboResponse data = new SendMessageToWeiboResponse(intent.getExtras());
        handler.onResponse(data);
        return true;
    }

    public boolean handleWeiboRequest(Intent intent, IWeiboHandler.Request handler) {
        if ((intent == null) || (handler == null)) {
            return false;
        }
        String appPackage = intent.getStringExtra("_weibo_appPackage");
        String transaction = intent.getStringExtra("_weibo_transaction");
        if (appPackage == null) {
            LogUtil.e("WeiboApiImpl", "requestListener() faild appPackage validateSign faild");
            handler.onRequest(null);
            return false;
        }
        if (transaction == null) {
            LogUtil.e("WeiboApiImpl", "requestListener() faild intent TRAN is null");
            handler.onRequest(null);
            return false;
        }
        if (!ApiUtils.validateWeiboSign(this.mContext, appPackage)) {
            LogUtil.e("WeiboApiImpl", "requestListener() faild appPackage validateSign faild");
            handler.onRequest(null);
            return false;
        }
        ProvideMessageForWeiboRequest data = new ProvideMessageForWeiboRequest(intent.getExtras());
        handler.onRequest(data);
        return true;
    }

    public boolean launchWeibo() {
        if (this.mWeiboInfo == null) {
            LogUtil.e("WeiboApiImpl", "startWeibo() faild winfo is null");
            return false;
        }
        try {
            if (TextUtils.isEmpty(this.mWeiboInfo.packageName)) {
                LogUtil.e("WeiboApiImpl", "startWeibo() faild packageName is null");
                return false;
            }
            this.mContext.startActivity(
                    this.mContext.getPackageManager().getLaunchIntentForPackage(this.mWeiboInfo.packageName));
        } catch (Exception e) {
            LogUtil.e("WeiboApiImpl", e.getMessage());
            return false;
        }
        return true;
    }

    public boolean sendRequest(BaseRequest request) {
        if (request == null) {
            LogUtil.e("WeiboApiImpl", "sendRequest faild act == null or request == null");
            return false;
        }
        try {
            if (!checkEnvironment(this.mNeedDownloadWeibo)) {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
        VersionCheckHandler checkHandler = new VersionCheckHandler(this.mWeiboInfo.packageName);


        //TODO 这临时如下处理
        boolean checked = false;
        if (request instanceof SendMessageToWeiboRequest) {
            if (((SendMessageToWeiboRequest) request).message == null) {
                checked = false;
            } else if ((checkHandler != null) &&
                    (!checkHandler.check(mContext, ((SendMessageToWeiboRequest) request).message))) {
                checked = false;
            } else {
                checked = ((SendMessageToWeiboRequest) request).message.checkArgs();
            }
        } else if (request instanceof SendMultiMessageToWeiboRequest) {
            if (((SendMultiMessageToWeiboRequest) request).multiMessage == null) {
                checked = false;
            } else if ((checkHandler != null) &&
                    (!checkHandler.check(mContext, ((SendMultiMessageToWeiboRequest) request).multiMessage))) {
                checked = false;
            } else {
                checked = ((SendMessageToWeiboRequest) request).message.checkArgs();
            }
        } else {
            checked = true;
        }

//        if (!request.check(this.mContext, checkHandler))
        if (checked) {
            LogUtil.e("WeiboApiImpl", "sendRequest faild request check faild");
            return false;
        }
        Bundle data = new Bundle();
        request.toBundle(data);

        return launchWeiboActivity((Activity) this.mContext, "com.sina.weibo.sdk.action.ACTION_WEIBO_ACTIVITY", this.mWeiboInfo.packageName, this.mAppKey, data);
    }

    public boolean sendResponse(BaseResponse response) {
        if (response == null) {
            LogUtil.e("WeiboApiImpl", "sendResponse failed response null");
            return false;
        }

        boolean checked = false;
        VersionCheckHandler handler = new VersionCheckHandler();

        //TODO 下面暂时这样处理
        if (response instanceof ProvideMessageForWeiboResponse) {
            ProvideMessageForWeiboResponse mResponse = (ProvideMessageForWeiboResponse) response;

            if (mResponse.message == null) {
                checked = false;
            } else if (handler != null) {
                handler.setPackageName(mResponse.reqPackageName);
                if (!handler.check(mContext, mResponse.message)) {
                    checked = false;
                }
            } else
                checked = mResponse.message.checkArgs();
        } else if (response instanceof ProvideMultiMessageForWeiboResponse) {
            ProvideMultiMessageForWeiboResponse mResponse = (ProvideMultiMessageForWeiboResponse) response;
            if (mResponse.multiMessage == null) {
                checked = false;
            } else if (handler != null) {
                handler.setPackageName(mResponse.reqPackageName);
                if (!handler.check(mContext, mResponse.multiMessage)) {
                    checked = false;
                }
            } else
                checked = mResponse.multiMessage.checkArgs();
        } else {
            checked = true;
        }

//        if (!response.check(this.mContext, new VersionCheckHandler()))
        if (checked) {
            LogUtil.e("WeiboApiImpl", "sendResponse checkArgs fail");
            return false;
        }
        Bundle data = new Bundle();
        response.toBundle(data);
        sendBroadcast(this.mContext, "com.sina.weibo.sdk.Intent.ACTION_WEIBO_RESPONSE", this.mAppKey, response.reqPackageName, data);

        return true;
    }

    public void registerWeiboDownloadListener(IWeiboDownloadListener listener) {
        this.mDownloadListener = listener;
    }

    public boolean checkEnvironment(boolean bShowDownloadDialog)
            throws WeiboShareException {
        if (this.mWeiboInfo == null) {
            if (bShowDownloadDialog) {
                if (this.mDownloadConfirmDialog == null) {
                    this.mDownloadConfirmDialog = WeiboDownloader.createDownloadConfirmDialog(this.mContext, this.mDownloadListener);
                    this.mDownloadConfirmDialog.show();
                } else if (!this.mDownloadConfirmDialog.isShowing()) {
                    this.mDownloadConfirmDialog.show();
                }
                return false;
            }
            throw new WeiboShareException("Weibo is NOT installed!");
        }
        if (!ApiUtils.isWeiboAppSupportAPI(this.mWeiboInfo.supportApi)) {
            throw new WeiboShareException("Weibo do NOT support Share API!");
        }
        if (!ApiUtils.validateWeiboSign(this.mContext, this.mWeiboInfo.packageName)) {
            throw new WeiboShareException("Weibo signature is incorrect!");
        }
        return true;
    }

    private boolean launchWeiboActivity(Activity activity, String action, String pkgName, String appkey, Bundle data) {
        if ((activity == null) ||
                (TextUtils.isEmpty(action)) ||
                (TextUtils.isEmpty(pkgName)) ||
                (TextUtils.isEmpty(appkey))) {
            LogUtil.e("ActivityHandler", "send fail, invalid arguments");
            return false;
        }
        Intent intent = new Intent();
        intent.setPackage(pkgName);
        intent.setAction(action);
        String appPackage = activity.getPackageName();

        intent.putExtra("_weibo_sdkVersion", 22);
        intent.putExtra("_weibo_appPackage", appPackage);
        intent.putExtra("_weibo_appKey", appkey);
        intent.putExtra("_weibo_flag", 538116905);
        intent.putExtra("_weibo_sign", MD5.hexdigest(Utility.getSign(activity, appPackage)));
        if (data != null) {
            intent.putExtras(data);
        }
        try {
            LogUtil.d("WeiboApiImpl", "intent=" + intent + ", extra=" + intent.getExtras());
            activity.startActivityForResult(intent, 765);
        } catch (ActivityNotFoundException e) {
            LogUtil.e("WeiboApiImpl", "Failed, target ActivityNotFound");
            return false;
        }
        return true;
    }

    private void sendBroadcast(Context context, String action, String key, String packageName, Bundle data) {
        Intent intent = new Intent(action);
        String appPackage = context.getPackageName();
        intent.putExtra("_weibo_sdkVersion", 22);
        intent.putExtra("_weibo_appPackage", appPackage);
        intent.putExtra("_weibo_appKey", key);
        intent.putExtra("_weibo_flag", 538116905);
        intent.putExtra("_weibo_sign", MD5.hexdigest(Utility.getSign(context, appPackage)));
        if (!TextUtils.isEmpty(packageName)) {
            intent.setPackage(packageName);
        }
        if (data != null) {
            intent.putExtras(data);
        }
        LogUtil.d("WeiboApiImpl", "intent=" + intent + ", extra=" + intent.getExtras());
        context.sendBroadcast(intent, "com.sina.weibo.permission.WEIBO_SDK_PERMISSION");
    }

}
