package com.fox.random.myshare;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.fox.random.SinaPlatform;

import fox.random.core.ShareSDK;
import fox.random.core.callback.AuthListener;
import fox.random.core.constants.SNS;
import fox.random.core.exception.SnsException;


public class MyActivity extends ActionBarActivity {
    private String TAG = "myShare";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);
        SinaPlatform sinaPlatform = new SinaPlatform("1316579902");
        sinaPlatform.setSSO(true);
        ShareSDK.doOauthVerify(this,sinaPlatform,new AuthListener() {
            @Override
            public void onStart() {
                Log.d(TAG,"onStart");
            }

            @Override
            public void onComplete(Bundle bundle, SNS sns) {
                Log.d(TAG,"onComplete");
            }

            @Override
            public void onError(SnsException e, SNS sns) {
                Log.d(TAG,"onError");
            }

            @Override
            public void onCancel(SNS sns) {
                Log.d(TAG,"onCancel");
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
