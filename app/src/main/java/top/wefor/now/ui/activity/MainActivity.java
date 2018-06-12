package top.wefor.now.ui.activity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.ColorRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.github.florent37.materialviewpager.MaterialViewPager;
import com.github.florent37.materialviewpager.header.HeaderDesign;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import top.wefor.now.App;
import top.wefor.now.Constants;
import top.wefor.now.PreferencesHelper;
import top.wefor.now.R;
import top.wefor.now.ui.BaseCompatActivity;
import top.wefor.now.ui.fragment.BaseFragment;
import top.wefor.now.ui.fragment.MomentListFragment;
import top.wefor.now.ui.fragment.MonoListFragment;
import top.wefor.now.ui.fragment.NGListFragment;
import top.wefor.now.ui.fragment.ZcoolListFragment;
import top.wefor.now.ui.fragment.ZhihuListFragment;
import top.wefor.now.ui.gank.GankDailyActivity;
import top.wefor.now.utils.UIHelper;

public class MainActivity extends BaseCompatActivity {

    @BindView(R.id.materialViewPager) MaterialViewPager mMaterialViewPager;
    @BindView(R.id.drawer_layout) DrawerLayout mDrawerLayout;
    @BindView(R.id.js_checkBox) CheckBox mJsCB;
    @BindView(R.id.js_textView) TextView mJsTv;
    @BindView(R.id.headPicture_textView) TextView mHeadPictureTv;

    @BindView(R.id.other_rootView) LinearLayout mOtherRootView;
    @BindView(R.id.wiki_imageButton) ImageButton mWikiImageButton;
    @BindView(R.id.columnSelect_textView) TextView mColumnSelectTextView;
    @BindView(R.id.headPicture_linearLayout) LinearLayout mHeadPictureLinearLayout;
    @BindView(R.id.about_textView) TextView mAboutTextView;
    @BindView(R.id.thanks_textView) TextView mThanksTextView;
    @BindView(R.id.suggest_linearLayout) LinearLayout mSuggestLinearLayout;
    @BindView(R.id.gank_textView) TextView mGankTextView;


    PreferencesHelper mPreferencesHelper = new PreferencesHelper(App.getInstance());

    private View mColumnSelectView, mHeadPictureView;
    private ActionBarDrawerToggle mDrawerToggle;
    Toolbar toolbar;

    private Integer mSize, mLuckyNum;
    private JSONArray mImgList;
    public ArrayList<MyTabItem> mMyTabItems;
    //用于 finish 当前 Activity 的 Runnable.
    private Runnable mFinishRunnable = this::finishAffinity;
    private boolean isFinishNow = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        String imgs = mPreferencesHelper.getHeadImages();
        Log.i("xyz", "imgs " + imgs);
        if (!TextUtils.isEmpty(imgs))
            mImgList = JSON.parseArray(imgs);

        setTitle("");

        toolbar = mMaterialViewPager.getToolbar();

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

            toolbar.setLayoutParams(new RelativeLayout.LayoutParams(toolbar.getWidth(), UIHelper.getStatusBarHeight()));
        }

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, 0, 0);
        mDrawerLayout.addDrawerListener(mDrawerToggle);

        checkIsFirst();

        initDrawer();
    }

    private void checkIsFirst() {
        if (mPreferencesHelper.isFirst()) {
            new AlertDialog.Builder(this)
                    .setTitle("Welcome to Now")
                    .setMessage(getString(R.string.fist_time_notice))
                    .setPositiveButton(getString(R.string.enter), (dialog, which) -> {
                        mPreferencesHelper.setFirst(false);
                        showAll();
                    })
                    .setCancelable(false)
                    .setNegativeButton(getString(R.string.exit), (dialog, which) -> MainActivity.this.finish())
                    .create().show();
        } else {
            showAll();
        }
    }

    public class MyTabItem {
        public String title;
        public BaseFragment fragment;
        @ColorRes public int colorRes;

        public MyTabItem(String title, BaseFragment fragment, int colorRes) {
            this.title = title;
            this.fragment = fragment;
            this.colorRes = colorRes;
        }
    }

    private void showAll() {
        mMyTabItems = new ArrayList<>();
        if (mPreferencesHelper.isModuleSelected(getString(R.string.zcool))) {
            mMyTabItems.add(new MyTabItem(getString(R.string.zcool), ZcoolListFragment.newInstance(), R.color.zcool));
        }
        if (mPreferencesHelper.isModuleSelected(getString(R.string.ng))) {
            mMyTabItems.add(new MyTabItem(getString(R.string.ng), NGListFragment.newInstance(), R.color.ng));
        }
        if (mPreferencesHelper.isModuleSelected(getString(R.string.zhihu))) {
            mMyTabItems.add(new MyTabItem(getString(R.string.zhihu), ZhihuListFragment.newInstance(), R.color.zhihu));
        }
        if (mPreferencesHelper.isModuleSelected(getString(R.string.moment))) {
            mMyTabItems.add(new MyTabItem(getString(R.string.moment), MomentListFragment.newInstance(), R.color.moment));
        }

        mMyTabItems.add(new MyTabItem(getString(R.string.mono), MonoListFragment.newInstance(), R.color.mono));

        mSize = mMyTabItems.size();
        mLuckyNum = new Random().nextInt(Calendar.getInstance().get(Calendar.DAY_OF_MONTH));

        mMaterialViewPager.getViewPager().setAdapter(new FragmentStatePagerAdapter(getSupportFragmentManager()) {

            @Override
            public Fragment getItem(int position) {
                return mMyTabItems.get(position % mSize).fragment;
            }

            @Override
            public int getCount() {
                return mSize;
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return mMyTabItems.get(position % mSize).title;
            }
        });

        setViewPagerListener();

        mMaterialViewPager.getViewPager().setOffscreenPageLimit(mSize);
        mMaterialViewPager.getPagerTitleStrip().setViewPager(mMaterialViewPager.getViewPager());

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return mDrawerToggle.onOptionsItemSelected(item) ||
                super.onOptionsItemSelected(item);
    }

    private void setViewPagerListener() {
        if (mImgList == null || mImgList.size() < mSize - 1)
            mMaterialViewPager.setMaterialViewPagerListener(page -> HeaderDesign.fromColorResAndDrawable(
                    mMyTabItems.get(page % mSize).colorRes,
                    getResources().getDrawable(mMyTabItems.get(page % mSize).colorRes)));
        else mMaterialViewPager.setMaterialViewPagerListener(page -> {
            if (mMyTabItems.get(page % mSize).colorRes == R.color.ng)
                return HeaderDesign.fromColorResAndDrawable(
                        R.color.ng,
                        getResources().getDrawable(R.color.ng));

            int size = Math.min(mImgList.size(), mSize);
            return HeaderDesign.fromColorResAndUrl(
                    mMyTabItems.get(page % mSize).colorRes,
                    mImgList.getString((page + mLuckyNum) % size));
        });

    }

    /**
     * 按返回键时延迟执行 mFinishRunnable ;
     * 并将当前Activity隐藏在后台, 看起来就像退出了一样。
     */
    @Override
    public void onBackPressed() {
//        super.onBackPressed(); //注释掉 super 代码.
        if (mDrawerLayout.isDrawerOpen(mOtherRootView)) {
            mDrawerLayout.closeDrawer(mOtherRootView);
            return;
        }
        if (isFinishNow) {
            mFinishRunnable.run();
            return;
        }
        getWindow().getDecorView().postDelayed(mFinishRunnable, Constants.VALUE_FINISH_DELAYED_TIME);
        moveTaskToBack(true);
    }

    /**
     * 用户可能误触了返回键, 重回Activity后应该取消延迟执行的 mFinishRunnable .
     */
    @Override
    protected void onStart() {
        super.onStart();
        getWindow().getDecorView().removeCallbacks(mFinishRunnable);
    }

    private void initDrawer() {
        //视图默认为打开  default is checked in view
        if (mPreferencesHelper.isJSEnabled()) {
            mJsCB.setChecked(true);
            mJsTv.setText(R.string.js_close_description);
        }

        mHeadPictureTv.setText(getResources().getStringArray(
                R.array.head_picture_source)[mPreferencesHelper.getHeadImageType()]);

        mWikiImageButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, WebActivity.class);
            intent.putExtra(WebActivity.EXTRA_TITLE, getString(R.string.wiki_title));
            intent.putExtra(WebActivity.EXTRA_URL, getString(R.string.wiki_url));
            startActivity(intent);
        });

        mJsCB.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked)
                mJsTv.setText(R.string.js_close_description);
            else
                mJsTv.setText(R.string.js_open_description);
            mPreferencesHelper.setJSEnabled(mJsCB.isEnabled());
        });

        mColumnSelectTextView.setOnClickListener(v -> {
            if (mColumnSelectView == null) {
                mColumnSelectView = getLayoutInflater().inflate(R.layout.dialog_column_select, null);
                LinearLayout linearLayout = mColumnSelectView.findViewById(R.id.linearLayout);
                linearLayout.addView(getCheckBox(getString(R.string.zcool)));
                linearLayout.addView(getCheckBox(getString(R.string.ng)));
                linearLayout.addView(getCheckBox(getString(R.string.zhihu)));
                linearLayout.addView(getCheckBox(getString(R.string.moment)));
            } else if (mColumnSelectTextView.getParent() instanceof ViewGroup) {
                ViewGroup parent = (ViewGroup) mColumnSelectView.getParent();
                parent.removeView(mColumnSelectView);
            }

            new AlertDialog.Builder(this)
                    .setTitle(getString(R.string.column_select))
                    .setView(mColumnSelectView)
                    .create().show();
        });

        mHeadPictureLinearLayout.setOnClickListener(v -> {
            if (mHeadPictureView == null) {
                mHeadPictureView = getLayoutInflater().inflate(R.layout.dialog_head_picture, null);
                RadioGroup radioGroup = (RadioGroup) mHeadPictureView.findViewById(R.id.radioGroup);
                radioGroup.check(radioGroup.getChildAt(mPreferencesHelper.getHeadImageType()).getId());
                radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
                    RadioButton radioButton = (RadioButton) group.findViewById(checkedId);
                    mPreferencesHelper.setHeadImageType(group.indexOfChild(radioButton));
                    mHeadPictureTv.setText(radioButton.getText());
                    setFinishNow();

                    switch (radioGroup.indexOfChild(radioButton)) {
                        case Constants.TYPE_NG:
                            mPreferencesHelper.setHeadImages(mPreferencesHelper.getNgImages());
                            break;
                        case Constants.TYPE_GANK_MEIZHI:
                            mPreferencesHelper.setHeadImages("");
                            break;
                        case Constants.TYPE_MAC:
                            JSONArray jsonArray = new JSONArray();
                            jsonArray.add(getString(R.string.pic_url_1));
                            jsonArray.add(getString(R.string.pic_url_2));
                            jsonArray.add(getString(R.string.pic_url_3));
                            jsonArray.add(getString(R.string.pic_url_4));
                            mPreferencesHelper.setHeadImages(jsonArray.toJSONString());
                            break;
                        case Constants.TYPE_COLOR:
                            mPreferencesHelper.setHeadImages("");
                            break;
                    }
                });
            } else if (mHeadPictureView.getParent() instanceof ViewGroup) {
                ((ViewGroup) mHeadPictureView.getParent()).removeView(mHeadPictureView);
            }

            new AlertDialog.Builder(this)
                    .setTitle(getString(R.string.head_picture))
                    .setView(mHeadPictureView)
                    .create().show();
        });

        mAboutTextView.setOnClickListener(v -> {
            new AlertDialog.Builder(this)
                    .setTitle(getString(R.string.about))
                    .setView(R.layout.dialog_about)
                    .setPositiveButton("wefor.top", (dialogInterface, i) -> {
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse(getString(R.string.my_website)));
                        startActivity(intent);
                    })
                    .create().show();
        });

        mThanksTextView.setOnClickListener(v -> {
            new AlertDialog.Builder(this)
                    .setTitle(getString(R.string.thanks))
                    .setView(R.layout.dialog_thanks)
                    .setPositiveButton("GitHub", (dialogInterface, i) -> {
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse(getString(R.string.my_github)));
                        startActivity(intent);
                    })
                    .create().show();
        });

        mSuggestLinearLayout.setOnClickListener(v -> {
            Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("message/rfc822");
            i.putExtra(Intent.EXTRA_EMAIL, new String[]{Constants.MY_EMAIL_GOOGLE});
            i.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.email_subject));
            try {
                startActivity(Intent.createChooser(i, getString(R.string.send_email)));
            } catch (ActivityNotFoundException ex) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage(R.string.send_email_failed).create().show();
            }
        });

        mGankTextView.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, GankDailyActivity.class)));
    }

    private View getCheckBox(final String name) {
        View view = getLayoutInflater().inflate(R.layout.item_column_select, null);
        CheckBox checkBox = view.findViewById(R.id.checkbox);
        checkBox.setText(name);
        checkBox.setChecked(mPreferencesHelper.isModuleSelected(name));

        checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            mPreferencesHelper.setModuleSelected(name, isChecked);
            setFinishNow();
        });
        return view;
    }

    private void setFinishNow() {
        isFinishNow = true;
    }

}
