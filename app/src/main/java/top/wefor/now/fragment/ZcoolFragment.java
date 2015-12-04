package top.wefor.now.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.github.florent37.materialviewpager.MaterialViewPagerHelper;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import top.wefor.now.R;
import top.wefor.now.Urls;
import top.wefor.now.adapter.ZcoolAdapter;
import top.wefor.now.model.Zcool;

/**
 * Created by ice on 15/10/28.
 */
public class ZcoolFragment extends BaseFragment {
    private static final int MSG_SUCCESS = 1;
    private static final int MSG_FAILURE = 0;

    private List<Zcool> mZcoolList;
    private ZcoolAdapter mAdapter;
    private Thread mThread;

    public static ZcoolFragment newInstance() {
        ZcoolFragment fragment = new ZcoolFragment();
        // TODO you can use bundle to transfer data
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mThread = new Thread() {
            @Override
            public void run() {
                getZcoolList();
            }
        };
        threadStart();
        setRetainInstance(true);
    }

    public void threadStart() {
        if (mZcoolList != null) return;
        mZcoolList = new ArrayList<>();
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

        mAdapter = new ZcoolAdapter(getActivity(), mZcoolList);
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

    private void getZcoolList() {
        mZcoolList.clear();
        Document doc = null;
        try {
            doc = Jsoup.connect(Urls.ZCOOL_URL).get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (doc == null) return;
        // Links
        Element userWorks = doc.body().getElementById("user-works");
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
        if (mZcoolList.size() > 0) mHandler.obtainMessage(MSG_SUCCESS).sendToTarget();
    }
}
