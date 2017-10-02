package top.wefor.now.ui;

import android.support.v7.app.AppCompatActivity;


/**
 * Created by ice on 15/10/25.
 */
public abstract class BaseCompatActivity extends AppCompatActivity {

    protected String pass(String string) {
        if (string == null) string = "";
        return string;
    }

    protected String pass(Integer integer) {
        if (integer == null) return "";
        return "" + integer;
    }

}
