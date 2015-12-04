package top.wefor.now;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;

import com.umeng.analytics.MobclickAgent;

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

    protected boolean isWifiConnected(Context context) {
        ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (mWifi.isConnected()) {
            // Do whatever
            return true;
        }
        return false;
    }


    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

}
