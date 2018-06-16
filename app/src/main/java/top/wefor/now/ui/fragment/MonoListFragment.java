package top.wefor.now.ui.fragment;

import android.support.annotation.NonNull;

import com.github.florent37.materialviewpager.MaterialViewPagerHelper;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import top.wefor.now.Constants;
import top.wefor.now.R;
import top.wefor.now.data.http.BaseObserver;
import top.wefor.now.data.http.NowApi;
import top.wefor.now.data.model.Mono;
import top.wefor.now.data.model.MonoTea;
import top.wefor.now.data.model.MonoToken;
import top.wefor.now.data.model.entity.TeaBean;
import top.wefor.now.data.model.realm.RealmMono;
import top.wefor.now.ui.activity.WebActivity;
import top.wefor.now.ui.adapter.BaseListAdapter;
import top.wefor.now.ui.adapter.MonoAdapter;
import top.wefor.now.utils.PrefUtil;

/**
 * Created on 2018/6/12.
 *
 * @author ice
 */
public class MonoListFragment extends BaseListFragment<TeaBean.MeowBean, RealmMono> {

    public static MonoListFragment newInstance() {
        MonoListFragment fragment = new MonoListFragment();
        return fragment;
    }

    @Override
    protected void initRecyclerView() {
        super.initRecyclerView();

        mRecyclerView.setAdapter(mAdapter);
        MaterialViewPagerHelper.registerRecyclerView(getActivity(), mRecyclerView);

        mAdapter.setOnItemClickListener(news -> {
            WebActivity.startThis(getActivity(), news.rec_url, news.title, news.getCover(),
                    getString(R.string.share_summary_zhihu));
        });

        if (mList.size() < 1) {
            getData();
        }
    }

    @Override
    public void getData() {
        if (!PrefUtil.isNeedRefresh(Constants.KEY_REFRESH_TIME_MONO)) {
            showList();
            return;
        }
        NowApi.getMonoApi().getToken(new Mono())
                .flatMap((Function<MonoToken, ObservableSource<MonoTea>>) monoToken -> NowApi.getMonoApi().getTea(monoToken.access_token, getDate()))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<MonoTea>() {
                    @Override
                    protected void onSucceed(MonoTea result) {
                        mList.clear();
                        addTea(result.afternoon_tea);
                        addTea(result.morning_tea);
                        if (mList.size() > 0)
                            PrefUtil.setRefreshTime(Constants.KEY_REFRESH_TIME_MONO, new Date().getTime());
                        saveData();
                        showList();
                    }

                    @Override
                    protected void onFailed(String msg) {
                        super.onFailed(msg);
                        showList();
                    }
                });
    }

    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);

    private String getDate() {
        return DATE_FORMAT.format(new Date());
    }

    private void addTea(TeaBean teaBean) {
        if (teaBean == null || teaBean.entity_list == null) {
            return;
        }

        for (TeaBean.EntityListBean entityListBean : teaBean.entity_list) {
            if (entityListBean.meow != null)
                mList.add(entityListBean.meow);
        }
    }

    @NonNull
    @Override
    public Class<RealmMono> getNowRealmClass() {
        return RealmMono.class;
    }

    @Override
    public BaseListAdapter<TeaBean.MeowBean> getNowAdapter(List<TeaBean.MeowBean> list) {
        return new MonoAdapter(getActivity(), list);
    }

}
