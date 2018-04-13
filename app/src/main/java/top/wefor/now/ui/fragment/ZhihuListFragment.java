package top.wefor.now.ui.fragment;

import android.os.Bundle;

import com.github.florent37.materialviewpager.MaterialViewPagerHelper;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import io.reactivex.schedulers.Schedulers;
import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter;
import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;
import jp.wasabeef.recyclerview.animators.FadeInAnimator;
import top.wefor.now.Constants;
import top.wefor.now.R;
import top.wefor.now.data.http.BaseHttpObserver;
import top.wefor.now.data.http.NowApi;
import top.wefor.now.data.model.ZhihuDailyResult;
import top.wefor.now.data.model.entity.Zhihu;
import top.wefor.now.data.model.realm.RealmZhihu;
import top.wefor.now.ui.activity.WebActivity;
import top.wefor.now.ui.adapter.BaseListAdapter;
import top.wefor.now.ui.adapter.ZhihuAdapter;
import top.wefor.now.utils.PrefUtil;

public class ZhihuListFragment extends BaseListFragment<Zhihu, RealmZhihu> {

    private NowApi mNowApi = new NowApi();
    private String date;

    public static ZhihuListFragment newInstance() {
        ZhihuListFragment fragment = new ZhihuListFragment();
        Calendar dateToGetUrl = Calendar.getInstance();
        dateToGetUrl.add(Calendar.DAY_OF_YEAR, 1);
        String date = Constants.simpleDateFormat.format(dateToGetUrl.getTime());
        Bundle bundle = new Bundle();
        bundle.putBoolean("first_page?", true);
        bundle.putBoolean("single?", false);
        bundle.putString("date", date);
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        assert bundle != null;
        date = bundle.getString("date");
        setRetainInstance(true);
    }

    @Override
    protected void initRecyclerView() {
        super.initRecyclerView();

        mRecyclerView.setItemAnimator(new FadeInAnimator());
        AlphaInAnimationAdapter alphaAdapter = new AlphaInAnimationAdapter(mAdapter);
        ScaleInAnimationAdapter scaleAdapter = new ScaleInAnimationAdapter(alphaAdapter);
        mRecyclerView.setAdapter(scaleAdapter);
        MaterialViewPagerHelper.registerRecyclerView(getActivity(), mRecyclerView);

        mAdapter.setOnItemClickListener(news -> {
            String news_url = NowApi.getNewsContent(news.id);
            String imageUrl = null;
            if (news.images != null && news.images.size() > 0)
                imageUrl = news.images.get(0);

            WebActivity.startThis(getActivity(), news_url, news.title, imageUrl,
                    getString(R.string.share_summary_zhihu));
        });

        mAdapter.setOnItemLongClickListener(model -> saveToNote(model.toNow()));

        if (mList.size() < 1) {
            getData();
        }
    }

    @Override
    public void getData() {
        // Debug url
//        String url = "http://news.at.zhihu.com/api/4/news/before/20150822";
        if (!PrefUtil.isNeedRefresh(Constants.KEY_REFRESH_TIME_ZHIHU)) {
            showList();
            return;
        }

        mNowApi.getZhihuDaily(date)
                .subscribeOn(Schedulers.computation())
                .subscribe(new BaseHttpObserver<ZhihuDailyResult>(getLifecycle()) {
                    @Override
                    protected void onSucceed(ZhihuDailyResult result) {
                        if (result.stories != null) {
                            PrefUtil.setRefreshTime(Constants.KEY_REFRESH_TIME_ZHIHU, new Date().getTime());
                            mList.clear();
                            mList.addAll(result.stories);
                            saveData();
                        }
                        showList();
                    }

                    @Override
                    protected void onFailed(String msg) {
                        super.onFailed(msg);
                        showList();
                    }
                });
    }

    @android.support.annotation.NonNull
    @Override
    public Class<RealmZhihu> getNowRealmClass() {
        return RealmZhihu.class;
    }

    @Override
    public BaseListAdapter<Zhihu> getNowAdapter(List<Zhihu> list) {
        return new ZhihuAdapter(getActivity(), list);
    }

}
