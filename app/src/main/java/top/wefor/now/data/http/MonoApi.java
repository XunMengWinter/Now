package top.wefor.now.data.http;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.HEAD;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Part;
import top.wefor.now.data.model.GankMeizhiResult;

/**
 * Created on 2018/6/9.
 *
 * @author ice
 */
public interface MonoApi {

//    curl -H 'HOST: mmmono.com' -H 'Content-Type: application/json; charset=UTF-8' --data-binary '{"ts":"67ZE3/cAOa19yMFAHaWFV7T8fFWdtX3GoX+Nv3CD0x24iEoHvFVZThUKuUUISxBI"}' --compressed 'http://mmmono.com/api/v3/accountsv2/welcome/966c-1d46-1f80-52ee/'
@POST("/api/v3/accountsv2/welcome/966c-1d46-1f80-52ee/")
@Headers({"HOST: mmmono.com", "Content-Type: application/json; charset=UTF-8"})
Observable<String> getToken(@Body String data); // data = "--data-binary '..."
}
