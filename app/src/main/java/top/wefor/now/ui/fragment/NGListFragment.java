package top.wefor.now.ui.fragment;

import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.fastjson.JSONArray;
import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.view.SimpleDraweeView;
import com.github.florent37.materialviewpager.MaterialViewPagerHelper;
import com.orhanobut.logger.Logger;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import top.wefor.now.database.NGDbHelper;
import top.wefor.now.http.Urls;
import top.wefor.now.model.entity.NG;
import top.wefor.now.ui.adapter.NGAdapter;
import top.wefor.now.ui.interactor.OnImageClickListener;
import top.wefor.now.utils.Constants;

/**
 * Created by ice on 15/10/28.
 */
public class NGListFragment extends BaseListFragment<NG> implements OnImageClickListener {
    private static final int SIZE = 10;

    private NGAdapter mAdapter;
    protected SimpleDraweeView mBigSdv;

    public static NGListFragment newInstance() {
        NGListFragment fragment = new NGListFragment();
        // TODO you can use bundle to transfer data
        return fragment;
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
        mAdapter.mOnImageClickListener = this;
        mRecyclerView.setAdapter(mAdapter);

        MaterialViewPagerHelper.registerRecyclerView(getActivity(), mRecyclerView, null);

        if (mList.size() < 1) {
            getData();
        }

    }

    @Override
    public void getData() {
//        String nGUrl = "http://photography.nationalgeographic.com/photography/";
        Observable
                .create(new Observable.OnSubscribe<Document>() {
                    @Override
                    public void call(Subscriber<? super Document> subscriber) {
                        try {
                            Document document = Jsoup.connect(Urls.NG_BASE_URL).get();
                            subscriber.onNext(document);
                        } catch (IOException e) {
                            e.printStackTrace();
                            subscriber.onNext(null);
                        }
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.computation())
                .doOnNext(document -> {
                    mList.clear();
                    if (document == null) {
                        return;
                    }
                    Logger.i(document.ownText());
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
                        SharedPreferences preferences = getActivity().getSharedPreferences(Constants.PREFS_NAME, 0);
                        //设置封面图,set Cover image
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString(Constants.COVER_IMAGE, mList.get(0).imgUrl);
                        editor.apply();

                        int type = preferences.getInt(Constants.COVER_SOURCE, 0);
                        // setHeadImages
                        if (type == Constants.TYPE_NG) {
                            SharedPreferences.Editor editor2 = preferences.edit();
                            JSONArray jsonArray = new JSONArray();
                            for (NG ng : mList)
                                jsonArray.add(ng.imgUrl);
                            editor2.putString(Constants.HEAD_IMAGES, jsonArray.toJSONString());
                            editor2.apply();
                        }
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(document1 -> {
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

    @Override
    public void onImageClick(String imageUrl) {
        if (mBigSdv == null) {
            mBigSdv = new SimpleDraweeView(getActivity());
            GenericDraweeHierarchy hierarchy = mBigSdv.getHierarchy();
            hierarchy.setActualImageScaleType(ScalingUtils.ScaleType.FIT_CENTER);
            mBigSdv.setBackgroundColor(0xCC000000);
            mBigSdv.setOnClickListener(v -> mBigSdv.setVisibility(View.GONE));
            if (getActivity().getWindow().getDecorView().getRootView() != null)
                ((ViewGroup) getActivity().getWindow().getDecorView().getRootView()).addView(mBigSdv);
        }

        mBigSdv.setVisibility(View.VISIBLE);
        mBigSdv.setImageURI(Uri.parse(imageUrl));

    }

}
