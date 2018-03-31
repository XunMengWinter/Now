package top.wefor.now.utils;

import android.text.TextUtils;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.Interceptor;
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
    public static Retrofit.Builder get(String baseUrl) {
        return get(baseUrl, null);
    }

    public static Retrofit.Builder get(String baseUrl, Cache cache) {
        OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder()
                .connectTimeout(5, TimeUnit.SECONDS);
        if (cache != null) {
            /*TODO 缓存优化（缓存不限时）*/
            httpClientBuilder.cache(cache);
            httpClientBuilder.addNetworkInterceptor(new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Request request = chain.request();
                    Response response = chain.proceed(request);

                    String cacheControl = request.cacheControl().toString();
                    if (TextUtils.isEmpty(cacheControl)) {
                        int cacheSeconds = 60 * 60 * 24 * 365;
                        cacheControl = "public, max-age=" + cacheSeconds;
                    }
                    return response.newBuilder()
                            .header("Cache-Control", cacheControl)
                            .removeHeader("Pragma")
                            .build();
                }
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
