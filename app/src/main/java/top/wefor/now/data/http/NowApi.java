package top.wefor.now.data.http;

import retrofit2.http.Path;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import top.wefor.now.data.model.BDImgResult;
import top.wefor.now.data.model.ZhihuDailyResult;
import top.wefor.now.utils.RetrofitUtil;

/*
 * Thanks to
 * https://github.com/izzyleung/ZhihuDailyPurify/wiki/知乎日报-API-分析
 * Author: izzyleung
 */
public final class NowApi implements ApiService {

    // getZhihuDaily GET
    @Override
    public Observable<ZhihuDailyResult> getZhihuDaily(String date) {
        return RetrofitUtil.getApi(Urls.ZHIHU_NEWS).getZhihuDaily(date)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    // getDailyNewsContent GET
    public static String getNewsContent(int id) {
        return Urls.ZHIHU_DAILY_NEWS_CONTENT + id;
    }

    @Override
    public Observable<BDImgResult> getBDImage(@Path("page") int page) {
        return RetrofitUtil.getApi(Urls.BAIDU).getBDImage(page)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }
}
