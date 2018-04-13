package top.wefor.now.data.http;

import android.arch.lifecycle.Lifecycle;

import top.wefor.now.data.model.BaseResult;

/**
 * 网络请求返回需要的模型
 * Created by ice on 3/3/16.
 */
public abstract class BaseHttpObserver<T extends BaseResult> extends BaseObserver<T> {

    public BaseHttpObserver() {
        super();
    }

    public BaseHttpObserver(Lifecycle lifecycle) {
        super(lifecycle);
    }

}
