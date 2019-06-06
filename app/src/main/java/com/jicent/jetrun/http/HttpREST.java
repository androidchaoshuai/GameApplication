package com.jicent.jetrun.http;

import android.os.Handler;
import android.os.Message;
import com.alibaba.fastjson.JSONArray;
import com.jicent.jetrun.utils.LogUtils;
import okhttp3.*;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;
import so.asch.sdk.impl.ParameterMap;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static com.jicent.jetrun.utils.TransactionUtils.magic;


public final class HttpREST {
    private static final OkHttpClient mOkHttpClient = new OkHttpClient();
    private static String TAG = "HttpREST";
    private static final String CHARSET_NAME = "UTF-8";
    public static final int REQUEST_SUCCESS = 1;
    public static final int REQUEST_FAIL = 2;

    public static void getTransactionConstant(String fullUrl, Object[] methodArgs, Handler mHandler){
        JSONArray arr = new JSONArray();
        for (int i = 0; i < methodArgs.length; i++) {
            arr.add(methodArgs[i]);// + ",";
        }

        Map mapHeader = new HashMap<>();
        mapHeader.put("magic", magic);
        mapHeader.put("Content-Type", "application/json");

        String args = arr.toJSONString();
        postJsonContent(fullUrl, args, mapHeader, mHandler);
    }

    public static void getStringFromServer(String fullUrl, Handler resultHandler) {
        Request request = new Request.Builder().url(fullUrl).build();
        getStringFromServer(request, resultHandler);
    }

    public static void getStringFromServer(String url, List<BasicNameValuePair> param, Handler resultHandler){
        Request request = new Request.Builder().url(attachHttpGetParams(url, param)).build();
        getStringFromServer(request, resultHandler);
    }

    private static void getStringFromServer(Request request, Handler resultHandler){
        Runnable requestTask = new Runnable() {
            @Override
            public void run() {
                mOkHttpClient.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Message msg = resultHandler.obtainMessage();
                        LogUtils.d(TAG, "IOException: " + e.toString());
                        msg.what = REQUEST_FAIL;
                        msg.sendToTarget();
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        Message msg = resultHandler.obtainMessage();
                        if(response.isSuccessful()){
                            msg.what = REQUEST_SUCCESS;
                            msg.obj = response.body().string();
                        } else {
                            LogUtils.d(TAG, "IOException: " + response.toString());
                            msg.what = REQUEST_FAIL;
                        }
                        msg.sendToTarget();
                    }
                });
            }
        };


        Thread requestThread = new Thread(requestTask);
        requestThread.start();
    }


    /**
     * 这里使用了HttpClinet的API。只是为了方便
     * @param params
     * @return
     */
    private static String formatParams(List<BasicNameValuePair> params){
        return URLEncodedUtils.format(params, CHARSET_NAME);
    }
    /**
     * 为HttpGet 的 url 方便的添加多个name value 参数。
     * @param url
     * @param params
     * @return
     */
    private static String attachHttpGetParams(String url, List<BasicNameValuePair> params){
        return url + "?" + formatParams(params);
    }

    public static void postJsonContent(String url, String parameters, Map<String, String> customeHeads, Handler resultHandler) {
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(JSON, parameters);

        Request.Builder requestBuilder = new Request.Builder().url(url);
        if(customeHeads != null){
            for(Iterator iter=customeHeads.entrySet().iterator();iter.hasNext();){
                Map.Entry element=(Map.Entry)iter.next();
                String strKey = (String)element.getKey();
                String value=(String)element.getValue();
                requestBuilder.addHeader(strKey, value);
            }
        }
        Runnable requestTask = new Runnable() {
            @Override
            public void run() {
                Request request = requestBuilder.post(body).build();
                mOkHttpClient.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Message msg = resultHandler.obtainMessage();
                        msg.what = REQUEST_FAIL;
                        msg.sendToTarget();
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        Message msg = resultHandler.obtainMessage();
                        if(response.isSuccessful()){
                            msg.what = REQUEST_SUCCESS;
                            msg.obj = response.body().string();
                        } else {
                            msg.what = REQUEST_FAIL;
                        }
                        msg.sendToTarget();
                    }
                });
            }
        };

        Thread requestThread = new Thread(requestTask);
        requestThread.start();
    }

    public static void post(String url, ParameterMap parameters, Map<String, String> customeHeads, Handler resultHandler) throws IOException {
        FormBody.Builder formBody = new FormBody.Builder();

        if(parameters != null){
            for(Iterator iter=parameters.iterator();iter.hasNext();){
                Map.Entry element=(Map.Entry)iter.next();
                String strKey = (String)element.getKey();
                String value=(String)element.getValue();
                formBody.add(strKey, value);//用相同的key替代过滤这个key的值
            }
        }
        Request.Builder requestBuilder = new Request.Builder().url(url);
        if(customeHeads != null){
            for(Iterator iter=customeHeads.entrySet().iterator();iter.hasNext();){
                Map.Entry element=(Map.Entry)iter.next();
                String strKey = (String)element.getKey();
                String value=(String)element.getValue();
                requestBuilder.addHeader(strKey, value);
            }
        }

        Runnable requestTask = new Runnable() {
            @Override
            public void run() {
                Message msg = resultHandler.obtainMessage();

                Request request = requestBuilder.post(formBody.build()).build();
                mOkHttpClient.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Message msg = resultHandler.obtainMessage();
                        msg.what = REQUEST_FAIL;
                        msg.sendToTarget();
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        Message msg = resultHandler.obtainMessage();
                        if(response.isSuccessful()){
                            msg.what = REQUEST_SUCCESS;
                            msg.obj = response.body().string();
                        } else {
                            msg.what = REQUEST_FAIL;
                        }
                        msg.sendToTarget();
                    }
                });
            }
        };

        Thread requestThread = new Thread(requestTask);
        requestThread.start();
    }
}

