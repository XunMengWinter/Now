package top.wefor.now.ui.activity;

import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import top.wefor.now.App;
import top.wefor.now.Constants;
import top.wefor.now.PreferencesHelper;
import top.wefor.now.R;
import top.wefor.now.data.http.BaseObserver;
import top.wefor.now.data.http.NowApi;
import top.wefor.now.data.model.GankMeizhiResult;
import top.wefor.now.data.model.entity.Gank;
import top.wefor.now.ui.BaseCompatActivity;
import top.wefor.now.utils.NowAppUtils;

/**
 * Created by ice on 15/11/22.
 */
public class WelcomeActivity extends BaseCompatActivity {

    @BindView(R.id.simpleDraweeView) SimpleDraweeView mSimpleDraweeView;
    @BindView(R.id.textView) TextView mTextView;

    private Date mStartDate;
    final long WELCOME_TIME = 1500;
    PreferencesHelper mPreferencesHelper = new PreferencesHelper(App.getInstance());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_welcome);
        ButterKnife.bind(this);

        mStartDate = new Date();
        String coverImgUrl = mPreferencesHelper.getCoverImage();
        if (!TextUtils.isEmpty(coverImgUrl))
            mSimpleDraweeView.setImageURI(Uri.parse(coverImgUrl));
        else {
            GenericDraweeHierarchy hierarchy = mSimpleDraweeView.getHierarchy();
            hierarchy.setPlaceholderImage(R.mipmap.img_first_welcome);
        }

        try {
            PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_ACTIVITIES);
            String version = String.format(getResources().getString(R.string.app_version), packageInfo.versionName);
            mTextView.setText(pass(version));
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        int type = mPreferencesHelper.getHeadImageType();
        switch (type) {
            case Constants.TYPE_GANK_MEIZHI:
                if (NowAppUtils.isWifiConnected() || TextUtils.isEmpty(mPreferencesHelper.getHeadImages()))
                    getCoverImgsThenToMainPage();
                else
                    toMainPage();
                break;
            default:
                toMainPage();
                break;
        }

    }

    private void getCoverImgsThenToMainPage() {
        new NowApi().getGankMeizhi()
                .subscribe(new BaseObserver<GankMeizhiResult>() {
                    @Override
                    protected void onSucceed(GankMeizhiResult result) {
                        JSONArray jsonArray = new JSONArray();
                        for (Gank item : result.results) {
                            if (item.url != null)
                                jsonArray.add(item.url);
                        }
                        mPreferencesHelper.setHeadImages(jsonArray.toJSONString());
                        toMainPage();
                    }
                });
    }

    private void toMainPage() {
        if (getWaitTime() <= 0)
            go();
        else
            mTextView.postDelayed(this::go, getWaitTime());
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
