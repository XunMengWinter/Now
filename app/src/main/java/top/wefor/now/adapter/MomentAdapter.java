package top.wefor.now.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.bumptech.glide.Glide;
import com.orhanobut.logger.Logger;

import java.util.List;

import top.wefor.now.NowApplication;
import top.wefor.now.R;
import top.wefor.now.TestRecyclerViewAdapter;
import top.wefor.now.WebActivity;
import top.wefor.now.model.Moment;

/**
 * Created by ice on 15/10/26.
 */
public class MomentAdapter extends TestRecyclerViewAdapter<Moment> {

    public static Integer IMAGE_WIDTH, IMAGE_HEIGHT;

    public MomentAdapter(Context context, List<Moment> contents) {
        super(context, contents);
        setBigViewResId(R.layout.item_empty_head);
        setSmallViewResId(R.layout.item_moment);
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
        Moment news = contents.get(position);
        Logger.d(position + "");
        CardViewHolder cardViewHolder = (CardViewHolder) cellViewHolder;

        if (news.imgUrls != null) {
            cardViewHolder.mTextLayout.setVisibility(View.GONE);
            cardViewHolder.mImageLayout.setVisibility(View.VISIBLE);
            cardViewHolder.mImageTitleTv.setText("" + news.title);
            JSONArray jsonArray = JSON.parseArray(news.imgUrls);
            for (int i = 0; i < jsonArray.size(); i++) {
                Glide.with(context).load(jsonArray.getString(i)).override(IMAGE_WIDTH, IMAGE_HEIGHT).into(cardViewHolder.mImageViews[i]);
            }
        } else if (news.content != null) {
            cardViewHolder.mImageLayout.setVisibility(View.GONE);
            cardViewHolder.mTextLayout.setVisibility(View.VISIBLE);
            cardViewHolder.mTextTitleTv.setText("" + news.title);
            cardViewHolder.mTextContentTv.setText("" + news.content);
        }

    }

    public class CardViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        LinearLayout mImageLayout;
        TextView mImageTitleTv;
        ImageView[] mImageViews;
        ImageView mImageView1, mImageView2, mImageView3;

        LinearLayout mTextLayout;
        TextView mTextTitleTv;
        TextView mTextContentTv;

        public CardViewHolder(View v) {
            super(v);
            mImageLayout = (LinearLayout) v.findViewById(R.id.image_linearLayout);
            mImageTitleTv = (TextView) v.findViewById(R.id.image_title_textView);
            mImageView1 = (ImageView) v.findViewById(R.id.imageView1);
            mImageView2 = (ImageView) v.findViewById(R.id.imageView2);
            mImageView3 = (ImageView) v.findViewById(R.id.imageView3);
            mImageViews = new ImageView[]{mImageView1, mImageView2, mImageView3};

            if (IMAGE_WIDTH == null) {
                float scale = context.getResources().getDisplayMetrics().density;
                int width = (int) (NowApplication.getWidth() - 4 * 8 * scale) / 3;
                IMAGE_WIDTH = IMAGE_HEIGHT = width;
            }
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(IMAGE_WIDTH, IMAGE_HEIGHT);
            mImageView1.setLayoutParams(params);
            mImageView2.setLayoutParams(params);
            mImageView3.setLayoutParams(params);

            mTextLayout = (LinearLayout) v.findViewById(R.id.text_linearLayout);
            mTextTitleTv = (TextView) v.findViewById(R.id.text_title_textView);
            mTextContentTv = (TextView) v.findViewById(R.id.text_content_textView);

            v.setOnClickListener(this);
        }

        public CardViewHolder(View v, int viewType) {
            super(v);
            v.setOnClickListener(this);
            if (viewType == TYPE_CELL)
                new CardViewHolder(v);
        }

        @Override
        public void onClick(View v) {

            // TODO do what you want :) you can use WebActivity to load detail content
            Moment news = (Moment) contents.get(getLayoutPosition());
            Intent intent = new Intent(v.getContext(), WebActivity.class);
            intent.putExtra(WebActivity.EXTRA_TITLE, news.title);
            intent.putExtra(WebActivity.EXTRA_URL, news.url);
            if (news.imgUrls != null) {
                JSONArray jsonArray = JSON.parseArray(news.imgUrls);
                if (jsonArray.size() > 0)
                    intent.putExtra(WebActivity.EXTRA_PIC_URL, jsonArray.getString(0));
            } else if (news.content != null)
                intent.putExtra(WebActivity.EXTRA_SUMMARY, news.content);
            v.getContext().startActivity(intent);
        }
    }

}
