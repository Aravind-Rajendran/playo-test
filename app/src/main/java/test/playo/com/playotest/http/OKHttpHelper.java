package test.playo.com.playotest.http;

import android.content.Context;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;



public class OKHttpHelper {


    private static OkHttpClient okHttpClient;


    public static OkHttpClient getInstanceOf(Context context) {
        if (okHttpClient == null)
            okHttpClient = getOkHttpClient(context);

        return okHttpClient;
    }

    private static OkHttpClient getOkHttpClient(Context context) {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BASIC);
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(1, TimeUnit.MINUTES)
                .addInterceptor(logging)
                .build();
        okHttpClient.retryOnConnectionFailure();
        return okHttpClient;
    }



}
