/*
 * Copyright (C) 2016 Jake Wharton
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package top.wefor.now.data.http;

import android.util.Log;

import java.lang.reflect.Type;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
import retrofit2.Call;
import retrofit2.CallAdapter;

/**
 * TODO...make this support Scheduler set.
 */

final class RxAndroidCallAdapter<R> implements CallAdapter<R, Object> {

    private Type mResponseType;
    private Scheduler mSubscribeScheduler;
    private Scheduler mObserveScheduler;

    public RxAndroidCallAdapter(Type responseType, Scheduler subscribeScheduler, Scheduler observeScheduler) {
        mResponseType = responseType;
        mSubscribeScheduler = subscribeScheduler;
        mObserveScheduler = observeScheduler;
    }

    @Override
    public Type responseType() {
        return mResponseType;
    }

    @Override
    public Object adapt(Call<R> call) {
        if (call instanceof Observable) {
            Observable observable = (Observable) call;

            if (mSubscribeScheduler != null)
                observable = observable.subscribeOn(mSubscribeScheduler);

            if (mObserveScheduler != null)
                observable = observable.observeOn(mObserveScheduler);

            Log.i("xyz", "get get");
            return observable;
        }
        return call;
    }

}
