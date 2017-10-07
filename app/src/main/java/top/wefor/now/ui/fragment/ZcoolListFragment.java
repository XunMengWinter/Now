package top.wefor.now.ui.fragment;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.util.Log;
import android.view.View;

import com.github.florent37.materialviewpager.MaterialViewPagerHelper;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.Date;

import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter;
import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;
import jp.wasabeef.recyclerview.animators.FadeInAnimator;
import top.wefor.now.Constants;
import top.wefor.now.R;
import top.wefor.now.data.database.ZcoolDbHelper;
import top.wefor.now.data.http.Urls;
import top.wefor.now.data.model.entity.Zcool;
import top.wefor.now.ui.activity.WebActivity;
import top.wefor.now.ui.adapter.ZcoolAdapter;
import top.wefor.now.utils.PrefUtil;

/**
 * Created by ice on 15/10/28.
 */
public class ZcoolListFragment extends BaseListFragment<Zcool> {

    private ZcoolAdapter mAdapter;

    public static ZcoolListFragment newInstance() {
        return new ZcoolListFragment();
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

        mAdapter = new ZcoolAdapter(getActivity(), mList);

        // set LayoutManager for your RecyclerView
        if (getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
            mAdapter.setImageWidthAndHeight(2);
        } else {
            mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
            mAdapter.setImageWidthAndHeight(3);
        }

        mRecyclerView.setHasFixedSize(true);
        int dp8 = getResources().getDimensionPixelSize(R.dimen.d3);
        mRecyclerView.setPadding(dp8, 0, dp8, 0);

        mRecyclerView.setItemAnimator(new FadeInAnimator());
        AlphaInAnimationAdapter alphaAdapter = new AlphaInAnimationAdapter(mAdapter);
        ScaleInAnimationAdapter scaleAdapter = new ScaleInAnimationAdapter(alphaAdapter);

//        scaleAdapter.setFirstOnly(false);
//        scaleAdapter.setInterpolator(new OvershootInterpolator());
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
        io.reactivex.Observable
                .create((ObservableOnSubscribe<Document>) observableEmitter -> {
                    if (!PrefUtil.isNeedRefresh(Constants.KEY_REFRESH_TIME_ZCOOL)) {
                        observableEmitter.onComplete();
                        return;
                    }
                    try {
                        Document document = Jsoup.connect(Urls.ZCOOL_URL).get();
                        observableEmitter.onNext(document);
                    } catch (IOException e) {
                        e.printStackTrace();
                        observableEmitter.onComplete();
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
                .doOnComplete(this::showList)
                .subscribe(document -> {
                    ZcoolDbHelper zcoolDbHelper = new ZcoolDbHelper(mList, mRealm);
                    zcoolDbHelper.saveToDatabase();
                    showList();
                });
    }

    @Override
    public void showList() {
        ZcoolDbHelper zcoolDbHelper = new ZcoolDbHelper(mList, mRealm);
        zcoolDbHelper.getFromDatabase(PAGE_SIZE, mPage++);
        mAdapter.notifyDataSetChanged();
    }

}
