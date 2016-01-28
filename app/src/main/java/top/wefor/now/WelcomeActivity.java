package top.wefor.now;

import android.annotation.TargetApi;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.File;
import java.util.Calendar;
import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;
import top.wefor.now.model.BDImg;
import top.wefor.now.model.BDImgResult;
import top.wefor.now.utils.Constants;

/**
 * Created by ice on 15/11/22.
 */
public class WelcomeActivity extends BaseCompatActivity {

    @Bind(R.id.view)
    ImageView mImageView;

    @Bind(R.id.textView)
    TextView mTextView;

    private Date mStartDate;
    final long WELCOME_TIME = 1500;
    private SharedPreferences mPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        ButterKnife.bind(this);

        mStartDate = new Date();
        mPreferences = getSharedPreferences(Constants.PREFS_NAME, 0);
        SharedPreferences.Editor editor = mPreferences.edit();
        File file = new File(mPreferences.getString(Constants.COVER_IMAGE, ""));
        Log.i("xyz img path ", mPreferences.getString(Constants.COVER_IMAGE, ""));
        if (file.exists()) Glide.with(this).load(file).into(mImageView);
        try {
            PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_ACTIVITIES);
            String version = String.format(getResources().getString(R.string.app_version), packageInfo.versionName);
            mTextView.setText(pass(version));
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        int type = mPreferences.getInt(Constants.COVER_SOURCE, 0);
        switch (type) {
            case Constants.TYPE_NG:
                if (!mPreferences.getString(Constants.HEAD_IMAGES, "").equals("")) {
                    toMainPage();
                    break;
                }
            case Constants.TYPE_BD:
                if (!NowApplication.isWifiConnected()) {
                    toMainPage();
                    break;
                }
                getCoverImgsThenToMainPage();
                break;
            case Constants.TYPE_MAC:
                if (!NowApplication.isWifiConnected()) {
                    toMainPage();
                    break;
                }
                JSONArray jsonArray = new JSONArray();
                jsonArray.add(getString(R.string.pic_url_1));
                jsonArray.add(getString(R.string.pic_url_2));
                jsonArray.add(getString(R.string.pic_url_3));
                jsonArray.add(getString(R.string.pic_url_4));
                editor.putString(Constants.HEAD_IMAGES, jsonArray.toJSONString());
                editor.apply();
                toMainPage();
                break;
            case Constants.TYPE_COLOR:
                editor.putString(Constants.HEAD_IMAGES, "");
                editor.apply();
                toMainPage();
                break;
            default:
                toMainPage();
                break;
        }

    }

    private void getCoverImgsThenToMainPage() {
        OkHttpUtils.get()
                .url(Urls.BDIMG_BASE_URL.replace("page", "" + (Calendar.getInstance().get(Calendar.DAY_OF_WEEK) + 10)))
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {

                    }

                    @Override
                    public void onResponse(String response) {
                        Gson gson = new Gson();
                        BDImgResult bdImgResult = gson.fromJson(response, BDImgResult.class);
                        if (bdImgResult.imgs != null) {
                            JSONArray jsonArray = new JSONArray();
                            for (BDImg item : bdImgResult.imgs) {
                                Log.i("xyz", "img " + item.imageUrl);
                                if (item.imageUrl != null)
                                    jsonArray.add(item.imageUrl);
                            }
                            SharedPreferences.Editor editor = mPreferences.edit();
                            editor.putString(Constants.HEAD_IMAGES, jsonArray.toJSONString());
                            editor.apply();
                            toMainPage();
                        }
                    }
                });
    }

    private void toMainPage() {
        if (getWaitTime() <= 0) {
            go();
        } else {
            final Handler handler = new Handler();
            Runnable runnable = new Runnable() {
                public void run() {
                    go();
                }
            };
            handler.postDelayed(runnable, getWaitTime());
        }
    }

    private int getWaitTime() {
        long waitTime = WELCOME_TIME - ((new Date()).getTime() - mStartDate.getTime());
        return (int) waitTime;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void go() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivityForResult(intent, Constants.WELCOME_ACTIVITY);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        try {
            if (this.getPackageManager().getPackageInfo(getPackageName(), 0).versionCode >= Build.VERSION_CODES.LOLLIPOP)
                finishAfterTransition();
            else finish();
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }


}
