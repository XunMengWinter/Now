package top.wefor.now.utils;

import android.content.SharedPreferences;

import java.util.Date;

import top.wefor.now.Constants;
import top.wefor.now.NowApp;

/**
 * Created on 16/6/21 16:32.
 *
 * @author ice, GitHub: https://github.com/XunMengWinter
 */
public class PrefUtil {

    public static boolean isNeedRefresh(String key) {
        SharedPreferences sharedPreferences = NowApp.getInstance()
                .getSharedPreferences(Constants.PREFS_NAME, 0);
        long refreshTime = sharedPreferences.getLong(Constants.KEY_REFRESH_TIME_ZCOOL, 0);
        return (new Date().getTime() - refreshTime > Constants.VALUE_REFRESH_INTERVAL);
    }

    public static void setRefreshTime(String key, long time) {
        SharedPreferences sharedPreferences = NowApp.getInstance()
                .getSharedPreferences(Constants.PREFS_NAME, 0);
        sharedPreferences.edit().putLong(key, time).apply();
    }

}
