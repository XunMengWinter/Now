package top.wefor.now.ui.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import top.wefor.now.App;
import top.wefor.now.R;
import top.wefor.now.data.model.entity.Zcool;
import top.wefor.now.ui.activity.BigImageActivity;
import top.wefor.now.utils.NowAppUtil;

/**
 * Created by ice on 15/10/26.
 */
public class ZcoolAdapter extends BaseListAdapter<Zcool> {

    public Integer IMAGE_WIDTH, IMAGE_HEIGHT;

    /**
     * fit px by px
     */
    public void setImageWidthAndHeight(int columns) {
        int d3 = context.getResources().getDimensionPixelSize(R.dimen.d3);
        IMAGE_WIDTH = (NowAppUtil.getWidth() - d3 * 2) / columns - d3 * 2;
        IMAGE_HEIGHT = IMAGE_WIDTH * 3 / 4;
    }

    public ZcoolAdapter(Context context, List<Zcool> contents) {
        super(context, contents);
        setBigViewResId(R.layout.item_empty_head);
        setSmallViewResId(R.layout.item_zcool);
    }

    @Override
    public CardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;

        switch (viewType) {
            case TYPE_HEADER: {
                view = LayoutInflater.from(parent.getContext())
                        .inflate(getBigViewResId(), parent, false);
                return new CardViewHolder(view, TYPE_HEADER);
            }
            case TYPE_CELL: {
                view = LayoutInflater.from(parent.getContext())
                        .inflate(getSmallViewResId(), parent, false);
                return new CardViewHolder(view, TYPE_CELL);
            }
        }
        return null;
    }

    @Override
    protected void bindCellViewHolder(RecyclerView.ViewHolder cellViewHolder, int position) {
        Zcool news = mList.get(position);
        CardViewHolder cardViewHolder = (CardViewHolder) cellViewHolder;

        if (!TextUtils.isEmpty(news.imgUrl)) {
            Glide.with(context).load(news.imgUrl).into(cardViewHolder.mSimpleDraweeView);
            cardViewHolder.mSimpleDraweeView.setOnClickListener(v -> {
                BigImageActivity.startThis(context, v, news.imgUrl);
            });
        }
        cardViewHolder.mTitleTv.setText(news.title);
        cardViewHolder.mNameTv.setText("by " + news.name);
        cardViewHolder.mReadTv.setText(news.readCount + " 看过");
        cardViewHolder.mLikeTv.setText(news.likeCount + " 赞");
    }

    public static class CardViewHolder extends RecyclerView.ViewHolder {
        CardView mCardView;
        TextView mTitleTv;
        TextView mNameTv;
        TextView mReadTv;
        TextView mLikeTv;
        SimpleDraweeView mSimpleDraweeView;

        public CardViewHolder(View v) {
            super(v);

            // Manually bind views
            mCardView = v.findViewById(R.id.news_list_card_view);
            mTitleTv = v.findViewById(R.id.title_textView);
            mNameTv = v.findViewById(R.id.name_textView);
            mReadTv = v.findViewById(R.id.read_textView);
            mLikeTv = v.findViewById(R.id.like_textView);
            mSimpleDraweeView = v.findViewById(R.id.simpleDraweeView);

            // Handle rounding for devices below Lollipop
            if (NowAppUtil.isBelowLollipop()) {
                RoundingParams roundingParams = new RoundingParams();
                int d2 = App.getInstance().getResources().getDimensionPixelSize(R.dimen.d2);
                roundingParams.setCornersRadii(d2, d2, 0, 0);
                mSimpleDraweeView.getHierarchy().setRoundingParams(roundingParams);
            }
        }

        public CardViewHolder(View v, int viewType) {
            super(v);
            if (viewType == TYPE_CELL) {
                // Manually bind views for TYPE_CELL viewType
                mCardView = v.findViewById(R.id.news_list_card_view);
                mTitleTv = v.findViewById(R.id.title_textView);
                mNameTv = v.findViewById(R.id.name_textView);
                mReadTv = v.findViewById(R.id.read_textView);
                mLikeTv = v.findViewById(R.id.like_textView);
                mSimpleDraweeView = v.findViewById(R.id.simpleDraweeView);
            }
        }
    }
}