package top.wefor.now.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;

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
import top.wefor.now.Constants;
import top.wefor.now.R;
import top.wefor.now.data.http.BaseObserver;
import top.wefor.now.data.http.Urls;
import top.wefor.now.data.model.entity.Moment;
import top.wefor.now.data.model.realm.RealmMoment;
import top.wefor.now.ui.activity.WebActivity;
import top.wefor.now.ui.adapter.BaseListAdapter;
import top.wefor.now.ui.adapter.MomentAdapter;
import top.wefor.now.utils.PrefUtil;

/**
 * Created on 15/10/28.
 *
 * @author ice
 */
public class MomentListFragment extends BaseListFragment<Moment, RealmMoment> {

    public static MomentListFragment newInstance() {
        return new MomentListFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRetainInstance(true);
    }

    @Override
    protected void initRecyclerView() {
        super.initRecyclerView();
        mAdapter.setOnItemClickListener(news -> {
            String summary = getString(R.string.share_summary_moment);
            String imageUrl = null;
            if (news.imgUrls != null) {
                JSONArray jsonArray = JSON.parseArray(news.imgUrls);
                if (jsonArray.size() > 0)
                    imageUrl = jsonArray.getString(0);
            } else if (news.content != null)
                summary = news.content;
            WebActivity.startThis(getActivity(), news.url, news.title, imageUrl, summary);
        });

        mAdapter.setOnItemLongClickListener(model -> saveToNote(model.toNow()));

        if (mList.size() < 1) {
            getData();
        }
    }

    @Override
    public void getData() {
        if (!PrefUtil.isNeedRefresh(Constants.KEY_REFRESH_TIME_MOMENT)) {
            showList();
            return;
        }

        Observable
                .create((ObservableOnSubscribe<Document>) observableEmitter -> {
                    try {
                        Document document = Jsoup.connect(Urls.MOMENT_URL).get();
                        observableEmitter.onNext(document);
                        observableEmitter.onComplete();
                    } catch (IOException e) {
                        e.printStackTrace();
                        observableEmitter.onError(new Throwable("moment get error"));
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.computation())
                .doOnNext(document -> {
                    mList.clear();
                    PrefUtil.setRefreshTime(Constants.KEY_REFRESH_TIME_MOMENT, new Date().getTime());
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
    public Class<RealmMoment> getNowRealmClass() {
        return RealmMoment.class;
    }

    @Override
    public BaseListAdapter<Moment> getNowAdapter(List<Moment> list) {
        return new MomentAdapter(getActivity(), list);
    }

}
