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
import top.wefor.now.data.model.entity.Zhihu;
import top.wefor.now.ui.activity.BigImageActivity;

/**
 * Created by ice on 15/10/26.
 */
public class ZhihuAdapter extends BaseListAdapter<Zhihu> {

    public ZhihuAdapter(Context context, List<Zhihu> contents) {
        super(context, contents);
        setBigViewResId(R.layout.item_empty_head);
        setSmallViewResId(R.layout.item_zhihu);
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
        Zhihu news = mList.get(position);
        CardViewHolder cardViewHolder = (CardViewHolder) cellViewHolder;

        // Load image if available
        if (news.images != null && !news.images.isEmpty()) {
            String imageUrl = news.images.get(0);
            Glide.with(context).load(imageUrl).into(cardViewHolder.mSimpleDraweeView);
            cardViewHolder.mSimpleDraweeView.setOnClickListener(v -> {
                BigImageActivity.startThis(context, v, imageUrl);
            });
        }

        // Set title
        cardViewHolder.mTitle.setText(news.title);
    }

    public static class CardViewHolder extends RecyclerView.ViewHolder {
        SimpleDraweeView mSimpleDraweeView;
        TextView mTitle;

        public CardViewHolder(View v) {
            super(v);
            // Manually bind views
            mSimpleDraweeView = v.findViewById(R.id.simpleDraweeView);
            mTitle = v.findViewById(R.id.tv_title);
        }

        public CardViewHolder(View v, int viewType) {
            super(v);
            if (viewType == TYPE_CELL) {
                // Manually bind views for TYPE_CELL viewType
                mSimpleDraweeView = v.findViewById(R.id.simpleDraweeView);
                mTitle = v.findViewById(R.id.tv_title);
            }
        }
    }
}