package com.wq.businessdirectory.common.net;

import android.util.Log;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.wq.support.utils.DateUtil;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import io.realm.RealmObject;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * Created by Administrator on 2016/9/25.
 */
public class APIManager {
    static Retrofit retrofit;
    private static final Gson gson = new GsonBuilder()
            .setDateFormat(DateUtil.dateFormatYMDHMS)
            .setExclusionStrategies(new ExclusionStrategy() {
                @Override
                public boolean shouldSkipField(FieldAttributes f) {
                    return f.getDeclaringClass().equals(RealmObject.class);
                }

                @Override
                public boolean shouldSkipClass(Class<?> clazz) {
                    return false;
                }
            })
            .create();
    static {
        OkHttpClient client = new OkHttpClient();
//        //网络拦截器
//        client.networkInterceptors().add(new Interceptor() {
//            @Override
//            public Response intercept(Chain chain) throws IOException {
//                Request request = chain.request();
//                Response response = chain.proceed(request);
//
//                Log.e("weiquan", "" + request.url()+ " " + request.headers() + "  "+response);
//                return response;
//            }
//
//        });

        retrofit = new Retrofit
                .Builder()
                .baseUrl(API.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addConverterFactory(new FileRequestBodyConverterFactory())
                .client(client)
                .build();
    }
    static  API service;
    public static API getAPI() {
        if (service == null) {
            synchronized (API.class) {
                if (service == null)
                    service = retrofit.create(API.class);
            }
        }
        return service;
    }


    static class FileRequestBodyConverterFactory extends Converter.Factory {
        @Override
        public Converter<File, RequestBody> requestBodyConverter(Type type, Annotation[] parameterAnnotations, Annotation[] methodAnnotations, Retrofit retrofit) {
            return new FileRequestBodyConverter();
        }
    }

    static class FileRequestBodyConverter implements Converter<File, RequestBody> {

        @Override
        public RequestBody convert(File file) throws IOException {
            return RequestBody.create(MediaType.parse("application/otcet-stream"), file);
        }
    }


}
