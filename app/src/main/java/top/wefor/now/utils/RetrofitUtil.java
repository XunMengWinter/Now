package top.wefor.now.utils;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import top.wefor.now.http.ApiService;
import top.wefor.now.http.Urls;

/**
 * Created by ice on 3/3/16.
 */
public class RetrofitUtil {
    public static Retrofit.Builder get(String baseUrl) {
        Retrofit.Builder builder = new Retrofit.Builder();
        builder.client(new OkHttpClient())
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create());

        return builder;
    }

    public static ApiService getApi(String baseUrl) {
        return get(baseUrl).build().create(ApiService.class);
    }
}
