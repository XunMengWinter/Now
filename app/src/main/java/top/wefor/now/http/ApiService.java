package top.wefor.now.http;

import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;
import top.wefor.now.model.ZhihuDailyResult;

/**
 * Created by ice on 3/13/16.
 */
public interface ApiService {

    @GET("api/4/news/before/{date}")
    Observable<ZhihuDailyResult> getZhihuDaily(@Path("date") String date);

}
