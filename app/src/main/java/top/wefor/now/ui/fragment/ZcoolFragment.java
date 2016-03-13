package top.wefor.now.ui.fragment;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.github.florent37.materialviewpager.MaterialViewPagerHelper;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter;
import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;
import jp.wasabeef.recyclerview.animators.FadeInAnimator;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import top.wefor.now.R;
import top.wefor.now.http.Urls;
import top.wefor.now.model.entity.Zcool;
import top.wefor.now.ui.adapter.ZcoolAdapter;

/**
 * Created by ice on 15/10/28.
 */
public class ZcoolFragment extends BaseFragment {

    private List<Zcool> mZcoolList = new ArrayList<>();
    private ZcoolAdapter mAdapter;

    public static ZcoolFragment newInstance() {
        ZcoolFragment fragment = new ZcoolFragment();
        // TODO you can use bundle to transfer data
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getZcoolList();

        setRetainInstance(true);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        stopLoadingAnim();

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        mAdapter = new ZcoolAdapter(getActivity(), mZcoolList);

        // set LayoutManager for your RecyclerView
        if (getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
            mAdapter.setImageWidthAndHeight(2);
        } else {
            mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
            mAdapter.setImageWidthAndHeight(3);
        }

        mRecyclerView.setHasFixedSize(true);

        mRecyclerView.setItemAnimator(new FadeInAnimator());
        AlphaInAnimationAdapter alphaAdapter = new AlphaInAnimationAdapter(mAdapter);
        ScaleInAnimationAdapter scaleAdapter = new ScaleInAnimationAdapter(alphaAdapter);
//        scaleAdapter.setFirstOnly(false);
//        scaleAdapter.setInterpolator(new OvershootInterpolator());
        mRecyclerView.setAdapter(scaleAdapter);

        MaterialViewPagerHelper.registerRecyclerView(getActivity(), mRecyclerView, null);
    }

    private void getZcoolList() {
        mZcoolList.clear();
        Observable
                .create(new Observable.OnSubscribe<Document>() {
                    @Override
                    public void call(Subscriber<? super Document> subscriber) {
                        try {
                            Document document = Jsoup.connect(Urls.ZCOOL_URL).get();
                            subscriber.onNext(document);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.computation())
                .doOnNext(document -> {
                    if (document == null)
                        return;

                    // Links
                    Element userWorks = document.body().getElementById("user-works");
                    for (Element element : userWorks.select("li")) {
//            Log.i("xyz ", "cl item " + element.toString());
                        Zcool zcool = new Zcool();
                        zcool.url = pass(element.attr("onclick"));
                        zcool.url = zcool.url.split("'")[1];
                        zcool.imgUrl = pass(element.select("img").first().attr("src"));
                        zcool.title = pass(element.select("h2").first().ownText());
                        zcool.name = pass(element.getElementsByClass("time").first().ownText());
                        zcool.readCount = pass(element.select("span").get(1).ownText());
                        zcool.likeCount = pass(element.select("span").get(3).ownText());
//            Log.i("xyz ", "zcool " + zcool.imgUrl + zcool.url + zcool.title + zcool.name + zcool.readCount + "  " + zcool.likeCount);
                        mZcoolList.add(zcool);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(document1 -> {
                    if (mZcoolList.size() > 0) {
                        mAdapter.notifyDataSetChanged();
                        stopLoadingAnim();
                    } else
                        showLoadingAnim();
                });
    }

}
