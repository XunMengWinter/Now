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
        Zhihu news = mList.get(position);
        CardViewHolder cardViewHolder = (CardViewHolder) cellViewHolder;
        // 图像地址（官方 API 使用数组形式，目前暂未有使用多张图片的情形出现，曾见无 images 属性的情况，请在使用中注意 ）
//        Uri imgUri = Uri.parse(news.images.get(0));
//        cardViewHolder.mSimpleDraweeView.setImageURI(imgUri);
        if (news.images != null && news.images.size() > 0) {
            String imageUrl = news.images.get(0);
            Glide.with(context).load(imageUrl).into(cardViewHolder.mSimpleDraweeView);
            cardViewHolder.mSimpleDraweeView.setOnClickListener(v -> {
                BigImageActivity.startThis(context, v, imageUrl);
            });
        }
        cardViewHolder.mTitle.setText(news.title);
    }

    public class CardViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.simpleDraweeView)
        SimpleDraweeView mSimpleDraweeView;
        @BindView(R.id.tv_title)
        TextView mTitle;

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
