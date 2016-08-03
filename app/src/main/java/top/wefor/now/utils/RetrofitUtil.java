package top.wefor.now.utils;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import top.wefor.now.data.http.ApiService;
import top.wefor.now.data.http.Urls;

/**
 * Created by ice on 3/3/16.
 */
public class RetrofitUtil {
    public static Retrofit.Builder get(String baseUrl) {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(5, TimeUnit.SECONDS)
                .build();
        Retrofit.Builder builder = new Retrofit.Builder();
        builder.client(okHttpClient)
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create());

        return builder;
    }

    public static ApiService getApi(String baseUrl) {
        return get(baseUrl).build().create(ApiService.class);
    }
}
