package top.wefor.now.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.alibaba.fastjson.JSONArray;
import com.github.florent37.materialviewpager.MaterialViewPagerHelper;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import top.wefor.now.R;
import top.wefor.now.Urls;
import top.wefor.now.adapter.NGAdapter;
import top.wefor.now.model.NG;
import top.wefor.now.utils.Constants;

/**
 * Created by ice on 15/10/28.
 */
public class NGFragment extends BaseFragment {
    private static final int MSG_SUCCESS = 0;
    private static final int MSG_FAILURE = 1;
    private static final int SIZE = 10;

    private List<NG> mNGList;
    private NGAdapter mAdapter;
    private Thread mThread;

    public static NGFragment newInstance() {
        NGFragment fragment = new NGFragment();
        // TODO you can use bundle to transfer data
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mThread = new Thread() {
            @Override
            public void run() {
                getNGList();
            }
        };
        threadStart();
        setRetainInstance(true);
    }

    public void threadStart() {
        if (mNGList != null) return;
        mNGList = new ArrayList<>();
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

        mAdapter = new NGAdapter(getActivity(), mNGList);
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

    private void getNGList() {
        mNGList.clear();
//        String nGUrl = "http://photography.nationalgeographic.com/photography/";
        Document doc = null;
        try {
            doc = Jsoup.connect(Urls.NG_BASE_URL).get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (doc == null) return;
        Log.i("xyz contents ", doc.ownText());
        Element contents = doc.getElementById("ajaxBox");
        Elements list = contents.getElementsByClass("ajax_list");
        for (int i = 0; i < SIZE && i < list.size(); i++) {
            Element element = list.get(i);
            NG nG = new NG();
            Element imageA = element.select("dd").first().select("a").first();
            nG.url = pass(imageA.attr("href"));
            nG.imgUrl = pass(imageA.select("img").first().attr("src"));
            nG.title = pass(imageA.select("img").first().attr("alt"));
            nG.content = pass(element.getElementsByClass("ajax_dd_text").first().ownText());

            Log.i("xyz ", "ngfragment " + nG.content + nG.imgUrl + nG.url + nG.title);
            mNGList.add(nG);
        }
        if (mNGList.size() > 0) {
            mHandler.obtainMessage(MSG_SUCCESS).sendToTarget();

            SharedPreferences preferences = getActivity().getSharedPreferences(Constants.PREFS_NAME, 0);
            int type = preferences.getInt(Constants.COVER_SOURCE, 0);
            // setHeadImages
            if (type == Constants.TYPE_NG) {
                SharedPreferences.Editor editor = preferences.edit();
                JSONArray jsonArray = new JSONArray();
                for (NG ng : mNGList)
                    jsonArray.add(ng.imgUrl);
                editor.putString(Constants.HEAD_IMAGES, jsonArray.toJSONString());
                editor.apply();
            }

        }
    }

}
