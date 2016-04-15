package top.wefor.now;

import android.app.Application;

import com.facebook.drawee.backends.pipeline.Fresco;

import top.wefor.now.utils.Toaster;

/**
 * Created by tangqi on 7/20/15.
 */
public class NowApp extends Application {

    private static NowApp sNowApp;

    private static Toaster sToaster;

    public static NowApp getInstance() {
        if (sNowApp == null) {
            sNowApp = new NowApp();
        }
        return sNowApp;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sNowApp = this;
        Fresco.initialize(getApplicationContext());
    }

    public static void showToast(String msg) {
        if (sToaster == null)
            sToaster = new Toaster();
        sToaster.showToast(msg);
    }

}
