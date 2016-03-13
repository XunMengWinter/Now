package top.wefor.now.ui.adapter;

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
import top.wefor.now.http.Urls;
import top.wefor.now.model.entity.NG;
import top.wefor.now.ui.BigImageActivity;
import top.wefor.now.ui.WebActivity;

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
        super.bindCellViewHolder(cellViewHolder, position);
        NG news = contents.get(position);
        CardViewHolder cardViewHolder = (CardViewHolder) cellViewHolder;

        if (NowApplication.isWifiConnected())
            Glide.with(context).load(news.imgUrl).into(cardViewHolder.mImageView);
        else
            Glide.with(context).load(news.imgUrl).override(480, 360).into(cardViewHolder.mImageView);

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
            if (IMAGE_HEIGHT == null)
                // height = (minSide - margin)*3/4
                IMAGE_HEIGHT = (Math.min(NowApplication.getWidth(), NowApplication.getHeight()) - 2 * context.getResources().getDimensionPixelSize(R.dimen.d4)) * 3 / 4;

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

        @OnClick(R.id.view)
        void showBigImage(View v) {
            NG news = contents.get(getLayoutPosition());
            String imageUrl = news.imgUrl;

            Intent intent = new Intent(context, BigImageActivity.class);
            intent.putExtra(BigImageActivity.IMAGE_URL, imageUrl);
            context.startActivity(intent);

//            PhotoView photoView = new PhotoView(context);
//            int minSide = (int) (NowApplication.getWidth() * 0.9);
//            photoView.setLayoutParams(new ViewGroup.LayoutParams(minSide, minSide * 3 / 4));
//            Glide.with(context).load(imageUrl).into(photoView);
//            new AlertDialog.Builder(context)
//                    .setView(photoView)
//                    .create().show();
        }
    }

}
