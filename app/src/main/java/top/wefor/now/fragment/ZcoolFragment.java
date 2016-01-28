package top.wefor.now.fragment;

import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.github.florent37.materialviewpager.MaterialViewPagerHelper;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter;
import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;
import jp.wasabeef.recyclerview.animators.FadeInAnimator;
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

        mZcoolList = new ArrayList<>();

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                getZcoolList();
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                if (mZcoolList.size() > 0) {
                    mAdapter.notifyDataSetChanged();
                    stopLoadingAnim();
                } else
                    showLoadingAnim();
            }
        }.execute();

        setRetainInstance(true);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        stopLoadingAnim();

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        mAdapter = new ZcoolAdapter(getActivity(), mZcoolList);

        // set LayoutManager for your RecyclerView
        if (getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
            mAdapter.setImageWidthAndHeight(2);
        } else {
            mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
            mAdapter.setImageWidthAndHeight(3);
        }

        mRecyclerView.setHasFixedSize(true);

        mRecyclerView.setItemAnimator(new FadeInAnimator());
        AlphaInAnimationAdapter alphaAdapter = new AlphaInAnimationAdapter(mAdapter);
        ScaleInAnimationAdapter scaleAdapter = new ScaleInAnimationAdapter(alphaAdapter);
//        scaleAdapter.setFirstOnly(false);
//        scaleAdapter.setInterpolator(new OvershootInterpolator());
        mRecyclerView.setAdapter(scaleAdapter);

        MaterialViewPagerHelper.registerRecyclerView(getActivity(), mRecyclerView, null);
    }

    private void getZcoolList() {
        mZcoolList.clear();
        Document doc = null;
        try {
            doc = Jsoup.connect(Urls.ZCOOL_URL).get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (doc == null)
            return;

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
    }

}
