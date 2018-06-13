package top.wefor.now.ui.adapter;

import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import top.wefor.now.R;
import top.wefor.now.data.model.entity.TeaBean;
import top.wefor.now.ui.activity.BigImageActivity;

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
        if (news.thumb != null && !TextUtils.isEmpty(news.thumb.raw)) {
            Glide.with(context).load(news.thumb.raw).into(cardViewHolder.mImageView);
            cardViewHolder.mImageView.setVisibility(View.VISIBLE);
        } else {
            cardViewHolder.mImageView.setVisibility(View.GONE);
        }
        cardViewHolder.mTitleTextView.setText(news.title);
        cardViewHolder.mTitleTextView.setVisibility(news.title == null ? View.GONE : View.VISIBLE);
        cardViewHolder.mContentTextView.setText(news.description);
        cardViewHolder.mContentTextView.setVisibility(news.description == null ? View.GONE : View.VISIBLE);
    }

    public class CardViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.imageView) AppCompatImageView mImageView;
        @BindView(R.id.title_textView) TextView mTitleTextView;
        @BindView(R.id.content_textView) TextView mContentTextView;
        @BindView(R.id.name_textView) TextView mNameTextView;
        @BindView(R.id.read_textView) TextView mReadTextView;
        @BindView(R.id.like_textView) TextView mLikeTextView;
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

        @OnClick(R.id.imageView)
        void showBigImage(View v) {
            BigImageActivity.startThis(context, v, mList.get(getLayoutPosition()).thumb.raw);
        }

    }

}
