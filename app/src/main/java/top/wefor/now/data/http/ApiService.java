package top.wefor.now.data.http;

import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;
import top.wefor.now.data.model.GankMeizhiResult;
import top.wefor.now.data.model.ZhihuDailyResult;

/**
 * Created by ice on 3/13/16.
 */
public interface ApiService {

    @GET("/api/4/news/before/{date}")
    Observable<ZhihuDailyResult> getZhihuDaily(@Path("date") String date);

    @GET("/api/search/query/listview/category/福利/count/6/page/1")
    Observable<GankMeizhiResult> getGankMeizhi();

}
