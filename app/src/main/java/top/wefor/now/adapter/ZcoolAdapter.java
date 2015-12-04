package top.wefor.now.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.orhanobut.logger.Logger;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import top.wefor.now.R;
import top.wefor.now.TestRecyclerViewAdapter;
import top.wefor.now.WebActivity;
import top.wefor.now.model.Zcool;

/**
 * Created by ice on 15/10/26.
 */
public class ZcoolAdapter extends TestRecyclerViewAdapter<Zcool> {

    public static Integer IMAGE_WIDTH = 320, IMAGE_HEIGHT = 240;


    public ZcoolAdapter(Context context, List<Zcool> contents) {
        super(context, contents);
        setBigViewResId(R.layout.item_empty_head);
        setSmallViewResId(R.layout.item_zcool);
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
        super.bindCellViewHolder(cellViewHolder, position);
        Zcool news = contents.get(position);
        Logger.d(position + "");
        CardViewHolder cardViewHolder = (CardViewHolder) cellViewHolder;
        // 图像地址（官方 API 使用数组形式，目前暂未有使用多张图片的情形出现，曾见无 images 属性的情况，请在使用中注意 ）

        Glide.with(context).load(news.imgUrl).override(IMAGE_WIDTH, IMAGE_HEIGHT).into(cardViewHolder.mImageView);
        cardViewHolder.mTitleTv.setText(news.title);
        cardViewHolder.mNameTv.setText(news.name);
        cardViewHolder.mReadTv.setText(news.readCount + " 看过");
        cardViewHolder.mLikeTv.setText(news.likeCount + " 赞");
    }

    public class CardViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.view)
        ImageView mImageView;
        @Bind(R.id.title_textView)
        TextView mTitleTv;
        @Bind(R.id.name_textView)
        TextView mNameTv;
        @Bind(R.id.read_textView)
        TextView mReadTv;
        @Bind(R.id.like_textView)
        TextView mLikeTv;

        public CardViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }

        public CardViewHolder(View v, int viewType) {
            super(v);
            if (viewType == TYPE_CELL)
                ButterKnife.bind(this, v);
        }

        @OnClick(R.id.rootView)
        void onClick(View v) {
            // TODO do what you want :) you can use WebActivity to load detail content
            Zcool news = (Zcool) contents.get(getLayoutPosition());
            Intent intent = new Intent(v.getContext(), WebActivity.class);
            intent.putExtra(WebActivity.EXTRA_TITLE, news.title);
            intent.putExtra(WebActivity.EXTRA_URL, news.url);
            intent.putExtra(WebActivity.EXTRA_PIC_URL, news.imgUrl);
            v.getContext().startActivity(intent);
        }
    }

}
