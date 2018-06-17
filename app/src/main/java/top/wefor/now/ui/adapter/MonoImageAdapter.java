package top.wefor.now.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.facebook.drawee.view.SimpleDraweeView;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import top.wefor.now.R;
import top.wefor.now.data.model.entity.TeaBean;
import top.wefor.now.ui.activity.BigImagePagerActivity;
import top.wefor.now.utils.CommonUtils;

/**
 * Created on 2018/6/17.
 *
 * @author ice
 */
public class MonoImageAdapter extends RecyclerView.Adapter<MonoImageAdapter.ImageItemViewHolder> {

    private List<TeaBean.AvatarBean> mList;
    private List<View> mViews = new ArrayList<>();
    private Context mContext;

    public MonoImageAdapter(@NonNull Context context, @NonNull List<TeaBean.AvatarBean> pics) {
        mContext = context;
        mList = pics;
        mViews.clear();
        for (Object object : mList) {
            mViews.add(null);
        }
    }

    @NonNull
    @Override
    public ImageItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_mono_image, parent, false);
        return new ImageItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageItemViewHolder holder, int position) {
        mViews.set(position, holder.mImageView);
        TeaBean.AvatarBean model = mList.get(position);
        if (CommonUtils.isAvatarAvailable(model)) {
            holder.mImageView.setVisibility(View.VISIBLE);
            Glide.with(mContext).load(mList.get(position).raw).into(holder.mImageView);

            if (model.width > 0 && model.height > 0) {
                holder.mImageView.setAdjustViewBounds(false);
                holder.mImageView.setAspectRatio(1f * model.width / model.height);
            } else {
                try {
                    holder.mImageView.setAdjustViewBounds(true);
                    holder.mImageView.setLayoutParams(
                            new RecyclerView.LayoutParams(
                                    RecyclerView.LayoutParams.MATCH_PARENT,
                                    RecyclerView.LayoutParams.WRAP_CONTENT)
                    );
                } catch (Exception e) {
                    e.printStackTrace();
                    Logger.e("RecyclerView.LayoutParams " + e.getMessage());
                }
            }

            holder.mImageView.setOnClickListener(v -> {
                List<String> imgUrls = new ArrayList<>();
                for (TeaBean.AvatarBean avatarBean : mList) {
                    imgUrls.add(avatarBean.raw);
                }
                BigImagePagerActivity.startThis((AppCompatActivity) mContext, mViews, imgUrls, position);
            });
        } else {
            holder.mImageView.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    static class ImageItemViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.imageView) SimpleDraweeView mImageView;

        ImageItemViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}

