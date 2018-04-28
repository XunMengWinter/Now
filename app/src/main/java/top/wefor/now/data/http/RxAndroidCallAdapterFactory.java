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

import com.orhanobut.logger.Logger;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import javax.annotation.Nullable;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
import retrofit2.CallAdapter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

/**
 * TODO...make this support Scheduler set.
 */
public final class RxAndroidCallAdapterFactory extends CallAdapter.Factory {

    private Scheduler mSubscribeScheduler;
    private Scheduler mObserveScheduler;


    public static RxAndroidCallAdapterFactory createWithScheduler(Scheduler subscribeScheduler, Scheduler observeScheduler){
        return new RxAndroidCallAdapterFactory(subscribeScheduler, observeScheduler);
    }


    private RxAndroidCallAdapterFactory(Scheduler subscribeScheduler, Scheduler observeScheduler){
        mSubscribeScheduler = subscribeScheduler;
        mObserveScheduler = observeScheduler;
    }

    @Nullable
    @Override
    public CallAdapter<?, ?> get(Type returnType, Annotation[] annotations, Retrofit retrofit) {
        Class<?> rawType = getRawType(returnType);
        Log.e("xxxx",rawType.getName());
        return new RxAndroidCallAdapter<>(returnType, mSubscribeScheduler, mObserveScheduler);
    }

}
