package top.wefor.now.ui.activity;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.bumptech.glide.Glide;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.Date;

import butterknife.BindArray;
import butterknife.BindView;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import top.wefor.now.R;
import top.wefor.now.data.http.BaseObserver;
import top.wefor.now.data.http.NowApi;
import top.wefor.now.data.model.GankDailyResult;
import top.wefor.now.data.model.entity.Gank;
import top.wefor.now.ui.BaseToolbarActivity;
import top.wefor.now.ui.fragment.GankFragment;
import top.wefor.now.utils.DateUtil;

/**
 * Created on 16/7/7.
 *
 * @author ice
 */
public class GankDailyActivity extends BaseToolbarActivity {
    @BindView(R.id.banner_sdv) SimpleDraweeView mBannerSdv;
    @BindView(R.id.collapsing_toolbar) CollapsingToolbarLayout mCollapsingToolbar;
    @BindView(R.id.gank_tabLayout) TabLayout mGankTabLayout;
    @BindView(R.id.viewPager) ViewPager mViewPager;

    private Date mDate = new Date();
    private int mRequestTimes;
    NowApi mNowApi = new NowApi();
    FragmentPagerAdapter mFragmentPagerAdapter;

    @BindArray(R.array.ganks) String[] mTitles;
    private ArrayList<Fragment> mFragments = new ArrayList<>();

    @Override
    protected int getLayoutId() {
        return R.layout.activity_gank_daily;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
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
                return mTitles[position];
            }
        };

        mGankTabLayout.setupWithViewPager(mViewPager);
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mGankTabLayout));
        mViewPager.setAdapter(mFragmentPagerAdapter);

        getTheLastGank();
    }

    private void getTheLastGank() {
        mNowApi.getGankDaily(DateUtil.toDate(mDate))
                .subscribe(new BaseObserver<GankDailyResult>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    protected void onSucceed(GankDailyResult result) {
                        if (result.error || result.results == null || result.results.妹纸List == null) {
                            if (mRequestTimes >= 10) {
                                return;
                            }
                            mDate = DateUtil.getLastdayDate(mDate);
                            getTheLastGank();
                            mRequestTimes++;
                            return;
                        }

                        addGankList(result.results.androidList);
                        addGankList(result.results.iOSList);
                        addGankList(result.results.appList);
                        addGankList(result.results.瞎推荐List);
                        addGankList(result.results.休息视频List);
                        addGankList(result.results.拓展资源List);
                        addGankList(result.results.妹纸List);

                        if (result.results.妹纸List != null && result.results.妹纸List.size() > 0) {
                            Glide.with(GankDailyActivity.this)
                                    .load(result.results.妹纸List.get(0).url)
                                    .into(mBannerSdv);
//                            RxView.clicks(mBannerSdv).subscribe(aVoid -> go(null));
                        }

                        mFragmentPagerAdapter.notifyDataSetChanged();

                    }
                });
    }

    private void addGankList(ArrayList<Gank> gankList) {
        if (gankList == null)
            mFragments.add(GankFragment.get(new ArrayList<>()));
        else
            mFragments.add(GankFragment.get(gankList));
    }
}
