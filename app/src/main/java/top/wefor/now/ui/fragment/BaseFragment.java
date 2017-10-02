package top.wefor.now.ui.fragment;

import android.support.v4.app.Fragment;

/**
 * Created by ice on 16/4/14 23:48.
 */
public class BaseFragment extends Fragment {

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

}
