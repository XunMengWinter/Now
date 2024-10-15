package top.wefor.now.ui.gank;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

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

        // Set text content
        myViewHolder.mNameTv.setText(gank.desc);
        myViewHolder.mUrlTv.setText(gank.url);

        // Load image if available
        if (gank.images != null && !gank.images.isEmpty()) {
            myViewHolder.mImageIv.setVisibility(View.VISIBLE);
            String imageUrl = gank.images.get(0);
            RequestOptions requestOptions = new RequestOptions();
            int imageWidth = context.getResources().getDimensionPixelSize(R.dimen.width_gank_image);
            requestOptions.override(imageWidth);
            Glide.with(context)
                    .load(imageUrl)
                    .apply(requestOptions)
                    .into(myViewHolder.mImageIv);
        } else {
            myViewHolder.mImageIv.setVisibility(View.GONE);
        }
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView mNameTv;
        TextView mUrlTv;
        SimpleDraweeView mImageIv;

        MyViewHolder(View view) {
            super(view);
            // Manually bind views
            mNameTv = view.findViewById(R.id.name_tv);
            mUrlTv = view.findViewById(R.id.url_tv);
            mImageIv = view.findViewById(R.id.image_iv);
        }
    }
}