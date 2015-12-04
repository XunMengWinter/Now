package top.wefor.now;

/*
 * Thanks to
 * https://github.com/izzyleung/ZhihuDailyPurify/wiki/知乎日报-API-分析
 * Author: izzyleung
 */
public final class ZhihuApi {

    // getDailyNews GET
    public static String getDailyNews(String date) {
        return Urls.ZHIHU_DAILY_NEWS + date;
    }

    // getDailyNewsContent GET
    public static String getNewsContent(int id) {
        return Urls.ZHIHU_DAILY_NEWS_CONTENT + id;
    }

}
