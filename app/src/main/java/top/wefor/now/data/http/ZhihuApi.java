package top.wefor.now.data.http;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;
import top.wefor.now.data.model.ZhihuDailyResult;

/**
 * thanks
 * https://github.com/izzyleung/ZhihuDailyPurify/wiki/知乎日报-API-分析
 * <p>
 * Created by ice on 3/13/16.
 */
public interface ZhihuApi {

    @GET("/api/4/news/before/{date}")
    Observable<ZhihuDailyResult> getZhihuDaily(@Path("date") String date);

//    @POST("/api/collect")
//    Observable<NowCollectResult> postCollect(@Body NowCollectRequest nowCollectRequest);
}
