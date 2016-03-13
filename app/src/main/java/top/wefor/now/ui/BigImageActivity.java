package top.wefor.now.ui;

import android.os.Bundle;
import android.app.Activity;
import android.text.TextUtils;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;

import top.wefor.now.NowApplication;
import top.wefor.now.R;
import uk.co.senab.photoview.PhotoView;

public class BigImageActivity extends BaseSwipeBackCompatActivity {

    public static final String IMAGE_URL = "image_url";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_big_image);
        PhotoView photoView = (PhotoView) findViewById(R.id.photoView);

        String imageUrl = getIntent().getStringExtra(IMAGE_URL);

        if (!TextUtils.isEmpty(imageUrl))
            Glide.with(this).load(imageUrl).into(photoView);
    }

}
