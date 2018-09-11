package top.wefor.now.data.http;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.OnLifecycleEvent;

import com.orhanobut.logger.Logger;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created on 2018/4/13.
 *
 * @author ice
 */
public abstract class BaseObserver<T> implements Observer<T>, LifecycleObserver {

    public static final String TAG = "BaseObserver";

    protected abstract void onSucceed(T result);

    private Lifecycle mLifecycle;
    private Disposable mDisposable;

    public BaseObserver() {

    }

    public BaseObserver(Lifecycle lifecycle) {
        mLifecycle = lifecycle;
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    public void onDisPose() {
        Logger.t(TAG).i("dispose");
        if (mLifecycle != null)
            mLifecycle.removeObserver(this);
        if (mDisposable != null && !mDisposable.isDisposed())
            mDisposable.dispose();
    }

    @Override
    public void onComplete() {
//        Logger.i(TAG, "onComplete");
        onEnd();
    }

    @Override
    public void onError(Throwable e) {
        e.printStackTrace();
        Logger.w("onError " + e.getMessage());
        onFailed(e.getMessage());
        onEnd();
    }

    @Override
    public void onSubscribe(Disposable d) {
        mDisposable = d;
        if (mLifecycle != null)
            mLifecycle.addObserver(this);
    }

    @Override
    public void onNext(T result) {
        if (result != null)
            onSucceed(result);
        else
            onFailed(null);
        onEnd();
    }

    protected void onFailed(String msg) {

    }

    protected void onEnd() {
        onDisPose();
    }
}
