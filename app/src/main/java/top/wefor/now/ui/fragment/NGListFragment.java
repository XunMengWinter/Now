package top.wefor.now.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.webkit.ValueCallback;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.alibaba.fastjson.JSONArray;
import com.orhanobut.logger.Logger;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.net.URLDecoder;
import java.util.Date;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import top.wefor.now.App;
import top.wefor.now.Constants;
import top.wefor.now.PreferencesHelper;
import top.wefor.now.R;
import top.wefor.now.data.http.BaseObserver;
import top.wefor.now.data.http.Urls;
import top.wefor.now.data.model.entity.NG;
import top.wefor.now.data.model.realm.RealmNG;
import top.wefor.now.ui.activity.WebActivity;
import top.wefor.now.ui.adapter.BaseListAdapter;
import top.wefor.now.ui.adapter.NGAdapter;
import top.wefor.now.utils.PrefUtil;

/**
 * @author ice
 * <p>
 * Created on 15/10/28.
 */
public class NGListFragment extends BaseListFragment<NG, RealmNG> {
    private static final int SIZE = 10;

    public static NGListFragment newInstance() {
        return new NGListFragment();
    }

    private WebView mWebView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRetainInstance(true);
    }

    @Override
    protected void initRecyclerView() {
        super.initRecyclerView();
        mAdapter.setOnItemClickListener(news -> {
            String title = news.title;
            if (TextUtils.isEmpty(title)) {
                title = news.content;
            }
            WebActivity.startThis(getActivity(), news.getUrl(), title, news.imgUrl
                    , getString(R.string.share_summary_mono));
        });

        mAdapter.setOnItemLongClickListener(model -> saveToNote(model.toNow()));

        if (mList.size() < 1) {
            try {
                getData();
            } catch (Exception e) {
                e.printStackTrace();
                Logger.e("getData error " + e.getMessage());
            }
        }
    }

    @Override
    public void getData() {
        if (!PrefUtil.isNeedRefresh(Constants.KEY_REFRESH_TIME_NG)) {
            showList();
            return;
        }
        /* 因为2018-07-29左右国家地理做了防盗处理(9月左右又正常了)，无法通过Jsoup抓取数据，所以使用隐藏网页加载后再抓取网页数据。 */
        mWebView = new WebView(getActivity());
        mWebView.setVisibility(View.INVISIBLE);
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                mWebView.evaluateJavascript(
                        "(function() { return (encodeURI('<html>'+document.getElementsByTagName('html')[0].innerHTML+'</html>')); })();",
                        new ValueCallback<String>() {
                            @Override
                            public void onReceiveValue(String html) {
                                Logger.i(html.length() + "");
                                parseHtmlText(replacer(new StringBuffer(html)));
                                releaseWebView();
                                Logger.i("ng web page finished");
//                                parseHtmlText(Unicode2charUtil.getFixStr(html));
//                                parseHtmlText(CommonUtils.getTextFromAssets(getContext(), "ng.html"));
                            }
                        });
            }

        });
        mWebView.loadUrl(Urls.NG_PHOTO_OF_THE_DAY_URL);
    }

    private void releaseWebView() {
        try {
            mWebView.destroy();
        } catch (Exception e) {
            e.printStackTrace();
            Logger.e("release webview error " + e.getMessage());
        }
    }

    public static String replacer(StringBuffer outBuffer) {
        String data = outBuffer.toString();
        try {
            data = data.replaceAll("%(?![0-9a-fA-F]{2})", "%25");
            data = data.replaceAll("\\+", "%2B");
            data = URLDecoder.decode(data, "utf-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }

    private void parseHtmlText(String text) {
        Observable
                .create((ObservableOnSubscribe<Document>) observableEmitter -> {
//                        Document document = Jsoup.connect(Urls.NG_PHOTO_OF_THE_DAY_URL).get();
                    Document document = Jsoup.parse(text);
                    observableEmitter.onNext(document);
                    observableEmitter.onComplete();
                })
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.computation())
                .doOnNext(document -> {
                    mList.clear();
                    PrefUtil.setRefreshTime(Constants.KEY_REFRESH_TIME_NG, new Date().getTime());
                    Element contents = document.getElementById("ajaxBox");
                    Elements list = contents.getElementsByClass("ajax_list");
                    for (int i = 0; i < SIZE && i < list.size(); i++) {
                        Element element = list.get(i);
                        NG nG = new NG();
                        Element imageA = element.select("dd").first().select("a").first();
                        nG.url = pass(imageA.attr("href"));
                        nG.imgUrl = pass(imageA.select("img").first().attr("src"));
                        nG.title = pass(imageA.select("img").first().attr("alt"));
                        nG.content = pass(element.getElementsByClass("ajax_dd_text").first().ownText());
                        mList.add(nG);
                    }
                    if (mList.size() > 0) {
                        PreferencesHelper preferencesHelper = new PreferencesHelper(App.getInstance());
                        //设置封面图,set Cover image
                        preferencesHelper.setCoverImage(mList.get(0).imgUrl);

                        // setHeadImages
                        JSONArray jsonArray = new JSONArray();
                        for (int i = 0; i < Math.min(mList.size(), 6); i++)
                            jsonArray.add(mList.get(i).imgUrl);
                        preferencesHelper.setNgImages(jsonArray.toJSONString());
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<Document>(getLifecycle()) {
                    @Override
                    protected void onSucceed(Document result) {
                        saveData();
                        showList();
                    }

                    @Override
                    protected void onFailed(@Nullable String msg) {
                        super.onFailed(msg);
                        showList();
                    }

                });
    }

    @Override
    public void onDestroyView() {
        releaseWebView();
        super.onDestroyView();
    }

    @NonNull
    @Override
    public Class<RealmNG> getNowRealmClass() {
        return RealmNG.class;
    }

    @Override
    public BaseListAdapter<NG> getNowAdapter(List<NG> list) {
        return new NGAdapter(getActivity(), list);
    }

}
