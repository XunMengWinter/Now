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

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import top.wefor.now.NowApplication;
import top.wefor.now.R;
import top.wefor.now.TestRecyclerViewAdapter;
import top.wefor.now.Urls;
import top.wefor.now.WebActivity;
import top.wefor.now.model.NG;

/**
 * Created by ice on 15/10/26.
 */
public class NGAdapter extends TestRecyclerViewAdapter<NG> {

    public static Integer IMAGE_HEIGHT;

    public NGAdapter(Context context, List<NG> contents) {
        super(context, contents);
        setBigViewResId(R.layout.item_empty_head);
        setSmallViewResId(R.layout.item_ng);
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
        NG news = contents.get(position);
        CardViewHolder cardViewHolder = (CardViewHolder) cellViewHolder;

        Glide.with(context).load(news.imgUrl).into(cardViewHolder.mImageView);
        cardViewHolder.mTitleTv.setText(news.title);
        cardViewHolder.mContentTv.setText(news.content);

    }

    public class CardViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.view)
        ImageView mImageView;
        @Bind(R.id.title_textView)
        TextView mTitleTv;
        @Bind(R.id.content_textView)
        TextView mContentTv;

        public CardViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
            if (IMAGE_HEIGHT == null) {
                float scale = context.getResources().getDisplayMetrics().density;
                IMAGE_HEIGHT = (int) (NowApplication.getWidth() - 16 * 2 * scale) * 3 / 4;
            }
            ViewGroup.LayoutParams layoutParams = mImageView.getLayoutParams();
            layoutParams.height = IMAGE_HEIGHT;
            mImageView.setLayoutParams(layoutParams);
        }

        public CardViewHolder(View v, int viewType) {
            super(v);
            if (viewType == TYPE_CELL)
                ButterKnife.bind(this, v);

        }

        @OnClick(R.id.rootView)
        void onClick(View v) {
            // TODO do what you want :) you can use WebActivity to load detail content
            NG news = contents.get(getLayoutPosition());
            Intent intent = new Intent(v.getContext(), WebActivity.class);
            intent.putExtra(WebActivity.EXTRA_TITLE, news.title);
            intent.putExtra(WebActivity.EXTRA_URL, Urls.NG_BASE_URL + news.url);
            intent.putExtra(WebActivity.EXTRA_PIC_URL, news.imgUrl);
            v.getContext().startActivity(intent);
        }
    }

}
