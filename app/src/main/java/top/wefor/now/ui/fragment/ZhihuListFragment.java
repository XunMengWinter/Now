package top.wefor.now.ui.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.github.florent37.materialviewpager.MaterialViewPagerHelper;

import java.util.Calendar;
import java.util.Date;

import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter;
import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;
import jp.wasabeef.recyclerview.animators.FadeInAnimator;
import top.wefor.now.Constants;
import top.wefor.now.R;
import top.wefor.now.data.database.ZhihuDbHelper;
import top.wefor.now.data.http.BaseObserver;
import top.wefor.now.data.http.NowApi;
import top.wefor.now.data.model.ZhihuDailyResult;
import top.wefor.now.data.model.entity.Zhihu;
import top.wefor.now.ui.activity.WebActivity;
import top.wefor.now.ui.adapter.BaseListAdapter;
import top.wefor.now.ui.adapter.ZhihuAdapter;
import top.wefor.now.utils.PrefUtil;

public class ZhihuListFragment extends BaseListFragment<Zhihu> {

    private NowApi mNowApi = new NowApi();
    private ZhihuAdapter mAdapter;
    private String date;

    public static ZhihuListFragment newInstance() {
        ZhihuListFragment fragment = new ZhihuListFragment();
        // TODO you can use bundle to transfer data

        int i = 0;
        Calendar dateToGetUrl = Calendar.getInstance();
        dateToGetUrl.add(Calendar.DAY_OF_YEAR, 1 - i);
        String date = Constants.simpleDateFormat.format(dateToGetUrl.getTime());
        Bundle bundle = new Bundle();
        bundle.putBoolean("first_page?", i == 0);
        bundle.putBoolean("single?", false);
        bundle.putString("date", date);
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        date = bundle.getString("date");
        setRetainInstance(true);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        // set LayoutManager for your RecyclerView
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);

        mAdapter = new ZhihuAdapter(getActivity(), mList);
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
                .subscribe(new BaseObserver<ZhihuDailyResult>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    protected void onSucceed(ZhihuDailyResult result) {
                        if (result.stories != null) {
                            PrefUtil.setRefreshTime(Constants.KEY_REFRESH_TIME_ZHIHU, new Date().getTime());
                            mList.clear();
                            for (Zhihu item : result.stories) {
                                mList.add(item);
                            }
                            ZhihuDbHelper zcoolDbHelper = new ZhihuDbHelper(mList, mRealm);
                            zcoolDbHelper.saveToDatabase();
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

    @Override
    public void onDestroy() {
        super.onDestroy();
//        PropertyChangeListener propertyChangeListener = new PropertyChangeListener() {
//            @Override
//            public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
//                //
//            }
//        };
    }


    @Override
    public void showList() {
        //如果页数*分页数目大于列表长度，那么只能是列表正在加载或数据库数据见底，两种情况都不需要继续加载。
        if ((mPage - 1) * Constants.LIST_PAGE_SIZE > mList.size()) return;
        ZhihuDbHelper zcoolDbHelper = new ZhihuDbHelper(mList, mRealm);
        zcoolDbHelper.getFromDatabase(PAGE_SIZE, mPage++);
        mAdapter.notifyDataSetChanged();
    }

}
