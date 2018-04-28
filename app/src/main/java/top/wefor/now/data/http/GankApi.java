package top.wefor.now.data.http;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;
import top.wefor.now.data.model.GankDailyResult;
import top.wefor.now.data.model.GankMeizhiResult;

/**
 * Thanks
 * http://gank.io
 * <p>
 * Created by ice on 3/13/16.
 */
public interface GankApi {

    @GET("/api/random/data/福利/7")
    Observable<GankMeizhiResult> getGankMeizhi();

    /* date = yyyy/MM/dd */
    @GET("/api/day/{date}")
    Observable<GankDailyResult> getGankDaily(@Path("date") String date);

//    @POST("/api/collect")
//    Observable<NowCollectResult> postCollect(@Body NowCollectRequest nowCollectRequest);
}
