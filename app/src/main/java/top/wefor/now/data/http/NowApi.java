package top.wefor.now.data.http;

import com.orhanobut.logger.Logger;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.http.Path;
import top.wefor.now.data.model.GankDailyResult;
import top.wefor.now.data.model.GankMeizhiResult;
import top.wefor.now.data.model.ZhihuDailyResult;
import top.wefor.now.utils.RetrofitUtil;

/*
 * Thanks to
 *
 * https://github.com/izzyleung/ZhihuDailyPurify/wiki/知乎日报-API-分析
 *
 * http://gank.io
 *
 */
public final class NowApi implements ApiService {

    @Override
    public Observable<ZhihuDailyResult> getZhihuDaily(String date) {
        Logger.i(date);
        return RetrofitUtil.getApi(Urls.ZHIHU_NEWS).getZhihuDaily(date)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    // getDailyNewsContent GET
    public static String getNewsContent(int id) {
        return Urls.ZHIHU_DAILY_NEWS_CONTENT + id;
    }


    @Override
    public Observable<GankMeizhiResult> getGankMeizhi() {
        return RetrofitUtil.getApi(Urls.GANK).getGankMeizhi()
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<GankDailyResult> getGankDaily(@Path("date") String date) {
        return RetrofitUtil.getApi(Urls.GANK).getGankDaily(date)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }


}
