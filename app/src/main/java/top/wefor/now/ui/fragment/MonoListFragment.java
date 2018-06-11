package top.wefor.now.ui.fragment;

import android.support.annotation.NonNull;
import android.util.Log;

import com.orhanobut.logger.Logger;

import java.util.List;

import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import top.wefor.now.data.http.BaseObserver;
import top.wefor.now.data.http.NowApi;
import top.wefor.now.data.model.Mono;
import top.wefor.now.data.model.MonoTea;
import top.wefor.now.data.model.MonoToken;
import top.wefor.now.ui.adapter.BaseListAdapter;

/**
 * Created on 2018/6/12.
 *
 * @author ice
 */
public class MonoListFragment extends BaseListFragment {
    @Override
    public void getData() {
        NowApi.getMonoApi().getToken(new Mono())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap((Function<MonoToken, ObservableSource<MonoTea>>) monoToken -> NowApi.getMonoApi().getTea(monoToken.access_token, "2018-06-11"))
                .subscribe(new BaseObserver<MonoTea>() {
                    @Override
                    protected void onSucceed(MonoTea result) {
                        Logger.i("xyz mono", result.afternoon_tea.title);
                    }
                });
    }

    @NonNull
    @Override
    public Class getNowRealmClass() {
        return null;
    }

    @Override
    public BaseListAdapter getNowAdapter(List list) {
        return null;
    }
}
