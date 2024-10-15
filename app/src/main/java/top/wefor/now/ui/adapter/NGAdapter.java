package top.wefor.now.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

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
                return new CardViewHolder(view, TYPE_HEADER);
            case TYPE_CELL:
                view = LayoutInflater.from(parent.getContext())
                        .inflate(getSmallViewResId(), parent, false);
                return new CardViewHolder(view, TYPE_CELL);
        }
        return null;
    }

    @Override
    protected void bindCellViewHolder(RecyclerView.ViewHolder cellViewHolder, int position) {
        NG news = mList.get(position);
        CardViewHolder cardViewHolder = (CardViewHolder) cellViewHolder;
        final String imageUrl = news.imgUrl;

        // Use Glide to load the image
        Glide.with(context).load(imageUrl).into(cardViewHolder.mSimpleDraweeView);

        cardViewHolder.mTitleTv.setText(news.title);
        cardViewHolder.mContentTv.setText(news.content);

        cardViewHolder.mSimpleDraweeView.setOnClickListener(v -> {
            BigImageActivity.startThis(context, v, imageUrl);
        });
    }

    public class CardViewHolder extends RecyclerView.ViewHolder {
        SimpleDraweeView mSimpleDraweeView;
        TextView mTitleTv;
        TextView mContentTv;

        public CardViewHolder(View v) {
            super(v);
            // Bind views manually
            mSimpleDraweeView = v.findViewById(R.id.simpleDraweeView);
            mTitleTv = v.findViewById(R.id.title_textView);
            mContentTv = v.findViewById(R.id.content_textView);
        }

        public CardViewHolder(View v, int viewType) {
            super(v);
            if (viewType == TYPE_CELL) {
                // Bind views manually for TYPE_CELL viewType
                mSimpleDraweeView = v.findViewById(R.id.simpleDraweeView);
                mTitleTv = v.findViewById(R.id.title_textView);
                mContentTv = v.findViewById(R.id.content_textView);
            }
        }
    }
}