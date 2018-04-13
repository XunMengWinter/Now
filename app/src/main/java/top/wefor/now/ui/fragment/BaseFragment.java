package top.wefor.now.ui.fragment;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleRegistry;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;

/**
 * Created on 16/4/14 23:48.
 *
 * @author ice
 */
public class BaseFragment extends Fragment {

    protected LifecycleRegistry mLifecycleRegistry = (LifecycleRegistry) getLifecycle();

    public static BaseFragment newInstance() {
        return new BaseFragment();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        handleLifecycleEvent(Lifecycle.Event.ON_CREATE);
    }

    @Override
    public void onStart() {
        super.onStart();
        handleLifecycleEvent(Lifecycle.Event.ON_START);
    }

    @Override
    public void onResume() {
        super.onResume();
        handleLifecycleEvent(Lifecycle.Event.ON_RESUME);
    }

    @Override
    public void onPause() {
        handleLifecycleEvent(Lifecycle.Event.ON_PAUSE);
        super.onPause();
    }

    @Override
    public void onStop() {
        handleLifecycleEvent(Lifecycle.Event.ON_STOP);
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        handleLifecycleEvent(Lifecycle.Event.ON_DESTROY);
        super.onDestroyView();
    }

    protected String pass(String string) {
        if (string == null) string = "";
        return string;
    }

    protected String pass(Integer integer) {
        if (integer == null) return "";
        return "" + integer;
    }

    public void handleLifecycleEvent(@NonNull Lifecycle.Event event) {
        mLifecycleRegistry.handleLifecycleEvent(event);
    }

}
