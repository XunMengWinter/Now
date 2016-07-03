package top.wefor.now.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.squareup.picasso.Picasso;
import com.tencent.connect.share.QQShare;
import com.tencent.connect.share.QzoneShare;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import java.io.IOException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import top.wefor.now.App;
import top.wefor.now.Constants;
import top.wefor.now.PreferencesHelper;
import top.wefor.now.R;
import top.wefor.now.utils.NowAppUtils;
import top.wefor.now.utils.Share;

/*
 * Created by ice on 15/10/26.
 *
 * Thanks to
 * Author: drakeet
 */

public class WebActivity extends BaseSwipeBackCompatActivity implements View.OnTouchListener {

    public static final String EXTRA_URL = "extra_url";
    public static final String EXTRA_TITLE = "extra_title";
    public static final String EXTRA_PIC_URL = "extra_pic_url";
    public static final String EXTRA_SUMMARY = "extra_summary";

    public static final Integer TYPE_QZONE = 0;
    public static final Integer TYPE_QQ = 1;

    public static void startThis(Context context, String url, String title, String picUrl, String summary) {
        Intent intent = new Intent(context, WebActivity.class);
        intent.putExtra(WebActivity.EXTRA_TITLE, title);
        intent.putExtra(WebActivity.EXTRA_URL, url);
        intent.putExtra(WebActivity.EXTRA_PIC_URL, picUrl);
        intent.putExtra(WebActivity.EXTRA_SUMMARY, summary);
        context.startActivity(intent);
    }

    @BindView(R.id.progressbar) ProgressBar mProgressbar;
    @BindView(R.id.webView) WebView mWebView;
    @BindView(R.id.cardView) CardView mCardView;
    @BindView(R.id.loading_view) View mLoadingView;

    @OnClick(R.id.wechat_textView)
    void shareWechat() {
        mCardView.setVisibility(View.GONE);
        isMenuShow = false;
        Share.shareToWechat(this, mTitle, summary, mUrl, bitmap);

    }

    @OnClick(R.id.wechatcircle_textView)
    void shareWechatcircle() {
        mCardView.setVisibility(View.GONE);
        isMenuShow = false;
        Share.shareToWechatcircle(this, mTitle, summary, mUrl, bitmap);
    }

    @OnClick(R.id.qq_textView)
    void shareQQ() {
        mCardView.setVisibility(View.GONE);
        isMenuShow = false;
        share(TYPE_QQ);
    }

    @OnClick(R.id.qzone_textView)
    void shareQzone() {
        mCardView.setVisibility(View.GONE);
        isMenuShow = false;
        share(TYPE_QZONE);
    }

    @OnClick(R.id.other_textView)
    void shareOther() {
        mCardView.setVisibility(View.GONE);
        isMenuShow = false;
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, mTitle + " \n" + mUrl);
        intent.putExtra(Intent.EXTRA_SUBJECT, "Come from Now, see more, get now.");
        intent = Intent.createChooser(intent, getString(R.string.share_to));
        startActivity(intent);
    }

    @OnClick(R.id.fab)
    void fabOnClick() {
        if (isMenuShow) {
            mCardView.setVisibility(View.GONE);
            isMenuShow = false;
        } else {
            mCardView.setVisibility(View.VISIBLE);
            isMenuShow = true;
        }
    }

    @BindView(R.id.fab)
    FloatingActionButton mFloatingActionButton;

    Context mContext;
    String mUrl, mTitle;

    private Tencent mTencent;
    private QQUiListener mQQUiListener;
    private String picUrl, summary;
    private Bitmap bitmap;

    private boolean isMenuShow;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        ButterKnife.bind(this);
        mContext = this;

        mUrl = getIntent().getStringExtra(EXTRA_URL);
        mTitle = getIntent().getStringExtra(EXTRA_TITLE);
        picUrl = getIntent().getStringExtra(EXTRA_PIC_URL);
        summary = getIntent().getStringExtra(EXTRA_SUMMARY);

        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    bitmap = Picasso.with(WebActivity.this).load(picUrl).resize(100, 100).get();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();

        mWebView.setWebChromeClient(new ChromeClient());
        mWebView.setWebViewClient(new ViewClient());

        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(new PreferencesHelper(App.getInstance()).isJSEnabled());
        webSettings.setLoadWithOverviewMode(true);

        webSettings.setAppCacheEnabled(true);
        webSettings.setAppCachePath(Constants.WEB_CACHE_DIR);

        if (NowAppUtils.isWifiConnected())
            webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
        else
            webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        mWebView.loadUrl(mUrl);

        mWebView.setOnTouchListener(this);
        setTitle(mTitle);
        mFloatingActionButton.setVisibility(View.GONE);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            switch (keyCode) {
                case KeyEvent.KEYCODE_BACK:
                    if (mWebView.canGoBack()) {
                        mWebView.goBack();
                    } else {
                        finish();
                    }
                    return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mWebView != null) mWebView.destroy();
    }

    @Override
    protected void onPause() {
        if (mWebView != null) mWebView.onPause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mWebView != null) mWebView.onResume();
    }

    private class ChromeClient extends WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
            Log.i("xyz", newProgress + " progress");
            mProgressbar.setProgress(newProgress);
            if (newProgress == 100) {
                mProgressbar.setVisibility(View.GONE);
                mLoadingView.setVisibility(View.GONE);
            } else {
                mProgressbar.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
            setTitle(title);
        }
    }

    private class ViewClient extends WebViewClient {
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (url != null) view.loadUrl(url);
            return true;
        }
    }

    private int direction = 0;   //0     1左右   2上下
    private int oldY, oldX, maxY, miniY, lastX, lastY;
    private boolean isShowFab;

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        WebView.HitTestResult hr = ((WebView) v).getHitTestResult();

        Log.i("xyz ", "getExtra = " + hr.getExtra() + "\t\t Type=" + hr.getType());

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                oldX = (int) event.getRawX();
                oldY = (int) event.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:

                int dx = (int) event.getRawX() - oldX;
                int dy = (int) event.getRawY() - oldY;

                direction = (dx * dx > dy * dy) ? 1 : 2;
                break;
            case MotionEvent.ACTION_UP:
                if (direction == 2) {
                    if ((event.getRawY() - oldY > 72) && !isShowFab) {
                        mFloatingActionButton.setVisibility(View.VISIBLE);
                        isShowFab = true;
                    } else if (isShowFab && oldY - event.getRawY() > 72) {
                        mFloatingActionButton.setVisibility(View.GONE);
                        isShowFab = false;
                        if (isMenuShow) {
                            mCardView.setVisibility(View.GONE);
                            isMenuShow = false;
                        }
                    }

                }
        }

        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Tencent.onActivityResultData(requestCode, resultCode, data, mQQUiListener);
        if (null != mTencent)
            mTencent.onActivityResult(requestCode, resultCode, data);

    }

    public void share(int type) {
        mTencent = Tencent.createInstance(Constants.QQ_APP_ID, this.getApplicationContext());
        mQQUiListener = new QQUiListener();
        Bundle bundle = new Bundle();
        //这条分享消息被好友点击后的跳转URL。
        if (mUrl != null)
            bundle.putString(QQShare.SHARE_TO_QQ_TARGET_URL, mUrl);
        // 分享的标题。注：PARAM_TITLE、PARAM_IMAGE_URL、PARAM_	SUMMARY不能全为空，最少必须有一个是有值的。
        if (mTitle != null)
            bundle.putString(QQShare.SHARE_TO_QQ_TITLE, mTitle);
        // 分享的图片URL
        if (!TextUtils.isEmpty(picUrl)) {
            if (type == TYPE_QQ)
                bundle.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, picUrl);
            else {
                ArrayList<String> imageUrls = new ArrayList<String>();
                imageUrls.add(picUrl);
                bundle.putStringArrayList(QzoneShare.SHARE_TO_QQ_IMAGE_URL, imageUrls);
            }
        }
        // 分享的消息摘要，最长50个字
        if (summary != null)
            bundle.putString(QQShare.SHARE_TO_QQ_SUMMARY, summary);
        // 手Q客户端顶部，替换“返回”按钮文字，如果为空，用返回代替
        bundle.putString(QQShare.SHARE_TO_QQ_APP_NAME, "Now");
        // 标识该消息的来源应用，值为应用名称+AppId。
        bundle.putString(QQShare.SHARE_TO_QQ_EXT_INT, "Now" + Constants.QQ_APP_ID);


        if (type == TYPE_QQ) mTencent.shareToQQ(this, bundle, mQQUiListener);
        else mTencent.shareToQzone(this, bundle, mQQUiListener);

    }

    private class QQUiListener implements IUiListener {
        @Override
        public void onComplete(Object o) {

        }

        @Override
        public void onError(UiError uiError) {

        }

        @Override
        public void onCancel() {

        }
    }
}
