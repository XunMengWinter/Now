package top.wefor.now.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.alibaba.fastjson.JSONArray;
import com.github.florent37.materialviewpager.MaterialViewPagerHelper;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.Date;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import top.wefor.now.App;
import top.wefor.now.Constants;
import top.wefor.now.PreferencesHelper;
import top.wefor.now.R;
import top.wefor.now.data.database.NGDbHelper;
import top.wefor.now.data.http.Urls;
import top.wefor.now.data.model.entity.NG;
import top.wefor.now.ui.activity.WebActivity;
import top.wefor.now.ui.adapter.NGAdapter;
import top.wefor.now.utils.PrefUtil;

/**
 * Created by ice on 15/10/28.
 */
public class NGListFragment extends BaseListFragment<NG> {
    private static final int SIZE = 10;

    private NGAdapter mAdapter;

    public static NGListFragment newInstance() {
        return new NGListFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRetainInstance(true);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());

        // set LayoutManager for your RecyclerView
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);

        mAdapter = new NGAdapter(getActivity(), mList);
        mRecyclerView.setAdapter(mAdapter);

        MaterialViewPagerHelper.registerRecyclerView(getActivity(), mRecyclerView);

        mAdapter.setOnItemClickListener(news -> {
            Intent intent = new Intent(getActivity(), WebActivity.class);
            intent.putExtra(WebActivity.EXTRA_TITLE, news.title);
            intent.putExtra(WebActivity.EXTRA_URL, Urls.NG_BASE_URL + news.url);
            intent.putExtra(WebActivity.EXTRA_PIC_URL, news.imgUrl);
            intent.putExtra(WebActivity.EXTRA_SUMMARY, getString(R.string.share_summary_ng));
            startActivity(intent);
        });

        mAdapter.setOnItemLongClickListener(model -> saveToNote(model.toNow()));

        if (mList.size() < 1) {
            getData();
        }

    }

    @Override
    public void getData() {
//        String nGUrl = "http://photography.nationalgeographic.com/photography/";
        Observable
                .create((ObservableOnSubscribe<Document>) observableEmitter -> {
                    if (!PrefUtil.isNeedRefresh(Constants.KEY_REFRESH_TIME_NG)) {
                        observableEmitter.onComplete();
                        return;
                    }
                    try {
                        Document document = Jsoup.connect(Urls.NG_BASE_URL).get();
                        observableEmitter.onNext(document);
                    } catch (IOException e) {
                        e.printStackTrace();
                        observableEmitter.onComplete();
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.computation())
                .doOnNext(document -> {
                    mList.clear();
//                    Logger.i(document.ownText());
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

                        Log.i("xyz ", "ngfragment " + nG.content + nG.imgUrl + nG.url + nG.title);
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
                .doOnComplete(this::showList)
                .subscribe(document -> {
                    NGDbHelper zcoolDbHelper = new NGDbHelper(mList, mRealm);
                    zcoolDbHelper.saveToDatabase();
                    showList();
                });
    }

    @Override
    public void showList() {
        NGDbHelper momentDbHelper = new NGDbHelper(mList, mRealm);
        momentDbHelper.getFromDatabase(PAGE_SIZE, mPage++);
        mAdapter.notifyDataSetChanged();
    }

}
