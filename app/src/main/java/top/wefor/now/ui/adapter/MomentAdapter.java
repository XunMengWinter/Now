package top.wefor.now.ui.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.orhanobut.logger.Logger;
import com.squareup.picasso.Picasso;

import java.util.List;

import top.wefor.now.NowApp;
import top.wefor.now.R;
import top.wefor.now.model.entity.Moment;
import top.wefor.now.ui.BigImageActivity;
import top.wefor.now.ui.WebActivity;
import top.wefor.now.utils.NowAppUtils;

/**
 * Created by ice on 15/10/26.
 */
public class MomentAdapter extends TestRecyclerViewAdapter<Moment> {

    private Integer IMAGE_WIDTH, IMAGE_HEIGHT;

    public void setImageWidthAndHeight() {
        int width = (NowAppUtils.getWidth() - 4 * NowApp.getInstance().getResources().getDimensionPixelSize(R.dimen.d3)) / 3;
        IMAGE_WIDTH = width;
        IMAGE_HEIGHT = width * 4 / 5;
    }

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
                String imageUrl = jsonArray.getString(i);
                Uri imgUri = Uri.parse(imageUrl);
//                cardViewHolder.mImageViews[i].setImageURI(imgUri);
                //由于BigImageActivity中用到的PhotoViewAttacher与Fresco不兼容，所以使用Picasso.
                Picasso.with(context).load(imageUrl).into(cardViewHolder.mImageViews[i]);
                //click to see big image
                cardViewHolder.mImageViews[i].setTag(imageUrl);
                cardViewHolder.mImageViews[i].setOnClickListener(v -> {
                    Intent intent = new Intent(context, BigImageActivity.class);
                    intent.putExtra(BigImageActivity.IMAGE_URL, (String) v.getTag());
                    ActivityOptionsCompat optionsCompat
                            = ActivityOptionsCompat.makeSceneTransitionAnimation(
                            (Activity) context, v, BigImageActivity.TRANSIT_PIC);
                    try {
                        ActivityCompat.startActivity((Activity) context, intent,
                                optionsCompat.toBundle());
                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                        context.startActivity(intent);
                    }
                });

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
            mImageView1 = (ImageView) v.findViewById(R.id.simpleDraweeView1);
            mImageView2 = (ImageView) v.findViewById(R.id.simpleDraweeView2);
            mImageView3 = (ImageView) v.findViewById(R.id.simpleDraweeView3);
            mImageViews = new ImageView[]{mImageView1, mImageView2, mImageView3};

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(IMAGE_WIDTH, IMAGE_HEIGHT);
            mImageView1.setLayoutParams(params);
            mImageView2.setLayoutParams(params);
            mImageView3.setLayoutParams(params);

//            if (NowAppUtils.isBelowLollipop()) {
//                //set round corner
//                RoundingParams roundingParams = new RoundingParams();
//                int d2 = context.getResources().getDimensionPixelSize(R.dimen.d2);
//                roundingParams.setCornersRadii(0, 0, 0, d2);
//                mImageView1.getHierarchy().setRoundingParams(roundingParams);
//                roundingParams.setCornersRadii(0, 0, d2, 0);
//                mImageView3.getHierarchy().setRoundingParams(roundingParams);
//            }

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
