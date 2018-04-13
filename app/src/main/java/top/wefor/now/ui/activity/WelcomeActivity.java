package top.wefor.now.ui.activity;

import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.bumptech.glide.Glide;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import top.wefor.now.App;
import top.wefor.now.BuildConfig;
import top.wefor.now.Constants;
import top.wefor.now.PreferencesHelper;
import top.wefor.now.R;
import top.wefor.now.data.http.BaseHttpObserver;
import top.wefor.now.data.http.NowApi;
import top.wefor.now.data.model.GankMeizhiResult;
import top.wefor.now.data.model.entity.Gank;
import top.wefor.now.ui.BaseCompatActivity;
import top.wefor.now.utils.NowAppUtil;

/**
 * Created on 15/11/22.
 * @author ice
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
        if (!TextUtils.isEmpty(coverImgUrl)) {
//            mSimpleDraweeView.setImageURI(Uri.parse(coverImgUrl));
            Glide.with(this).load(coverImgUrl).into(mSimpleDraweeView);
        } else {
            GenericDraweeHierarchy hierarchy = mSimpleDraweeView.getHierarchy();
            hierarchy.setPlaceholderImage(R.mipmap.img_first_welcome);
        }

        String version = String.format(getResources().getString(R.string.app_version), BuildConfig.VERSION_NAME);
        mTextView.setText(pass(version));

        int type = mPreferencesHelper.getHeadImageType();
        switch (type) {
            case Constants.TYPE_GANK_MEIZHI:
                if (NowAppUtil.isWifiConnected() || TextUtils.isEmpty(mPreferencesHelper.getHeadImages()))
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
                .subscribe(new BaseHttpObserver<GankMeizhiResult>(getLifecycle()) {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

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

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
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
