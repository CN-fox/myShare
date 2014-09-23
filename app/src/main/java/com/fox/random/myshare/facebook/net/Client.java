package com.fox.random.myshare.facebook.net;

import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;

import android.os.AsyncTask;

import org.apache.http.client.HttpClient;

/**
 * Created by w_q on 14-9-21.
 */
public class Client extends AsyncTask {
    @Override
    protected Object doInBackground(Object[] params) {
        HttpClient client = createHttpClient();
        return null;
    }

    /**
     * 创建httpclient实例
     *
     * @return
     */
    private final HttpClient createHttpClient() {
        HttpParams params = new BasicHttpParams();
        params.setParameter(CoreConnectionPNames.SO_TIMEOUT, 20000);// 设置数据等待超时
        params.setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 10000); // 设置连接超时
        HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
        HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);
        HttpProtocolParams.setUseExpectContinue(params, true);
        SchemeRegistry schReg = new SchemeRegistry();
        schReg.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
        schReg.register(new Scheme("https", SSLSocketFactory.getSocketFactory(), 443));
        ClientConnectionManager connMgr = new ThreadSafeClientConnManager(params, schReg);
        return new DefaultHttpClient(connMgr, params);
    }
}
