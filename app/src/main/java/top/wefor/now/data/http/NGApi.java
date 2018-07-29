package top.wefor.now.data.http;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import top.wefor.now.data.model.Mono;
import top.wefor.now.data.model.MonoTea;
import top.wefor.now.data.model.MonoToken;
import top.wefor.now.data.model.NGMainsResult;

/**
 * Created on 2018/7/29.
 *
 * @author ice
 */
public interface NGApi {

    // curl -H 'Host: dili.bdatu.com' --compressed 'http://dili.bdatu.com/jiekou/mains/p1.html'
    @POST("/jiekou/mains/p1.html")
    @Headers({"HOST: dili.bdatu.com"})
    Observable<NGMainsResult> getMains();


    // curl -H 'Host: dili.bdatu.com' --compressed 'http://dili.bdatu.com/jiekou/albums/a2037.html'
    @GET("jiekou/albums/a{albumId}.html") //date:2018-06-12
    @Headers({"HOST: dili.bdatu.com"})
    Observable<MonoTea> getAlbum(@Path("albumId") String albumId);
}
