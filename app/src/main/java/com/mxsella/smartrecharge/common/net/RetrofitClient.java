package com.mxsella.smartrecharge.common.net;

import com.mxsella.smartrecharge.MyApplication;
import com.mxsella.smartrecharge.common.Config;
import com.mxsella.smartrecharge.common.net.interceptor.TokenInterceptor;
import com.mxsella.smartrecharge.utils.LogUtil;

import java.io.File;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private final ApiService apiService;
    private static final long WRITE_TIMEOUT = 30L;
    private static final long READ_TIMEOUT = 30L;
    private static final long CONNECT_TIMEOUT = 6L;
    private static final String CACHE_DIR = "HttpCache";
    private static final long MAX_CACHE_SIZE = 1024 * 1024 * 1024;

    private RetrofitClient() {
        final Cache cache = new Cache(new File(MyApplication.getInstance().getCacheDir(), CACHE_DIR),
                MAX_CACHE_SIZE);

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .cache(cache)
                .addInterceptor(new TokenInterceptor())
                .addInterceptor(getLoggingInterceptor())
                .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
                .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiService.BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .build();

        apiService = retrofit.create(ApiService.class);
    }

    private HttpLoggingInterceptor getLoggingInterceptor() {
        HttpLoggingInterceptor.Level level;
        if (Config.isDebug) {
            level = HttpLoggingInterceptor.Level.BODY;
        } else {
            level = HttpLoggingInterceptor.Level.HEADERS;
        }

        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(message -> {
            LogUtil.i("Api -> " + message);
        });

        loggingInterceptor.setLevel(level);

        return loggingInterceptor;
    }

    private static final class RetrofitClientHolder {
        static final RetrofitClient instance = new RetrofitClient();
    }

    public static ApiService getInstance() {
        return RetrofitClientHolder.instance.apiService;
    }
}
