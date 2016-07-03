package top.wefor.now.ui.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.view.SimpleDraweeView;
import com.orhanobut.logger.Logger;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import top.wefor.now.App;
import top.wefor.now.R;
import top.wefor.now.data.model.entity.Moment;
import top.wefor.now.ui.BigImageActivity;
import top.wefor.now.ui.WebActivity;
import top.wefor.now.utils.NowAppUtils;

/**
 * Created by ice on 15/10/26.
 */
public class MomentAdapter extends BaseRecyclerViewAdapter<Moment> {

    private Integer IMAGE_WIDTH, IMAGE_HEIGHT;

    public void setImageWidthAndHeight() {
        int width = (NowAppUtils.getWidth() - 4 * App.getInstance().getResources().getDimensionPixelSize(R.dimen.d3)) / 3;
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
        Moment news = mList.get(position);
        Logger.d(position + "");
        CardViewHolder cardViewHolder = (CardViewHolder) cellViewHolder;

        if (news.imgUrls != null) {
            cardViewHolder.mTextLinearLayout.setVisibility(View.GONE);
            cardViewHolder.mImageLinearLayout.setVisibility(View.VISIBLE);
            cardViewHolder.mImageTitleTextView.setText("" + news.title);
            JSONArray jsonArray = JSON.parseArray(news.imgUrls);
            for (int i = 0; i < jsonArray.size(); i++) {
                String imageUrl = jsonArray.getString(i);
//                cardViewHolder.mImageViews[i].setImageURI(imgUri);
                //由于BigImageActivity中用到的PhotoViewAttacher与Fresco不兼容，所以使用Picasso.
                Picasso.with(context).load(imageUrl).into(cardViewHolder.mImageViews[i]);
                //click to see big image
                cardViewHolder.mImageViews[i].setTag(imageUrl);
                cardViewHolder.mImageViews[i].setOnClickListener(v -> {
                    BigImageActivity.startThis(context, v, (String) v.getTag());
                });
            }
        } else if (news.content != null) {
            cardViewHolder.mImageLinearLayout.setVisibility(View.GONE);
            cardViewHolder.mTextLinearLayout.setVisibility(View.VISIBLE);
            cardViewHolder.mTextTitleTextView.setText("" + news.title);
            cardViewHolder.mTextContentTextView.setText("" + news.content);
        }

    }

    public class CardViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        SimpleDraweeView[] mImageViews;

        @BindView(R.id.image_title_textView) TextView mImageTitleTextView;
        @BindView(R.id.simpleDraweeView1) SimpleDraweeView mSimpleDraweeView1;
        @BindView(R.id.simpleDraweeView2) SimpleDraweeView mSimpleDraweeView2;
        @BindView(R.id.simpleDraweeView3) SimpleDraweeView mSimpleDraweeView3;
        @BindView(R.id.image_linearLayout) LinearLayout mImageLinearLayout;
        @BindView(R.id.text_title_textView) TextView mTextTitleTextView;
        @BindView(R.id.text_content_textView) TextView mTextContentTextView;
        @BindView(R.id.text_linearLayout) LinearLayout mTextLinearLayout;
        @BindView(R.id.news_list_card_view) CardView mNewsListCardView;

        public CardViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);

            mImageViews = new SimpleDraweeView[]{mSimpleDraweeView1, mSimpleDraweeView2, mSimpleDraweeView3};

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(IMAGE_WIDTH, IMAGE_HEIGHT);
            mSimpleDraweeView1.setLayoutParams(params);
            mSimpleDraweeView2.setLayoutParams(params);
            mSimpleDraweeView3.setLayoutParams(params);

            // because us Picasso loadView,so this is not work.
            if (NowAppUtils.isBelowLollipop()) {
                //set round corner
                RoundingParams roundingParams = new RoundingParams();
                int d2 = context.getResources().getDimensionPixelSize(R.dimen.d2);
                roundingParams.setCornersRadii(0, 0, 0, d2);
                mSimpleDraweeView1.getHierarchy().setRoundingParams(roundingParams);
                roundingParams.setCornersRadii(0, 0, d2, 0);
                mSimpleDraweeView3.getHierarchy().setRoundingParams(roundingParams);
            }

            v.setOnClickListener(this);
        }

        public CardViewHolder(View v, int viewType) {
            super(v);
            if (viewType == TYPE_CELL) {
                ButterKnife.bind(this, v);
                v.setOnClickListener(this);
            }
        }

        @Override
        public void onClick(View v) {
            // TODO do what you want :) you can use WebActivity to load detail content
            Moment news = mList.get(getLayoutPosition());
            String summary = context.getString(R.string.share_summary_moment);
            String imageUrl = null;
            if (news.imgUrls != null) {
                JSONArray jsonArray = JSON.parseArray(news.imgUrls);
                if (jsonArray.size() > 0)
                    imageUrl = jsonArray.getString(0);
            } else if (news.content != null)
                summary = news.content;
            WebActivity.startThis(context, news.url, news.title, imageUrl, summary);
        }
    }

}
