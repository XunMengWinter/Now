package top.wefor.now.http;

import android.util.Log;

import rx.Observer;
import top.wefor.now.NowApplication;
import top.wefor.now.model.BaseResult;

/**
 * 网络请求返回需要的模型
 * Created by ice on 3/3/16.
 */
public abstract class BaseObserver<T extends BaseResult> implements Observer<T> {

    protected abstract void onSucceed(T result);

    protected void onFailed(String msg) {
        NowApplication.showToast(msg);
    }

    @Override
    public void onCompleted() {
        Log.i("xyz", "onCompleted");
    }

    @Override
    public void onError(Throwable e) {
        Log.i("xyz", "onError");
    }

    @Override
    public void onNext(T result) {
        if (result == null) {
            onFailed("result is null");
            return;
        }

        onSucceed(result);
//        onFailed(result.msg);
    }
}
