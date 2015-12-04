package top.wefor.now;

import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.WindowManager;

/**
 * Created by ice on 15/10/25.
 */
public class NoteActivity extends BaseSwipeBackCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        WindowManager.LayoutParams p = getWindow().getAttributes();     //获取对话框当前的参数值
        p.width = NowApplication.getWidth();
        p.height = (int) (NowApplication.getHeight() - getResources().getDimension(R.dimen.head_image_height));
        p.alpha = 1.0f;                                                 //设置本身透明度
        p.dimAmount = 0.2f;                                             //设置黑暗度
        getWindow().setAttributes(p);
        getWindow().setGravity(Gravity.BOTTOM);

        Log.i("xyz", " w h" + NowApplication.getWidth() + " " + NowApplication.getHeight());
    }
}
