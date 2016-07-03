package top.wefor.now;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;

public class PreferencesHelper {

    private final SharedPreferences mPreferences;

    public PreferencesHelper(Context context) {
        mPreferences = context.getSharedPreferences(Constants.PREFS_NAME, Context.MODE_PRIVATE);
    }

    public void clear() {
        mPreferences.edit().clear().apply();
    }

    public boolean isFirst() {
        return mPreferences.getBoolean(Constants.IS_FIRST, true);
    }

    public void setFirst(boolean isFirst) {
        mPreferences.edit().putBoolean(Constants.IS_FIRST, isFirst).apply();
    }

    public boolean isJSEnabled() {
        return mPreferences.getBoolean(Constants.JAVA_SCRIPT_ENABLED, true);
    }

    public void setJSEnabled(boolean isJSEnabled) {
        mPreferences.edit().putBoolean(Constants.JAVA_SCRIPT_ENABLED, isJSEnabled).apply();
    }

    public int getHeadImageType() {
        return mPreferences.getInt(Constants.COVER_SOURCE, Constants.TYPE_MAC);
    }

    public void setHeadImageType(int headImageIndex) {
        mPreferences.edit().putInt(Constants.COVER_SOURCE, headImageIndex).apply();
    }

    public boolean isModuleSelected(@NonNull String name) {
        return mPreferences.getBoolean(name, true);
    }

    public void setModuleSelected(@NonNull String name, boolean isSelected) {
        mPreferences.edit().putBoolean(name, isSelected).apply();
    }

    public String getHeadImages() {
        return mPreferences.getString(Constants.HEAD_IMAGES, null);
    }

    public void setHeadImages(String headImages){
        mPreferences.edit().putString(Constants.HEAD_IMAGES, headImages).apply();
    }

    public String getCoverImage(){
        return mPreferences.getString(Constants.COVER_IMAGE, "");
    }

    public void setCoverImage(String imageUrl){
        mPreferences.edit().putString(Constants.COVER_IMAGE, imageUrl).apply();
    }

    public void setNgImages(String images){
        mPreferences.edit().putString(Constants.NG_IMAGES, images).apply();
    }

    public String getNgImages(){
        return mPreferences.getString(Constants.NG_IMAGES, "");
    }

}
