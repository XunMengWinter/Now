package top.wefor.now.utils;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.view.ViewConfiguration;
import android.view.WindowManager;

import java.lang.reflect.Method;

import top.wefor.now.NowApplication;

/**
 * Created by ice on 2/2/16.
 */
public class UIHelper {

    private final static String SHOW_NAV_BAR_RES_NAME = "config_showNavigationBar";

    private static String getNavBarOverride() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WindowManager wm = (WindowManager) NowApplication.sContext.getSystemService(Context.WINDOW_SERVICE);
            try {
                // 相当于SystemProperties.get
                Class c = Class.forName("android.os.SystemProperties");
                Method m = c.getDeclaredMethod("get", String.class);
                m.setAccessible(true);
                return (String) m.invoke(null, "qemu.hw.mainkeys");
            } catch (Throwable e) {
                return null;
            }
        }
        return null;
    }

    @TargetApi(14)
    private static boolean hasNavBar() {
        Resources res = NowApplication.sContext.getResources();
        // resources.getIdentifier() 方法可以获取指定报名下的资源文件ID，后两个参数表示资源类型和默认报名
        int resourceId = res.getIdentifier(SHOW_NAV_BAR_RES_NAME, "bool", "android");
        if (resourceId != 0) {
            if ("1".equals(getNavBarOverride())) {
                return false;
            } else if ("0".equals(getNavBarOverride())) {
                return true;
            }
            return false;
        } else {
            return !ViewConfiguration.get(NowApplication.sContext).hasPermanentMenuKey();
        }
    }


    public static int getNavigationBarHeight() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT)
            return 0;

        if (!hasNavBar()) return 0;

        Resources resources = NowApplication.sContext.getResources();
        int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
        if (resourceId > 0)
            return resources.getDimensionPixelSize(resourceId);
        return 0;
    }

    public static int getStatusBarHeight() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT)
            return 0;

        Resources resources = NowApplication.sContext.getResources();
        int resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0)
            return resources.getDimensionPixelSize(resourceId);
        return 0;
    }
}
