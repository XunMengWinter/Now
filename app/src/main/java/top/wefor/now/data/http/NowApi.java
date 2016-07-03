package top.wefor.now.data.http;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import top.wefor.now.data.model.GankMeizhiResult;
import top.wefor.now.data.model.ZhihuDailyResult;
import top.wefor.now.utils.RetrofitUtil;

/*
 * Thanks to
 * https://github.com/izzyleung/ZhihuDailyPurify/wiki/知乎日报-API-分析
 * Author: izzyleung
 */
public final class NowApi {

    // getZhihuDaily GET
    public Observable<ZhihuDailyResult> getZhihuDaily(String date) {
        return RetrofitUtil.getApi(Urls.ZHIHU_NEWS).getZhihuDaily(date)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    // getDailyNewsContent GET
    public static String getNewsContent(int id) {
        return Urls.ZHIHU_DAILY_NEWS_CONTENT + id;
    }

    public Observable<GankMeizhiResult> getGankMeizhi() {
        return RetrofitUtil.getApi(Urls.GANK).getGankMeizhi()
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

}
