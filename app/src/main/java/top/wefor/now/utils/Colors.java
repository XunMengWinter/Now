package top.wefor.now.utils;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import java.util.Random;

import top.wefor.now.NowApplication;
import top.wefor.now.R;

/**
 * Created by ice on 15/10/29.
 */
public class Colors {

    private static final Double R_LIGHT = 0.299, G_LIGHT = 0.587, B_LIGHT = 0.114;
    private static Random sRandom = new Random();

    /**
     * 传入一个明度，返回该明度的一个随机颜色
     *
     * @return
     */
    public static ColorDrawable getRandomColor(int light) {
        int nextLight = light;
        int r, g, b;
        b = sRandom.nextInt(255);
        nextLight -= b * B_LIGHT;
        if (R_LIGHT * 255 + G_LIGHT * 255 < nextLight) {
            r = g = 255;
        } else {
            int miniLight = (int) (nextLight - G_LIGHT * 255);
            if (miniLight > 0) {
                int miniR = (int) (miniLight / 0.299);
                r = miniR + sRandom.nextInt(255 - miniR);
            } else {
                r = sRandom.nextInt(255);
            }
            g = (int) ((nextLight - r * R_LIGHT) / 0.587);
        }

        return new ColorDrawable(Color.rgb(r, g, b));
    }

    public static int getRandomColorFromArray(int colorArray) {
        int[] colors = NowApplication.sResources.getIntArray(colorArray);
        int i = sRandom.nextInt(colors.length);
        return colors[i];
    }

    public static int softColor() {
        int[] colors = NowApplication.sResources.getIntArray(R.array.soft_colors);
        int i = sRandom.nextInt(colors.length);
        return colors[i];
    }

    /**
     * 随机生成一个颜色值
     *
     * @return
     */
    public static int getRandomColorInt() {
        Random rnd = new Random();
        int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
        return color;
    }

}
