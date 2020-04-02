package com.example.trivia.controller;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.util.LruCache;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

public class AppController extends Application {

    public static final String TAG = AppController.class.getSimpleName();
    private RequestQueue mRequestqueue;
    private static AppController mInstance;

    public static AppController getInstance(){
        if(mInstance==null)
        {
            synchronized (AppController.class)
            {
                if(mInstance==null)
                    mInstance=new AppController();
            }
        }
        return mInstance;
    }

    @Override
    public void onCreate()
    {
        super.onCreate();
        mInstance=this;
    }
    public RequestQueue getRequestQueue() {
        if(mRequestqueue==null)
            mRequestqueue=Volley.newRequestQueue(getApplicationContext());
        return mRequestqueue;
    }
    public <T> void addToRequestQueue(Request<T> req,String tag)
    {
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }
    public <T> void addToRequestQueue(Request<T> req)
    {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }
    public void cancelPendingRequests(Object tag)
    {
        if(mRequestqueue!=null)
            mRequestqueue.cancelAll(tag);
    }
}
