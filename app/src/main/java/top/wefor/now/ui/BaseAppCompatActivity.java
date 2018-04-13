package top.wefor.now.ui;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleRegistry;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.MenuItem;

import butterknife.ButterKnife;
import io.reactivex.disposables.Disposable;

/**
 * Created on 3/18/16 09:06.
 *
 * @author ice
 *         Github https://github.com/XunMengWinter
 */
public abstract class BaseAppCompatActivity extends AppCompatActivity {

    protected LifecycleRegistry mLifecycleRegistry = (LifecycleRegistry) getLifecycle();

    protected abstract int getLayoutId();

    protected abstract void initViews(Bundle savedInstanceState);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(this.getLayoutId());
        // 用ButterKnife绑定视图，
        // 配合 Android Studio 中的插件 Android ButterKnife Zelezny 自动命名所选layout中的所有带id的视图控件
        // 请注意删除自动生成的onCreate()方法以避免重复绑定.
        ButterKnife.bind(this);

        this.initToolbar(savedInstanceState);
        initActionBar();//在initToolbar()完成后设置ActionBar
        this.initViews(savedInstanceState);
        handleLifecycleEvent(Lifecycle.Event.ON_CREATE);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        try {
            if (item.getItemId() == android.R.id.home) {
                finish();
                return true;
            }
            return super.onOptionsItemSelected(item);
        } catch (Exception e) {
            e.printStackTrace();
            return true;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        handleLifecycleEvent(Lifecycle.Event.ON_START);
    }

    @Override
    protected void onResume() {
        super.onResume();
        handleLifecycleEvent(Lifecycle.Event.ON_RESUME);
    }

    @Override
    protected void onPause() {
        handleLifecycleEvent(Lifecycle.Event.ON_PAUSE);
        super.onPause();
    }

    @Override
    protected void onStop() {
        handleLifecycleEvent(Lifecycle.Event.ON_STOP);
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        handleLifecycleEvent(Lifecycle.Event.ON_DESTROY);
        super.onDestroy();
    }

    /**
     * 之所以不声明为 abstract 方法是因为主题自带ActionBar:
     * <style name="AppTheme" parent="Theme.AppCompat.Light.DarkActionBar">
     */
    protected void initToolbar(Bundle savedInstanceState) {

    }

    protected void initActionBar() {
        if (getSupportActionBar() != null) {
            // 默认左上角按钮可以点击
            getSupportActionBar().setHomeButtonEnabled(true);
            // 默认显示左上角返回按钮
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    /**
     * 隐藏ActionBar左上角的返回按钮
     */
    protected void hideBack() {
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
    }

    /**
     * show toast
     *
     * @param msg
     */
    protected void showToast(CharSequence msg) {
        if (!TextUtils.isEmpty(msg))
            Snackbar.make(getWindow().getDecorView(), msg, Snackbar.LENGTH_SHORT).show();
    }

    /**
     * startActivity
     *
     * @param clazz
     */
    protected void go(Class<?> clazz) {
        Intent intent = new Intent(this, clazz);
        startActivity(intent);
    }

    public void handleLifecycleEvent(@NonNull Lifecycle.Event event) {
        mLifecycleRegistry.handleLifecycleEvent(event);
    }

}
