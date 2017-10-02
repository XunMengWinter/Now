package top.wefor.now.ui.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.github.chrisbanes.photoview.PhotoView;
import com.github.chrisbanes.photoview.PhotoViewAttacher;
import com.tbruyelle.rxpermissions2.RxPermissions;

import top.wefor.now.App;
import top.wefor.now.R;
import top.wefor.now.ui.BaseCompatActivity;
import top.wefor.now.utils.ImageUtil;

/**
 * Created on 2016.
 * <p>
 * last edited at 2017/10/2.
 *
 * @author ice
 * @GitHub https://github.com/XunMengWinter
 */

public class BigImageActivity extends BaseCompatActivity {

    public static final String IMAGE_URL = "image_url";
    public static final String TRANSIT_PIC = "picture";

    PhotoViewAttacher mPhotoViewAttacher;
    PhotoView mSimpleDraweeView;
    private Drawable mImageDrawable;
    private String mImageUrl;

    public static void startThis(Context context, View view, String imageUrl) {
        Intent intent = new Intent(context, BigImageActivity.class);
        intent.putExtra(BigImageActivity.IMAGE_URL, imageUrl);
        ActivityOptionsCompat optionsCompat
                = ActivityOptionsCompat.makeSceneTransitionAnimation(
                (Activity) context, view, BigImageActivity.TRANSIT_PIC);
        try {
            ActivityCompat.startActivity(context, intent,
                    optionsCompat.toBundle());
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            context.startActivity(intent);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_big_image);
        //由于PhotoViewAttacher与Fresco不兼容，所以使用ImageView+Picasso.注意，
        mSimpleDraweeView = (PhotoView) findViewById(R.id.simpleDraweeView);

        mImageUrl = getIntent().getStringExtra(IMAGE_URL);
        if (!TextUtils.isEmpty(mImageUrl)) {
            ViewCompat.setTransitionName(mSimpleDraweeView, TRANSIT_PIC);
            Glide.with(this).load(mImageUrl).into(new SimpleTarget<Drawable>() {
                @Override
                public void onResourceReady(Drawable resource, Transition<? super Drawable> transition) {
                    mSimpleDraweeView.setImageDrawable(resource);
                    mImageDrawable = resource.getCurrent();
                }
            });
        }

        setupPhotoAttacher();

    }

    private void setupPhotoAttacher() {
        mPhotoViewAttacher = new PhotoViewAttacher(mSimpleDraweeView);
        mPhotoViewAttacher.setOnViewTapListener((view, v, v1) -> {
            onBackPressed();
        });

        mPhotoViewAttacher.setOnLongClickListener(v -> {
            if (mImageDrawable == null) {
                return false;
            }

            View view = getLayoutInflater().inflate(R.layout.dialog_save_image, null);

            AlertDialog alertDialog = new AlertDialog.Builder(BigImageActivity.this)
                    .setView(view)
                    .create();

            TextView textView = (TextView) view.findViewById(R.id.save_image_tv);
            textView.setOnClickListener(v1 -> {
                saveImage();
                alertDialog.dismiss();
            });

            alertDialog.show();
            return true;
        });
    }


    private void saveImage() {
        RxPermissions rxPermissions = new RxPermissions(this);
        rxPermissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe(isGranted -> {
                    if (isGranted) {
                        boolean isSaved = ImageUtil.saveImage(this, mImageDrawable, mImageUrl);
                        if (isSaved) {
                            App.showToast(getString(R.string.save_image_success, Environment.DIRECTORY_PICTURES));
                        } else {
                            App.showToast(R.string.save_image_failed);
                        }
                    } else {
                        new AlertDialog.Builder(BigImageActivity.this)
                                .setMessage(R.string.save_image_failed_permission)
                                .setPositiveButton(R.string.ok, null)
                                .create().show();
                    }
                });
    }

}
