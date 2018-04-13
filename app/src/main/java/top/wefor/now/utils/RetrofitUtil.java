package top.wefor.now.utils;

import android.text.TextUtils;

import com.orhanobut.logger.Logger;

import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import top.wefor.now.data.http.ApiService;

/**
 * Created by ice on 3/3/16.
 */
public class RetrofitUtil {
    public static final String TAG = "RetrofitUtil";

    public static Retrofit.Builder get(String baseUrl) {
        return get(baseUrl, null);
    }

    public static Retrofit.Builder get(String baseUrl, Cache cache) {
        OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder()
                .connectTimeout(5, TimeUnit.SECONDS);
        if (cache != null) {
            /*TODO 缓存优化（缓存不限时）*/
            httpClientBuilder.cache(cache);
            httpClientBuilder.addInterceptor(chain -> {
                Request request = chain.request();
                Logger.i(TAG, "url: " + request.url().toString());
                return chain.proceed(request);
            });
            httpClientBuilder.addNetworkInterceptor(chain -> {
                Request request = chain.request();
                String cacheControl = request.cacheControl().toString();
                if (TextUtils.isEmpty(cacheControl)) {
                    int cacheSeconds = 60 * 60 * 24 * 365;
                    cacheControl = "public, max-age=" + cacheSeconds;
                }
                Response response = chain.proceed(request);
                return response.newBuilder()
                        .header("Cache-Control", cacheControl)
                        .build();
            });
        }
        Retrofit.Builder builder = new Retrofit.Builder();
        builder.client(httpClientBuilder.build())
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
