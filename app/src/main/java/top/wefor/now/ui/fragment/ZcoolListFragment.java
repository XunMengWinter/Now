package top.wefor.now.ui.fragment;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.util.Log;

import com.github.florent37.materialviewpager.MaterialViewPagerHelper;
import com.orhanobut.logger.Logger;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter;
import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;
import jp.wasabeef.recyclerview.animators.FadeInAnimator;
import top.wefor.now.Constants;
import top.wefor.now.R;
import top.wefor.now.data.http.BaseObserver;
import top.wefor.now.data.http.Urls;
import top.wefor.now.data.model.entity.Zcool;
import top.wefor.now.data.model.realm.RealmZcool;
import top.wefor.now.ui.activity.WebActivity;
import top.wefor.now.ui.adapter.BaseListAdapter;
import top.wefor.now.ui.adapter.ZcoolAdapter;
import top.wefor.now.utils.PrefUtil;

/**
 * Created on 15/10/28.
 *
 * @author ice
 */
public class ZcoolListFragment extends BaseListFragment<Zcool, RealmZcool> {

    public static ZcoolListFragment newInstance() {
        return new ZcoolListFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRetainInstance(true);
    }

    @Override
    protected void initRecyclerView() {
//        super.initRecyclerView();

        mAdapter = getNowAdapter(mList);
        if (getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
            ((ZcoolAdapter) mAdapter).setImageWidthAndHeight(2);
            mAdapter.setHeadViewCount(2);
        } else {
            mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
            ((ZcoolAdapter) mAdapter).setImageWidthAndHeight(3);
            mAdapter.setHeadViewCount(3);
        }

        int dp8 = getResources().getDimensionPixelSize(R.dimen.d3);
        mRecyclerView.setPadding(dp8, 0, dp8, 0);

        mRecyclerView.setItemAnimator(new FadeInAnimator());
        AlphaInAnimationAdapter alphaAdapter = new AlphaInAnimationAdapter(mAdapter);
        ScaleInAnimationAdapter scaleAdapter = new ScaleInAnimationAdapter(alphaAdapter);
        mRecyclerView.setAdapter(scaleAdapter);
        MaterialViewPagerHelper.registerRecyclerView(getActivity(), mRecyclerView);

        mAdapter.setOnItemClickListener(model -> {
            WebActivity.startThis(getActivity(), model.url, model.title, model.imgUrl,
                    getString(R.string.share_summary_zcool));
        });

        mAdapter.setOnItemLongClickListener(model -> saveToNote(model.toNow()));

        if (mList.size() < 1) {
            getData();
        }
    }

    @Override
    public void getData() {
        if (!PrefUtil.isNeedRefresh(Constants.KEY_REFRESH_TIME_ZCOOL)) {
            showList();
            return;
        }

        Observable
                .create((ObservableOnSubscribe<Document>) observableEmitter -> {
                    try {
                        Document document = Jsoup.connect(Urls.ZCOOL_URL).get();
                        observableEmitter.onNext(document);
                        observableEmitter.onComplete();
                    } catch (IOException e) {
                        e.printStackTrace();
                        observableEmitter.onError(new Throwable("zcool get error"));
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.computation())
                .doOnNext(document -> {
                    Log.i("xyz", "doOnNext");
                    mList.clear();
                    PrefUtil.setRefreshTime(Constants.KEY_REFRESH_TIME_ZCOOL, new Date().getTime());
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
                        mList.add(zcool);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .doOnDispose(this::showList)
                .subscribe(new BaseObserver<Document>(getLifecycle()) {
                    @Override
                    protected void onSucceed(Document result) {
                        saveData();
                    }
                });
    }

    @NonNull
    @Override
    public Class<RealmZcool> getNowRealmClass() {
        return RealmZcool.class;
    }

    @Override
    public BaseListAdapter<Zcool> getNowAdapter(List<Zcool> list) {
        return new ZcoolAdapter(getActivity(), list);
    }

}
