package top.wefor.now.utils;

import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import top.wefor.now.data.http.ApiService;

/**
 * Created by ice on 3/3/16.
 */
public class RetrofitUtil {
    public static Retrofit.Builder get(String baseUrl) {
        return get(baseUrl, null);
    }

    public static Retrofit.Builder get(String baseUrl, Cache cache) {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(5, TimeUnit.SECONDS)
                .cache(cache)
                .build();
        Retrofit.Builder builder = new Retrofit.Builder();
        builder.client(okHttpClient)
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create());

        return builder;
    }

    public static ApiService getApi(String baseUrl) {
        return getApi(baseUrl, null);
    }

    public static ApiService getApi(String baseUrl, Cache cache) {
        return get(baseUrl, cache).build().create(ApiService.class);
    }
}
