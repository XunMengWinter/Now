package top.wefor.now.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import top.wefor.now.R;
import top.wefor.now.data.model.entity.TeaBean;
import top.wefor.now.ui.activity.BigImageActivity;
import top.wefor.now.ui.activity.BigImagePagerActivity;

/**
 * Created by ice on 18/06/13.
 */
public class MonoAdapter extends BaseListAdapter<TeaBean.MeowBean> {

    public MonoAdapter(Context context, List<TeaBean.MeowBean> contents) {
        super(context, contents);
        setBigViewResId(R.layout.item_empty_head);
        setSmallViewResId(R.layout.item_mono);
    }

    @Override
    public int getItemCount() {
        return super.getItemCount();
    }

    @Override
    public CardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;

        switch (viewType) {
            case TYPE_HEADER: {
                view = LayoutInflater.from(parent.getContext())
                        .inflate(getBigViewResId(), parent, false);
                return new CardViewHolder(view, TYPE_HEADER) {
                };
            }
            case TYPE_CELL: {
                view = LayoutInflater.from(parent.getContext())
                        .inflate(getSmallViewResId(), parent, false);
                return new CardViewHolder(view) {
                };
            }
        }
        return null;
    }

    @Override
    protected void bindCellViewHolder(RecyclerView.ViewHolder cellViewHolder, int position) {
        TeaBean.MeowBean news = mList.get(position);
        CardViewHolder cardViewHolder = (CardViewHolder) cellViewHolder;

        if (isAvatarAvailable(news.thumb)) {
            showSingleImage(cardViewHolder, news.thumb.raw, null);
        } else if (news.images != null && news.images.size() > 0) {
            showSingleImage(cardViewHolder, null, news.images);
        } else if (news.pics != null && news.pics.size() > 0) {
            showMultiPic(cardViewHolder, news.pics);
        } else {
            cardViewHolder.mImageView.setVisibility(View.GONE);
            cardViewHolder.mMultiPicRv.setVisibility(View.GONE);
        }
        cardViewHolder.mTitleTextView.setText(news.title);
        cardViewHolder.mTitleTextView.setVisibility(news.title == null ? View.GONE : View.VISIBLE);
        cardViewHolder.mContentTextView.setText(news.description);
        cardViewHolder.mContentTextView.setVisibility(news.description == null ? View.GONE : View.VISIBLE);
    }

    private void showSingleImage(CardViewHolder cardViewHolder, @Nullable final String imageUrl, @Nullable final List<TeaBean.AvatarBean> images) {
        cardViewHolder.mImageView.setVisibility(View.VISIBLE);
        cardViewHolder.mMultiPicRv.setVisibility(View.GONE);
        String thisImageUrl = imageUrl;
        if (images != null && images.size() > 0) {
            thisImageUrl = images.get(0).raw;
        }
        Glide.with(context).load(thisImageUrl).into(cardViewHolder.mImageView);
        cardViewHolder.mImageView.setOnClickListener(v -> {
            if (imageUrl != null) {
                BigImageActivity.startThis(context, v, imageUrl);
            } else if (images != null) {
                List<View> views = new ArrayList<>();
                views.add(cardViewHolder.mImageView);
                List<String> imgUrls = new ArrayList<>();
                for (TeaBean.AvatarBean avatarBean : images) {
                    imgUrls.add(avatarBean.raw);
                }
                BigImagePagerActivity.startThis((AppCompatActivity) context, views, imgUrls, 0);
            }
        });
    }

    private void showMultiPic(CardViewHolder cardViewHolder, List<TeaBean.AvatarBean> pics) {
        cardViewHolder.mImageView.setVisibility(View.GONE);
        cardViewHolder.mMultiPicRv.setVisibility(View.VISIBLE);
        cardViewHolder.mMultiPicRv.setLayoutManager(new GridLayoutManager(context, 3));
        ImageItemAdapter imageItemAdapter = new ImageItemAdapter(context, pics);
        cardViewHolder.mMultiPicRv.setAdapter(imageItemAdapter);
    }


    private static class ImageItemAdapter extends RecyclerView.Adapter<ImageItemViewHolder> {

        private List<TeaBean.AvatarBean> mList;
        private List<View> mViews = new ArrayList<>();
        private Context mContext;

        public ImageItemAdapter(@NonNull Context context, @NonNull List<TeaBean.AvatarBean> pics) {
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
            if (isAvatarAvailable(mList.get(position))) {
                Glide.with(mContext).load(mList.get(position).raw).into(holder.mImageView);

                holder.mImageView.setOnClickListener(v -> {
                    List<String> imgUrls = new ArrayList<>();
                    for (TeaBean.AvatarBean avatarBean : mList) {
                        imgUrls.add(avatarBean.raw);
                    }
                    BigImagePagerActivity.startThis((AppCompatActivity) mContext, mViews, imgUrls, position);
                });
            }
        }

        @Override
        public int getItemCount() {
            return mList.size();
        }
    }


    public static class ImageItemViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.imageView) AppCompatImageView mImageView;

        ImageItemViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    private static boolean isAvatarAvailable(TeaBean.AvatarBean avatarBean) {
        return (avatarBean != null && !TextUtils.isEmpty(avatarBean.raw));
    }

    public static class CardViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.imageView) AppCompatImageView mImageView;
        @BindView(R.id.multi_pic_rv) RecyclerView mMultiPicRv;
        @BindView(R.id.title_textView) TextView mTitleTextView;
        @BindView(R.id.content_textView) TextView mContentTextView;
        @BindView(R.id.rootView) LinearLayout mRootView;
        @BindView(R.id.news_list_card_view) CardView mNewsListCardView;


        public CardViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }

        public CardViewHolder(View v, int viewType) {
            super(v);
            if (viewType == TYPE_CELL)
                ButterKnife.bind(this, v);
        }

    }


}
