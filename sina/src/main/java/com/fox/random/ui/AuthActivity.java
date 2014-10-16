package com.fox.random.ui;

import java.io.IOException;
import java.io.InputStream;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.http.SslError;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.sina.weibo.sdk.auth.WeiboAuth;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.WeiboParameters;
import com.sina.weibo.sdk.utils.Utility;


/**
 * 这是一个dialog，将做成类似于Activity的样式，故取名Activity
 * <p/>
 * Created by w_q on 14-10-17.
 */
public class AuthActivity extends Dialog {

    static FrameLayout.LayoutParams FILL = new FrameLayout.LayoutParams(
            ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT);
    private final WeiboAuth.AuthInfo mAuthInfo;
    private String mUrl;
    private WeiboAuthListener mListener;
    private ProgressDialog mSpinner;
    private WebView mWebView;
    private RelativeLayout webViewContainer;
    private LinearLayout linearLayout;
    private Context mContext;

    private final static String TAG = "Weibo-WebView";

    private static int theme = android.R.style.Theme_Translucent_NoTitleBar;

    /**
     * 构造函数
     *
     * @param context
     * @param weiboAuth
     * @param listener
     */
    public AuthActivity(Context context, WeiboAuth weiboAuth, WeiboAuthListener listener) {
        super(context, theme);
        mContext = context;
        mAuthInfo = weiboAuth.getAuthInfo();
        mUrl = getUrl(1);
        mListener = listener;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//		mSpinner是加载时的缓冲界面
        mSpinner = new ProgressDialog(getContext());
        mSpinner.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mSpinner.setMessage("Loading...");
        mSpinner.setOnKeyListener(new OnKeyListener() {

            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                onBack();
                return false;
            }

        });

//		设置Dialog无标题
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFeatureDrawableAlpha(Window.FEATURE_OPTIONS_PANEL, 0);
        linearLayout = new LinearLayout(getContext());
//		对webview进行设置
        setUpWebView();
        addContentView(linearLayout, new LayoutParams(LayoutParams.FILL_PARENT,
                LayoutParams.FILL_PARENT));

        WindowManager.LayoutParams params = this.getWindow().getAttributes();
        params.softInputMode = WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN;
        this.getWindow().setAttributes(params);
    }

    /**
     * 返回按钮的处理
     */
    protected void onBack() {
        try {
            mSpinner.dismiss();
            if (null != mWebView) {
                mWebView.stopLoading();
                mWebView.destroy();
            }
        } catch (Exception e) {
        }
        dismiss();
    }

    /**
     * 对webview进行设置，这里进行了主要的验证处理
     */
    private void setUpWebView() {
//		相对布局
        webViewContainer = new RelativeLayout(getContext());
        mWebView = new WebView(getContext());
        mWebView.setVerticalScrollBarEnabled(false);
        mWebView.setHorizontalScrollBarEnabled(false);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setSavePassword(false);
        mWebView.setWebViewClient(new AuthActivity.WeiboWebViewClient());
        mWebView.loadUrl(mUrl);

        LinearLayout.LayoutParams WRAP = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);


        mWebView.setLayoutParams(WRAP);
        mWebView.setVisibility(View.INVISIBLE);


        RelativeLayout.LayoutParams lp0 = new RelativeLayout.LayoutParams(LayoutParams.FILL_PARENT,
                LayoutParams.FILL_PARENT);

        linearLayout.setBackgroundColor(Color.TRANSPARENT);

        InputStream is = null;
        try {

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

//		相对布局中加上webView
        webViewContainer.addView(mWebView, lp0);
        webViewContainer.setGravity(Gravity.CENTER);


//		从此处是添加进去的
// TODO 后续做一个 TitleBar, 及切换动画定义

        linearLayout.setOrientation(LinearLayout.VERTICAL);
//        linearLayout.addView(view);
        linearLayout.addView(webViewContainer);
        //  getWindow().setWindowAnimations(R.style.dAnimation2); //dialog动画
        getWindow().setGravity(Gravity.CENTER);

    }

    private class WeiboWebViewClient extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            Log.d(TAG, "Redirect URL: " + url);
            if (url.startsWith("sms:")) {  //针对webview里的短信注册流程，需要在此单独处理sms协议
                Intent sendIntent = new Intent(Intent.ACTION_VIEW);
                sendIntent.putExtra("address", url.replace("sms:", ""));
                sendIntent.setType("vnd.android-dir/mms-sms");
                AuthActivity.this.getContext().startActivity(sendIntent);
                return true;
            }
            return super.shouldOverrideUrlLoading(view, url);
        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description,
                                    String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);
            mListener.onWeiboException(new WeiboException(description));
            boolean err = false;
            try {
                Looper.prepare();
                Toast.makeText(getContext(), "新浪微博登录失败", Toast.LENGTH_SHORT).show();
                Looper.loop();
            } catch (Exception e) {
                err = true;
            } finally {
                if (err) {
                    Toast.makeText(getContext(), "新浪微博登录失败", Toast.LENGTH_SHORT).show();
                }
            }
            AuthActivity.this.dismiss();
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            Log.d(TAG, "onPageStarted URL: " + url);
            if (url.startsWith(mAuthInfo.getRedirectUrl())) {
                handleRedirectUrl(view, url);
                view.stopLoading();
                AuthActivity.this.dismiss();
                return;
            }
            super.onPageStarted(view, url, favicon);
            mSpinner.show();
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            Log.d(TAG, "onPageFinished URL: " + url);
            super.onPageFinished(view, url);
            if (mSpinner.isShowing()) {
                mSpinner.dismiss();
            }
            mWebView.setVisibility(View.VISIBLE);
        }

        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            handler.proceed();
        }

    }

    private void handleRedirectUrl(WebView view, String url) {
        Bundle values = Utility.parseUrl(url);

        String error = values.getString("error");
        String error_code = values.getString("error_code");

        if (error == null && error_code == null) {
            mListener.onComplete(values);
        } else if (error.equals("access_denied")) {
            // 用户或授权服务器拒绝授予数据访问权限
            mListener.onCancel();
        } else {
            mListener.onWeiboException(new WeiboException(error));
        }
    }

    private String getUrl(int type) {
        WeiboParameters requestParams = new WeiboParameters();
        requestParams.put("client_id", this.mAuthInfo.getAppKey());
        requestParams.put("redirect_uri", this.mAuthInfo.getRedirectUrl());
        requestParams.put("scope", this.mAuthInfo.getScope());
        requestParams.put("response_type", "code");
        requestParams.put("display", "mobile");
        if (1 == type) {
            requestParams.put("packagename", this.mAuthInfo.getPackageName());
            requestParams.put("key_hash", this.mAuthInfo.getKeyHash());
        }
        String url = "https://open.weibo.cn/oauth2/authorize?" + requestParams.encodeUrl();
        return url;
    }

}