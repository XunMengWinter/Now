package top.wefor.now.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.github.florent37.materialviewpager.MaterialViewPagerHelper;
import com.google.gson.Gson;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter;
import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;
import jp.wasabeef.recyclerview.animators.FadeInAnimator;
import okhttp3.Call;
import top.wefor.now.R;
import top.wefor.now.ZhihuApi;
import top.wefor.now.adapter.ZhihuAdapter;
import top.wefor.now.model.ZhihuDaily;
import top.wefor.now.model.ZhihuDailyResult;
import top.wefor.now.utils.Constants;

public class ZhihuFragment extends BaseFragment {
    private List<ZhihuDaily> mNewsList = new ArrayList<>();
    private ZhihuAdapter mAdapter;
    private String date;

    public static ZhihuFragment newInstance() {
        ZhihuFragment fragment = new ZhihuFragment();
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
        getZhihuList();
        setRetainInstance(true);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);

// set LayoutManager for your RecyclerView

        // use a linear layout manager

        mAdapter = new ZhihuAdapter(getActivity(), mNewsList);
        mRecyclerView.setItemAnimator(new FadeInAnimator());
        AlphaInAnimationAdapter alphaAdapter = new AlphaInAnimationAdapter(mAdapter);
        ScaleInAnimationAdapter scaleAdapter = new ScaleInAnimationAdapter(alphaAdapter);
        mRecyclerView.setAdapter(scaleAdapter);
        MaterialViewPagerHelper.registerRecyclerView(getActivity(), mRecyclerView, null);

    }

    private void getZhihuList() {
        String url = ZhihuApi.getDailyNews(date);
        // Debug url
//        String url = "http://news.at.zhihu.com/api/4/news/before/20150822";
        OkHttpUtils.get()
                .url(url)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        showLoadingAnim();
                    }

                    @Override
                    public void onResponse(String response) {
                        Gson gson = new Gson();
                        ZhihuDailyResult zhihuDailyResult = gson.fromJson(response, ZhihuDailyResult.class);
                        if (zhihuDailyResult.stories != null) {
                            for (ZhihuDaily item : zhihuDailyResult.stories) {
                                mNewsList.add(item);
                            }
                            mAdapter.notifyDataSetChanged();
                            stopLoadingAnim();
                        }
                    }
                });
    }

}
