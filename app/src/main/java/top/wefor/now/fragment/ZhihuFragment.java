package top.wefor.now.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.github.florent37.materialviewpager.MaterialViewPagerHelper;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.BaseJsonHttpResponseHandler;

import org.apache.http.Header;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter;
import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;
import jp.wasabeef.recyclerview.animators.FadeInAnimator;
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

    AsyncHttpClient mClient = new AsyncHttpClient();
    AsyncHttpResponseHandler mResponseHandlerGetNews = new BaseJsonHttpResponseHandler<ZhihuDailyResult>() {

        @Override
        public void onSuccess(int statusCode, Header[] headers, String rawJsonResponse, ZhihuDailyResult response) {
            if (response.stories != null) {
                for (ZhihuDaily item : response.stories) {
                    mNewsList.add(item);
                }
                mAdapter.notifyDataSetChanged();
                stopLoadingAnim();
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, Throwable throwable, String rawJsonData, ZhihuDailyResult errorResponse) {

        }

        @Override
        protected ZhihuDailyResult parseResponse(String rawJsonData, boolean isFailure) throws Throwable {
            Gson gson = new Gson();
            return gson.fromJson(rawJsonData, ZhihuDailyResult.class);
        }
    };

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
        if (savedInstanceState == null) {
            Bundle bundle = getArguments();
            date = bundle.getString("date");

            setRetainInstance(true);
        }
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
        String url = ZhihuApi.getDailyNews(date);
        // Debug url
//        String url = "http://news.at.zhihu.com/api/4/news/before/20150822";
        mClient.get(getActivity(), url, mResponseHandlerGetNews);

        MaterialViewPagerHelper.registerRecyclerView(getActivity(), mRecyclerView, null);
    }
}
