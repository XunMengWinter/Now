package top.wefor.now.data.http;

/**
 * Created by ice on 15/11/22.
 */
public class Urls {

    public static final String BASE = "";

    public static final String ZHIHU_NEWS = "http://news.at.zhihu.com/";
    public static final String ZHIHU_DAILY_NEWS_CONTENT = "http://daily.zhihu.com/story/";

    public static final String ZCOOL_URL = "http://m.zcool.com.cn/works/";
    // Deprecated at 2018/07/18
    public static final String NG_BASE_URL_OLD = "http://m.nationalgeographic.com.cn/";
//    public static final String NG_BASE_URL = "http://dili.bdatu.com/";
    public static final String NG_BASE_URL = "http://m.ngchina.com.cn/";
    public static final String NG_PHOTO_OF_THE_DAY_URL = "http://m.ngchina.com.cn/photography/photo_of_the_day/";
    public static final String MOMENT_URL = "http://moment.douban.com/app/";

    public static final String GANK = "http://gank.io/";

    public static final String AUTH_ENDPOINT = "https://api.dribbble.com/v2/";

    public static final String MONO = "http://mmmono.com/";


    public static String getNgUrl(String url){
        String httpUrl = url + "";
        if (!httpUrl.contains("http://") && !httpUrl.contains("https://")) {
            httpUrl = Urls.NG_BASE_URL + httpUrl;
        }
        return httpUrl;
    }
}
