package top.wefor.now.ui;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.Toolbar;

import butterknife.BindView;
import top.wefor.now.R;

/**
 * Created by ice on 3/18/16 09:44.
 */
public abstract class BaseToolbarActivity extends BaseAppCompatActivity {

    @BindView(R.id.toolbar) protected Toolbar mToolbar;
    @BindView(R.id.app_bar_layout) protected AppBarLayout mAppBarLayout;

    /**
     * Initialize the toolbar in the layout
     *
     * @param savedInstanceState savedInstanceState
     */
    @Override
    protected void initToolbar(Bundle savedInstanceState) {
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
