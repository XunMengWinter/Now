package top.wefor.now;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.tencent.connect.share.QQShare;
import com.tencent.connect.share.QzoneShare;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import top.wefor.now.utils.Constants;
import top.wefor.now.utils.Share;

/*
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

    @Bind(R.id.progressbar)
    ProgressBar mProgressbar;
    @Bind(R.id.webView)
    WebView mWebView;
    @Bind(R.id.cardView)
    CardView mCardView;
    @Bind(R.id.loading_view)
    View mLoadingView;

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

    @Bind(R.id.fab)
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
                    bitmap = Glide.with(WebActivity.this).load(picUrl).asBitmap().into(100, 100).get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
        }.start();

        // Restore preferences
        SharedPreferences settings = getSharedPreferences(Constants.PREFS_NAME, 0);
        boolean isJSEnabled = settings.getBoolean(Constants.JAVA_SCRIPT_ENABLED, true);

        mWebView.getSettings().setJavaScriptEnabled(isJSEnabled);
        mWebView.setWebChromeClient(new ChromeClient());
        mWebView.setWebViewClient(new ViewClient());
        mWebView.getSettings().setLoadWithOverviewMode(true);
        if (!isWifiConnected(mContext))
            mWebView.getSettings().setLoadsImagesAutomatically(false);

        mWebView.getSettings().setAppCacheEnabled(true);
        mWebView.loadUrl(mUrl);
//        mWebView.setOnTouchListener(this);
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
        ButterKnife.unbind(this);
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
            mProgressbar.setProgress(newProgress);
            if (newProgress == 100) {
                mProgressbar.setVisibility(View.GONE);
                mLoadingView.setVisibility(View.GONE);
            } else if (newProgress != 100) {
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

        if (hr.getType() == 8) {
            new AlertDialog.Builder(mContext)
                    .setMessage("hello: " + hr.getExtra())
                    .create().show();
        }

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
        if (picUrl != null && !picUrl.equals("")) {
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
