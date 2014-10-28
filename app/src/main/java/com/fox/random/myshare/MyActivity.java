package com.fox.random.myshare;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.fox.random.SinaPlatform;
import com.sina.weibo.sdk.constant.WBConstants;

import fox.random.core.ShareSDK;
import fox.random.core.api.params.BaseShareParams;
import fox.random.core.api.params.ShareParams;
import fox.random.core.callback.AuthListener;
import fox.random.core.callback.ShareListener;
import fox.random.core.constants.SNS;
import fox.random.core.exception.SnsException;


public class MyActivity extends ActionBarActivity implements View.OnClickListener {
    private String TAG = "myShare";
    private SinaPlatform sinaPlatform;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);

        initView();

        sinaPlatform = (SinaPlatform) ShareSDK.getPlatform(SNS.SINA,"1316579902");

        if (savedInstanceState != null && sinaPlatform !=null)
            sinaPlatform.handleWeiboResponse(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (sinaPlatform != null)
            sinaPlatform.handleWeiboResponse(intent);
    }

    private void initView(){
        findViewById(R.id.sina_login).setOnClickListener(this);
        findViewById(R.id.sina_share).setOnClickListener(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        sinaPlatform.authorizeCallBack(requestCode,resultCode,data);
        super.onActivityResult(requestCode, resultCode, data);
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.sina_login:
                sinaLogin();
                break;
            case R.id.sina_share:
                sinaShare();
                break;
        }
    }

    private void sinaLogin(){
        sinaPlatform.setSSO(true);
        ShareSDK.doOauthVerify(this, sinaPlatform, new AuthListener() {
            @Override
            public void onStart() {
                Log.d(TAG, "onStart");
            }

            @Override
            public void onComplete(Bundle bundle, SNS sns) {
                Log.d(TAG, bundle.toString());
                Log.d(TAG, "onComplete");
            }

            @Override
            public void onError(SnsException e, SNS sns) {
                Log.d(TAG, "onError");
            }

            @Override
            public void onCancel(SNS sns) {
                Log.d(TAG, "onCancel");
            }
        });
    }

    private void sinaShare(){
        BaseShareParams baseShareParams = new BaseShareParams();
        baseShareParams.setContent("测试一下");

        ShareParams shareParams = new ShareParams();
        shareParams.setSns(SNS.SINA);
        shareParams.setBaseShareParams(baseShareParams);


        ShareSDK.share(this,shareParams,new ShareListener() {
            @Override
            public void success(SNS sns) {

            }

            @Override
            public void error(Throwable e, SNS sns) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.my, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
