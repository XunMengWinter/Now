package top.wefor.now.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import top.wefor.now.R;
import top.wefor.now.data.model.entity.NG;
import top.wefor.now.ui.activity.BigImageActivity;

/**
 * Created by ice on 15/10/26.
 */
public class NGAdapter extends BaseListAdapter<NG> {

    public NGAdapter(Context context, List<NG> contents) {
        super(context, contents);
        setBigViewResId(R.layout.item_empty_head);
        setSmallViewResId(R.layout.item_ng);
    }

    @Override
    public CardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;

        switch (viewType) {
            case TYPE_HEADER:
                view = LayoutInflater.from(parent.getContext())
                        .inflate(getBigViewResId(), parent, false);
                return new CardViewHolder(view, TYPE_HEADER) {
                };
            case TYPE_CELL:
                view = LayoutInflater.from(parent.getContext())
                        .inflate(getSmallViewResId(), parent, false);
                return new CardViewHolder(view) {
                };
        }
        return null;
    }

    @Override
    protected void bindCellViewHolder(RecyclerView.ViewHolder cellViewHolder, int position) {
        NG news = mList.get(position);
        CardViewHolder cardViewHolder = (CardViewHolder) cellViewHolder;
//        Uri imgUri = Uri.parse(news.imgUrl);
//        cardViewHolder.mSimpleDraweeView.setImageURI(imgUri);
        final String imageUrl = news.imgUrl;
        Glide.with(context).load(imageUrl).into(cardViewHolder.mSimpleDraweeView);
        cardViewHolder.mTitleTv.setText(news.title);
        cardViewHolder.mContentTv.setText(news.content);
        cardViewHolder.mSimpleDraweeView.setOnClickListener(v -> {
            BigImageActivity.startThis(context, v, imageUrl);
        });
    }

    public class CardViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.simpleDraweeView)
        SimpleDraweeView mSimpleDraweeView;
        @BindView(R.id.title_textView)
        TextView mTitleTv;
        @BindView(R.id.content_textView)
        TextView mContentTv;

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
