package top.wefor.now.data.http;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created on 2018/5/5.
 *
 * @author ice
 */
public interface DribbbleApi {

    @POST("popular_shots")
    Observable<String> getToken(
            @Query("client_id") String authCode
    );


}
