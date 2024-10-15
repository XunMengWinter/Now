package top.wefor.now.ui.gank;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.github.chrisbanes.photoview.PhotoView;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import top.wefor.now.PreferencesHelper;
import top.wefor.now.R;
import top.wefor.now.data.http.BaseHttpObserver;
import top.wefor.now.data.http.NowApi;
import top.wefor.now.data.model.GankDailyResult;
import top.wefor.now.data.model.entity.Gank;
import top.wefor.now.ui.BaseToolbarActivity;
import top.wefor.now.utils.DateUtil;

/**
 * Created on 16/7/7.
 *
 * @author ice
 */
public class GankDailyActivity extends BaseToolbarActivity {

    private PhotoView mBannerSdv;
    private CollapsingToolbarLayout mCollapsingToolbar;
    private TabLayout mGankTabLayout;
    private ViewPager mViewPager;

    private final Date mDate = new Date();
    private int mRequestTimes;
    private FragmentPagerAdapter mFragmentPagerAdapter;
    private PreferencesHelper mPreferencesHelper;

    private String[] GANK_TAB_TITLES;
    private List<String> mTitles = new ArrayList<>();
    private List<Fragment> mFragments = new ArrayList<>();

    @Override
    protected int getLayoutId() {
        return R.layout.activity_gank_daily;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        // Binding views manually
        mBannerSdv = findViewById(R.id.banner_sdv);
        mCollapsingToolbar = findViewById(R.id.collapsing_toolbar);
        mGankTabLayout = findViewById(R.id.gank_tabLayout);
        mViewPager = findViewById(R.id.viewPager);

        GANK_TAB_TITLES = getResources().getStringArray(R.array.ganks);

        mFragmentPagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return mFragments.get(position);
            }

            @Override
            public int getCount() {
                return mFragments.size();
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return mTitles.get(position);
            }
        };

        mGankTabLayout.setupWithViewPager(mViewPager);
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mGankTabLayout));
        mViewPager.setAdapter(mFragmentPagerAdapter);

        mPreferencesHelper = new PreferencesHelper(this);
        showLastGankImage();
        mViewPager.postDelayed(this::getTheLatestGanks, 1000);
    }

    private void getTheLatestGanks() {
        Observable<GankDailyResult> observable = NowApi.getGankApi().getGankDaily(DateUtil.toGankDate(mDate)).observeOn(Schedulers.io());
        for (int i = 0; i < 7; i++) {
            observable = observable
                    .zipWith(getHistoryGank(DateUtil.toGankDate(mDate, -i - 1)), this::zipGankResult);
        }
        observable
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseHttpObserver<GankDailyResult>(getLifecycle()) {
                    @Override
                    protected void onSucceed(GankDailyResult result) {
                        Set<String> tabTitles = result.results.keySet();
                        mTitles.clear();
                        mFragments.clear();
                        for (String tabTitle : tabTitles) {
                            addGankList(tabTitle, (ArrayList<Gank>) result.results.get(tabTitle));
                        }

                        ArrayList<Gank> meiZhiList = (ArrayList<Gank>) result.results.get(getString(R.string.gank_meizhi_list));
                        if (meiZhiList != null && meiZhiList.size() > 0) {
                            String bannerImageUrl = meiZhiList.get(0).url;
                            Glide.with(GankDailyActivity.this)
                                    .load(bannerImageUrl)
                                    .apply(new RequestOptions().centerCrop())
                                    .into(mBannerSdv);
                            mPreferencesHelper.setLastGankBanner(bannerImageUrl);
                        }

                        mFragmentPagerAdapter.notifyDataSetChanged();
                    }
                });
    }

    private GankDailyResult zipGankResult(@NonNull GankDailyResult gankDailyResult, GankDailyResult gankDailyResult2) {
        if (gankDailyResult2 == null || gankDailyResult2.results == null || gankDailyResult2.results.isEmpty()) {
            return gankDailyResult;
        }
        for (String tabTitle : GANK_TAB_TITLES) {
            ArrayList<Gank> ganks = (ArrayList<Gank>) gankDailyResult.results.get(tabTitle);
            ArrayList<Gank> ganks2 = (ArrayList<Gank>) gankDailyResult2.results.get(tabTitle);
            if (ganks == null)
                gankDailyResult.results.put(tabTitle, ganks2);
            else if (ganks2 != null)
                ((ArrayList<Gank>)gankDailyResult.results.get(tabTitle)).addAll(ganks2);
        }
        return gankDailyResult;
    }

    private Observable<GankDailyResult> getHistoryGank(String date) {
        return NowApi.getGankApi(true).getGankDaily(date);
    }

    private void addGankList(String title, ArrayList<Gank> gankList) {
        mTitles.add(title);
        if (gankList == null)
            mFragments.add(GankListFragment.get(new ArrayList<>()));
        else
            mFragments.add(GankListFragment.get(gankList));
    }

    private void showLastGankImage() {
        String imageUrl = mPreferencesHelper.getLastGankBanner();
        if (imageUrl == null || imageUrl.isEmpty()) {
            return;
        }
        Glide.with(GankDailyActivity.this)
                .load(imageUrl)
                .apply(new RequestOptions().centerCrop())
                .into(mBannerSdv);
    }
}