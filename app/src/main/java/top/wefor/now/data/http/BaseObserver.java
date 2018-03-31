package top.wefor.now.data.http;

import android.text.TextUtils;
import android.util.Log;

import io.reactivex.Observer;
import top.wefor.now.App;
import top.wefor.now.data.model.BaseResult;

/**
 * 网络请求返回需要的模型
 * Created by ice on 3/3/16.
 */
public abstract class BaseObserver<T extends BaseResult> implements Observer<T> {

    protected abstract void onSucceed(T result);

    protected void onFailed(String msg) {
        if (!TextUtils.isEmpty(msg))
            App.showToast(msg);
    }

    @Override
    public void onComplete() {
        Log.i("xyz", "onComplete");
    }

    @Override
    public void onError(Throwable e) {
        e.printStackTrace();
        Log.i("xyz", "onError " + e.getMessage());
        onFailed(null);
    }

    @Override
    public void onNext(T result) {
        if (result != null)
            onSucceed(result);
        else
            onFailed(null);

    }
}
