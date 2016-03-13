package top.wefor.now;

import android.app.Application;
import android.content.Context;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.DisplayMetrics;

import top.wefor.now.utils.Toaster;

/**
 * Created by tangqi on 7/20/15.
 */
public class NowApplication extends Application {

    public static Context sContext;
    public static Resources sResources;

    private static Toaster sToaster;

    @Override
    public void onCreate() {
        super.onCreate();
        sContext = this;
        sToaster = new Toaster();
        sResources = sContext.getResources();
    }

    public static void showToast(String msg) {
        sToaster.showToast(msg);
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
