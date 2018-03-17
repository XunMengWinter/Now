package top.wefor.now.ui;

import android.support.v7.app.AppCompatActivity;


/**
 * Created on 15/10/25.
 * @author ice
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
