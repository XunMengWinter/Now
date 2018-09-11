package top.wefor.now;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.OnLifecycleEvent;

import com.orhanobut.logger.Logger;

/**
 * 用于测试 Lifecycle，使用方法很简单，在需要测试的类里实例化一个 LifecycleTest 即可。
 * <p>
 * Created on 2018/9/11.
 *
 * @author ice
 */
public class LifecycleTest implements LifecycleObserver {

    private static final String TAG = "lifecycle";

    private Lifecycle mLifecycle;

    public LifecycleTest(Lifecycle lifecycle) {
        mLifecycle = lifecycle;
        mLifecycle.addObserver(this);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    public void create() {
        print();
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    public void start() {
        print();
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    public void resume() {
        print();
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    public void pause() {
        print();
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    public void stop() {
        print();
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    public void destroy() {
        print();
        mLifecycle.removeObserver(this);
    }


    private void print() {
        Logger.t(TAG).i(mLifecycle.getCurrentState().name());
    }

}
