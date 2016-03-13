package top.wefor.now.http;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import top.wefor.now.model.ZhihuDailyResult;
import top.wefor.now.utils.RetrofitUtil;

/*
 * Thanks to
 * https://github.com/izzyleung/ZhihuDailyPurify/wiki/知乎日报-API-分析
 * Author: izzyleung
 */
public final class ZhihuApi {

    // getDailyNews GET
    public Observable<ZhihuDailyResult> getDailyNews(String date) {
        return RetrofitUtil.getApi(Urls.ZHIHU_NEWS).getZhihuDaily(date)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    // getDailyNewsContent GET
    public static String getNewsContent(int id) {
        return Urls.ZHIHU_DAILY_NEWS_CONTENT + id;
    }

}
