package top.wefor.now.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import java.util.ArrayList;
import java.util.List;

import top.wefor.now.R;
import top.wefor.now.Urls;
import top.wefor.now.adapter.MomentAdapter;
import top.wefor.now.model.Moment;

/**
 * Created by ice on 15/10/28.
 */
public class MomentFragment extends BaseFragment {
    private static final int MSG_SUCCESS = 0;
    private static final int MSG_FAILURE = 1;

    private List<Moment> mMomentList;
    private MomentAdapter mAdapter;
    private Thread mThread;

    public static MomentFragment newInstance() {
        MomentFragment fragment = new MomentFragment();
        // TODO you can use bundle to transfer data
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mThread = new Thread() {
            @Override
            public void run() {
                getMomentList();
            }
        };
        threadStart();
        setRetainInstance(true);
    }

    public void threadStart() {
        if (mMomentList != null) return;
        mMomentList = new ArrayList<>();
        mThread.start();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());

        // set LayoutManager for your RecyclerView
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);

        mAdapter = new MomentAdapter(getActivity(), mMomentList);
        mRecyclerView.setAdapter(mAdapter);

        MaterialViewPagerHelper.registerRecyclerView(getActivity(), mRecyclerView, null);
    }

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {//此方法在ui线程运行
            switch (msg.what) {
                case MSG_SUCCESS:
                    mAdapter.notifyDataSetChanged();
                    stopLoadingAnim();
                    break;

                case MSG_FAILURE:
                    break;
            }
        }
    };

    private void getMomentList() {
        mMomentList.clear();
        Document doc = null;
        try {
            doc = Jsoup.connect(Urls.MOMENT_URL).get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (doc == null) return;
        // Links
        Element userWorks = doc.body().getElementById("selection");
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
            mMomentList.add(moment);
        }
        if (mMomentList.size() > 0) mHandler.obtainMessage(MSG_SUCCESS).sendToTarget();
    }
}
