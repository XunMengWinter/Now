package top.wefor.now.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import top.wefor.now.R;
import top.wefor.now.data.model.entity.TeaBean;
import top.wefor.now.ui.activity.BigImageActivity;
import top.wefor.now.utils.CommonUtils;
import top.wefor.now.utils.NowAppUtil;

public class MonoAdapter extends BaseListAdapter<TeaBean.MeowBean> {

    public MonoAdapter(Context context, List<TeaBean.MeowBean> contents) {
        super(context, contents);
        setBigViewResId(R.layout.item_empty_head);
        setSmallViewResId(R.layout.item_mono);
    }

    @Override
    public CardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;

        switch (viewType) {
            case TYPE_HEADER: {
                view = LayoutInflater.from(parent.getContext())
                        .inflate(getBigViewResId(), parent, false);
                return new CardViewHolder(view, TYPE_HEADER);
            }
            case TYPE_CELL: {
                view = LayoutInflater.from(parent.getContext())
                        .inflate(getSmallViewResId(), parent, false);
                return new CardViewHolder(view);
            }
        }
        return null;
    }

    @Override
    protected void bindCellViewHolder(RecyclerView.ViewHolder cellViewHolder, int position) {
        TeaBean.MeowBean news = mList.get(position);
        CardViewHolder cardViewHolder = (CardViewHolder) cellViewHolder;

        if (CommonUtils.isAvatarAvailable(news.thumb)) {
            showSingleImage(cardViewHolder, news.thumb);
        } else if (news.images != null && news.images.size() > 0) {
            showSingleImage(cardViewHolder, news.images.get(0));
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

    private void showSingleImage(CardViewHolder cardViewHolder, @NonNull TeaBean.AvatarBean avatarBean) {
        if (avatarBean.height > 0 & avatarBean.width > 0) {
            cardViewHolder.mImageView.setAdjustViewBounds(false);
            cardViewHolder.mImageView.setAspectRatio(1f * avatarBean.width / avatarBean.height);
        } else {
            cardViewHolder.mImageView.setAdjustViewBounds(true);
            cardViewHolder.mImageView.setLayoutParams(
                    new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT)
            );
        }

        if (NowAppUtil.isBelowLollipop()) {
            RoundingParams roundingParams = new RoundingParams();
            int d2 = context.getResources().getDimensionPixelSize(R.dimen.d2);
            roundingParams.setCornersRadii(d2, d2, 0, 0);
            cardViewHolder.mImageView.getHierarchy().setRoundingParams(roundingParams);
        }

        cardViewHolder.mImageView.setVisibility(View.VISIBLE);
        cardViewHolder.mMultiPicRv.setVisibility(View.GONE);
        final String thisImageUrl = avatarBean.raw;
        Glide.with(context).load(thisImageUrl).into(cardViewHolder.mImageView);
        cardViewHolder.mImageView.setOnClickListener(v -> {
            BigImageActivity.startThis(context, v, thisImageUrl);
        });
    }

    private void showMultiPic(CardViewHolder cardViewHolder, List<TeaBean.AvatarBean> pics) {
        cardViewHolder.mImageView.setVisibility(View.GONE);
        cardViewHolder.mMultiPicRv.setVisibility(View.VISIBLE);
        cardViewHolder.mMultiPicRv.setLayoutManager(new GridLayoutManager(context, 3));
        MonoImageAdapter imageItemAdapter = new MonoImageAdapter(context, pics);
        cardViewHolder.mMultiPicRv.setAdapter(imageItemAdapter);
    }

    public static class CardViewHolder extends RecyclerView.ViewHolder {
        SimpleDraweeView mImageView;
        RecyclerView mMultiPicRv;
        TextView mTitleTextView;
        TextView mContentTextView;
        LinearLayout mRootView;
        CardView mNewsListCardView;

        public CardViewHolder(View v) {
            super(v);
            bindViewId(v);
        }
        private void bindViewId(View v){
            mImageView = v.findViewById(R.id.imageView);
            mMultiPicRv = v.findViewById(R.id.multi_pic_rv);
            mTitleTextView = v.findViewById(R.id.title_textView);
            mContentTextView = v.findViewById(R.id.content_textView);
            mRootView = v.findViewById(R.id.rootView);
            mNewsListCardView = v.findViewById(R.id.news_list_card_view);
        }

        public CardViewHolder(View v, int viewType) {
            super(v);
            if(viewType == TYPE_CELL) {
                bindViewId(v);
            }
        }
    }
}