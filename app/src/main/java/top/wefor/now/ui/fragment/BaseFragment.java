package top.wefor.now.ui.fragment;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleRegistry;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

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
