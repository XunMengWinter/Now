package top.wefor.now.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import top.wefor.now.R;
import top.wefor.now.data.model.entity.TeaBean;
import top.wefor.now.ui.activity.BigImageActivity;
import top.wefor.now.utils.CommonUtils;
import top.wefor.now.utils.NowAppUtil;

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
            /* resize ImageView */
            cardViewHolder.mImageView.setAdjustViewBounds(false);
//            cardViewHolder.mImageView.setLayoutParams(
//                    new LinearLayout.LayoutParams(
//                            LinearLayout.LayoutParams.MATCH_PARENT,
//                            cardViewHolder.mImageView.getMeasuredWidth() * avatarBean.height / avatarBean.width)
//            );
            // because the above code has bug in IU, so use fresco.
            cardViewHolder.mImageView.setAspectRatio(1f * avatarBean.width / avatarBean.height);
        } else {
            /* wrap_content & adjustViewBounds */
            cardViewHolder.mImageView.setAdjustViewBounds(true);
            cardViewHolder.mImageView.setLayoutParams(
                    new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT)
            );
        }

        if (NowAppUtil.isBelowLollipop()) {
            //set round corner
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

//        if (pics.size() > 6 && pics.size() < 10) {
//            cardViewHolder.mMultiPicRv.setLayoutParams(
//                    new LinearLayout.LayoutParams(
//                            LinearLayout.LayoutParams.MATCH_PARENT,
//                            cardViewHolder.mMultiPicRv.getMeasuredWidth())
//            );
//        } else {
//            cardViewHolder.mMultiPicRv.setLayoutParams(
//                    new LinearLayout.LayoutParams(
//                            LinearLayout.LayoutParams.MATCH_PARENT,
//                            LinearLayout.LayoutParams.WRAP_CONTENT)
//            );
//        }
    }

    public static class CardViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.imageView) SimpleDraweeView mImageView;
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
