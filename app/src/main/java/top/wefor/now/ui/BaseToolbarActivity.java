package top.wefor.now.ui;

import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;

import com.google.android.material.appbar.AppBarLayout;

import top.wefor.now.R;

/**
 * Created by ice on 3/18/16 09:44.
 */
public abstract class BaseToolbarActivity extends BaseAppCompatActivity {

    protected Toolbar mToolbar;
    protected AppBarLayout mAppBarLayout;

    /**
     * Initialize the toolbar in the layout
     *
     * @param savedInstanceState savedInstanceState
     */
    @Override
    protected void initToolbar(Bundle savedInstanceState) {
        mToolbar = findViewById(R.id.toolbar);
        mAppBarLayout = findViewById(R.id.app_bar_layout);
        if (mToolbar == null || mAppBarLayout == null) return;

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            this.mAppBarLayout.setElevation(10.6f);
//        }

        setSupportActionBar(mToolbar);
    }

    protected void setToolbarVisibility(int viewVisibility) {
        if (mAppBarLayout != null)
            mAppBarLayout.setVisibility(viewVisibility);
        else if (mToolbar != null)
            mToolbar.setVisibility(viewVisibility);
    }

}
