package top.wefor.now;

import android.app.Application;
import android.content.Context;
import android.content.res.Resources;

/**
 * Created by tangqi on 7/20/15.
 */
public class NowApplication extends Application {

    public static Context sContext;
    public static Resources sResources;
    private static Integer width, height;

    @Override
    public void onCreate() {
        super.onCreate();
        sContext = this;
        sResources = sContext.getResources();
    }



    public static Integer getWidth() {
        return width == null ? 0 : width;
    }

    public static void setWidth(Integer width) {
        NowApplication.width = width;
    }

    public static Integer getHeight() {
        return height == null ? 0 : height;
    }

    public static void setHeight(Integer height) {
        NowApplication.height = height;
    }
}
