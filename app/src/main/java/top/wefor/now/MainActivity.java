package top.wefor.now;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.github.florent37.materialviewpager.MaterialViewPager;
import com.github.florent37.materialviewpager.header.HeaderDesign;
import com.umeng.update.UmengUpdateAgent;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;

import top.wefor.now.fragment.BaseFragment;
import top.wefor.now.fragment.MomentFragment;
import top.wefor.now.fragment.NGFragment;
import top.wefor.now.fragment.OtherFragment;
import top.wefor.now.fragment.ZcoolFragment;
import top.wefor.now.fragment.ZhihuFragment;
import top.wefor.now.utils.Constants;

public class MainActivity extends BaseCompatActivity {

    private MaterialViewPager mViewPager;
    private DrawerLayout mDrawer;
    private ActionBarDrawerToggle mDrawerToggle;
    Toolbar toolbar;

    private ArrayList<BaseFragment> mFragmentArrayList;
    ArrayList<String> mTitles;
    ArrayList<Integer> mColors;
    private Integer mSize, mLuckyNum;
    private JSONArray mImgList;

    private SharedPreferences mPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UmengUpdateAgent.update(this);
        setContentView(R.layout.activity_main);

        mPreferences = getSharedPreferences(Constants.PREFS_NAME, 0);
        boolean isNotFirst = mPreferences.getBoolean(Constants.NOT_FIRST, false);
        String imgs = mPreferences.getString(Constants.HEAD_IMAGES, null);
        Log.i("xyz", "imgs " + imgs);
        if (imgs != null)
            mImgList = JSON.parseArray(imgs);
        if (mImgList != null && mImgList.size() > 0 && isWifiConnected(this))
            saveCoverImg();

//        if (!BuildConfig.DEBUG)
//            Fabric.with(this, new Crashlytics());

        setTitle("");

        mViewPager = (MaterialViewPager) findViewById(R.id.materialViewPager);

        toolbar = mViewPager.getToolbar();
        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawer.post(() -> {
            Log.i("xyz", " width " + mDrawer.getWidth());
            NowApplication.setWidth(mDrawer.getWidth());
            NowApplication.setHeight(mDrawer.getHeight());
        });

        if (toolbar != null) {
            setSupportActionBar(toolbar);

            final ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.setDisplayHomeAsUpEnabled(true);
                actionBar.setDisplayShowHomeEnabled(true);
                actionBar.setDisplayShowTitleEnabled(true);
                actionBar.setDisplayUseLogoEnabled(false);
                actionBar.setHomeButtonEnabled(true);
            }
        }

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawer, 0, 0);
//        mDrawer.setDrawerListener(mDrawerToggle);

//        checkIsFirst();
        showAll();
    }

    private void checkIsFirst() {
        boolean isNotFirst = mPreferences.getBoolean(Constants.NOT_FIRST, false);
        if (!isNotFirst) {
            new AlertDialog.Builder(this)
                    .setTitle("Welcome to Now")
                    .setMessage(getString(R.string.fist_time_notice))
                    .setPositiveButton(getString(R.string.enter), (dialog, which) -> {
                        showAll();
                        SharedPreferences.Editor editor = mPreferences.edit();
                        editor.putBoolean(Constants.NOT_FIRST, true);
                        editor.apply();

                        dialog.dismiss();
                    })
                    .setNegativeButton(getString(R.string.exit), (dialog, which) -> MainActivity.this.finish())
                    .create().show();
        } else
            showAll();
    }


    private void showAll() {
        mFragmentArrayList = new ArrayList<>();
        mTitles = new ArrayList<>();
        mColors = new ArrayList<>();

        if (mPreferences.getBoolean(getString(R.string.zcool), true)) {
            mFragmentArrayList.add(ZcoolFragment.newInstance());
            mTitles.add(getString(R.string.zcool));
            mColors.add(R.color.zcool);
        }
        if (mPreferences.getBoolean(getString(R.string.ng), true)) {
            mFragmentArrayList.add(NGFragment.newInstance());
            mTitles.add(getString(R.string.ng));
            mColors.add(R.color.ng);
        }
        if (mPreferences.getBoolean(getString(R.string.zhihu), true)) {
            mFragmentArrayList.add(ZhihuFragment.newInstance());
            mTitles.add(getString(R.string.zhihu));
            mColors.add(R.color.zhihu);
        }
        if (mPreferences.getBoolean(getString(R.string.moment), true)) {
            mFragmentArrayList.add(MomentFragment.newInstance());
            mTitles.add(getString(R.string.moment));
            mColors.add(R.color.moment);
        }

        mFragmentArrayList.add(OtherFragment.newInstance());
        mTitles.add(getString(R.string.fragment_other));
        mColors.add(R.color.purple);

        mSize = mTitles.size();
        mLuckyNum = new Random().nextInt(Calendar.getInstance().get(Calendar.DAY_OF_MONTH));

        mViewPager.getViewPager().setAdapter(new FragmentStatePagerAdapter(getSupportFragmentManager()) {

            @Override
            public Fragment getItem(int position) {
                return mFragmentArrayList.get(position % mSize);
            }

            @Override
            public int getCount() {
                return mSize;
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return mTitles.get(position % mSize);
            }
        });

        setViewPagerListener();

        mViewPager.getViewPager().setOffscreenPageLimit(mViewPager.getViewPager().getAdapter().getCount());
        mViewPager.getPagerTitleStrip().setViewPager(mViewPager.getViewPager());

        View logo = findViewById(R.id.logo_white);
        if (logo != null)
            logo.setOnClickListener(v -> {
                mViewPager.notifyHeaderChanged();
                Toast.makeText(getApplicationContext(), "Yes, the title is clickable", Toast.LENGTH_SHORT).show();
            });

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        return mDrawerToggle.onOptionsItemSelected(item) ||
//                super.onOptionsItemSelected(item);
//    }

    private void setViewPagerListener() {
        if (mImgList == null || mImgList.size() < mSize - 1)
            mViewPager.setMaterialViewPagerListener(page -> HeaderDesign.fromColorResAndDrawable(
                    mColors.get(page % mSize),
                    getResources().getDrawable(mColors.get(page % mSize))));
        else mViewPager.setMaterialViewPagerListener(page -> {
            if (mColors.get(page % mSize) == R.color.ng)
                return HeaderDesign.fromColorResAndDrawable(
                        R.color.ng,
                        getResources().getDrawable(R.color.ng));

            int size = Math.min(mImgList.size(), mSize);
            return HeaderDesign.fromColorResAndUrl(
                    mColors.get(page % mSize),
                    mImgList.getString((page + mLuckyNum) % size));
        });

    }

    private void saveCoverImg() {
//        String filePath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + Constants.COVER_IMAGE_PATH;
        Glide.with(MainActivity.this).load(mImgList.getString(mImgList.size() - 1)).downloadOnly(new SimpleTarget<File>() {
            @Override
            public void onResourceReady(File resource, GlideAnimation<? super File> glideAnimation) {
                SharedPreferences.Editor editor = mPreferences.edit();
                editor.putString(Constants.COVER_IMAGE, resource.getAbsolutePath());
                editor.apply();
            }
        });
    }

}
