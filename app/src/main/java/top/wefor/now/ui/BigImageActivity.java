package top.wefor.now.ui;

import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.text.TextUtils;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import top.wefor.now.R;
import uk.co.senab.photoview.PhotoViewAttacher;

public class BigImageActivity extends BaseSwipeBackCompatActivity {

    public static final String IMAGE_URL = "image_url";
    public static final String TRANSIT_PIC = "picture";
    PhotoViewAttacher mPhotoViewAttacher;
    ImageView mSimpleDraweeView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_big_image);
        //由于PhotoViewAttacher与Fresco不兼容，所以使用ImageView+Picasso.注意，
        mSimpleDraweeView = (ImageView) findViewById(R.id.simpleDraweeView);

        String imageUrl = getIntent().getStringExtra(IMAGE_URL);
        if (!TextUtils.isEmpty(imageUrl)) {
            ViewCompat.setTransitionName(mSimpleDraweeView, TRANSIT_PIC);
            Picasso.with(this).load(imageUrl).into(mSimpleDraweeView);
        }

        setupPhotoAttacher();
    }

    private void setupPhotoAttacher() {
        mPhotoViewAttacher = new PhotoViewAttacher(mSimpleDraweeView);
        mPhotoViewAttacher.setOnViewTapListener((view, v, v1) -> {
            onBackPressed();
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPhotoViewAttacher.cleanup();
    }
}
