package top.wefor.now;

import android.app.Application;
import android.content.Context;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.DisplayMetrics;

/**
 * Created by tangqi on 7/20/15.
 */
public class NowApplication extends Application {

    public static Context sContext;
    public static Resources sResources;

    @Override
    public void onCreate() {
        super.onCreate();
        sContext = this;
        sResources = sContext.getResources();
    }

    public static Integer getWidth() {
        DisplayMetrics metrics = sResources.getDisplayMetrics();
        return metrics.widthPixels;
    }

    public static Integer getHeight() {
        DisplayMetrics metrics = sResources.getDisplayMetrics();
        return metrics.heightPixels;
    }

    public static boolean isWifiConnected() {
        ConnectivityManager connManager = (ConnectivityManager) sContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (networkInfo.isConnected())
            return true;
        else
            return false;
    }

}
