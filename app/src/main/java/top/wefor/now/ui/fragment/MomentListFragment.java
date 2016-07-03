package top.wefor.now.ui.fragment;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.alibaba.fastjson.JSONArray;
import com.github.florent37.materialviewpager.MaterialViewPagerHelper;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.Date;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import top.wefor.now.data.database.MomentDbHelper;
import top.wefor.now.data.http.Urls;
import top.wefor.now.data.model.entity.Moment;
import top.wefor.now.ui.adapter.MomentAdapter;
import top.wefor.now.Constants;
import top.wefor.now.utils.PrefUtil;

/**
 * Created by ice on 15/10/28.
 */
public class MomentListFragment extends BaseListFragment<Moment> {

    private MomentAdapter mAdapter;

    public static MomentListFragment newInstance() {
        return new MomentListFragment();
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

        mAdapter = new MomentAdapter(getActivity(), mList);
        if (getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
            mAdapter.setImageWidthAndHeight();
        else
            mAdapter.setImageWidthAndHeight();

        mRecyclerView.setAdapter(mAdapter);

        MaterialViewPagerHelper.registerRecyclerView(getActivity(), mRecyclerView);

        if (mList.size() < 1) {
            getData();
        }
    }


    @Override
    public void getData() {
        Observable
                .create(new Observable.OnSubscribe<Document>() {
                    @Override
                    public void call(Subscriber<? super Document> subscriber) {
                        if (!PrefUtil.isNeedRefresh(Constants.KEY_REFRESH_TIME_MOMENT)) {
                            subscriber.onNext(null);
                        }

                        try {
                            Document document = Jsoup.connect(Urls.MOMENT_URL).get();
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
                    if (document == null) return;
                    PrefUtil.setRefreshTime(Constants.KEY_REFRESH_TIME_MOMENT,new Date().getTime());
                    // Links
                    Element userWorks = document.body().getElementById("selection");
                    if (userWorks == null) return;
                    for (Element element : userWorks.select("li")) {
//            Log.i("xyz ", "li " + element.toString());
                        Moment moment = new Moment();
                        moment.url = pass(element.select("a").first().attr("href"));
                        moment.title = pass(element.select("h3").first().ownText());

                        if (element.getElementsByClass("abstract").first() != null) {
                            moment.content = pass(element.getElementsByClass("abstract").first().ownText());
                        }
                        if (element.getElementsByClass("pics").first() != null) {
                            JSONArray jsonArray = new JSONArray();
                            for (Element element1 : element.getElementsByClass("pics").first().children())
                                jsonArray.add(element1.attr("src"));
                            moment.imgUrls = jsonArray.toJSONString();
                        }
                        mList.add(moment);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(document1 -> {
                    MomentDbHelper zcoolDbHelper = new MomentDbHelper(mList, mRealm);
                    zcoolDbHelper.saveToDatabase();
                    showList();
                });
    }

    @Override
    public void showList() {
        MomentDbHelper zcoolDbHelper = new MomentDbHelper(mList, mRealm);
        zcoolDbHelper.getFromDatabase(PAGE_SIZE, mPage++);
        mAdapter.notifyDataSetChanged();
    }

}
