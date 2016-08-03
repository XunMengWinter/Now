package top.wefor.now.data.http;

import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;
import top.wefor.now.data.model.GankMeizhiResult;
import top.wefor.now.data.model.GankDailyResult;
import top.wefor.now.data.model.ZhihuDailyResult;

/**
 * Created by ice on 3/13/16.
 */
public interface ApiService {

    @GET("/api/4/news/before/{date}")
    Observable<ZhihuDailyResult> getZhihuDaily(@Path("date") String date);

    @GET("/api/random/data/福利/7")
    Observable<GankMeizhiResult> getGankMeizhi();

    /* date = yyyy/MM/dd */
    @GET("/api/day/{date}")
    Observable<GankDailyResult> getGankDaily(@Path("date") String date);

}
