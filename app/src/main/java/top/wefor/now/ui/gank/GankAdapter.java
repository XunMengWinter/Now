package top.wefor.now.ui.gank;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.DrawableImageViewTarget;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import top.wefor.now.R;
import top.wefor.now.data.model.entity.Gank;
import top.wefor.now.ui.adapter.BaseRecyclerViewAdapter;

/**
 * Created on 16/7/7.
 *
 * @author ice
 */
public class GankAdapter extends BaseRecyclerViewAdapter<Gank> {
    public GankAdapter(Context context, List<Gank> list, RecyclerView recyclerView) {
        super(context, list, recyclerView);
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.item_gank;
    }

    @Override
    protected RecyclerView.ViewHolder getViewHolder(View view) {
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        MyViewHolder myViewHolder = (MyViewHolder) holder;
        Gank gank = mList.get(position);
        myViewHolder.mNameTv.setText(gank.desc);
        myViewHolder.mUrlTv.setText(gank.url);
        if (gank.images != null && !gank.images.isEmpty()) {
            myViewHolder.mImageIv.setVisibility(View.VISIBLE);
            final String imageUrl = gank.images.get(0);
            RequestOptions requestOptions = new RequestOptions();
            int imageWidth = context.getResources().getDimensionPixelSize(R.dimen.width_gank_image);
            requestOptions.override(imageWidth);
            Glide.with(context)
                    .load(imageUrl)
                    .apply(requestOptions)
                    .into(myViewHolder.mImageIv);
//                        Logger.e("gank item load image failed:" + imageUrl);
//            DraweeController controller = Fresco.newDraweeControllerBuilder()
//                    .setUri(imageUrl)
//                    .setAutoPlayAnimations(true)
//                    .build();
//            myViewHolder.mImageIv.setController(controller);
        } else {
            myViewHolder.mImageIv.setVisibility(View.GONE);
        }
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.name_tv) TextView mNameTv;
        @BindView(R.id.url_tv) TextView mUrlTv;
        @BindView(R.id.image_iv) SimpleDraweeView mImageIv;

        MyViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
