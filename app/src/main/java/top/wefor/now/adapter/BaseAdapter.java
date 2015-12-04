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
import top.wefor.now.WebActivity;
import top.wefor.now.ZhihuApi;
import top.wefor.now.model.ZhihuDaily;

/**
 * Created by tangqi on 8/20/15.
 */
public class BaseAdapter extends RecyclerView.Adapter<BaseAdapter.CardViewHolder> {
    private List<ZhihuDaily> mNewsList;
    private Context mContext;

    public BaseAdapter(Context mContext, List<ZhihuDaily> mNewsList) {
        this.mContext = mContext;
        this.mNewsList = mNewsList;
        // I hate it !!!
        // http://stackoverflow.com/questions/28787008/onbindviewholder-position-is-starting-again-at-0
//        setHasStableIds(true);
    }

    @Override
    public CardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater
                .from(mContext)
                .inflate(R.layout.item_zhihu, parent, false);
        CardViewHolder cardViewHolder = new CardViewHolder(itemView);
        return cardViewHolder;
    }

    @Override
    public void onBindViewHolder(CardViewHolder holder, int position) {
        ZhihuDaily news = mNewsList.get(position);
        Logger.d(position + "");
        // 图像地址（官方 API 使用数组形式，目前暂未有使用多张图片的情形出现，曾见无 images 属性的情况，请在使用中注意 ）
        Glide.with(mContext).load(news.images.get(0)).into(holder.mCover);
        holder.mTitle.setText(news.title);
    }

    @Override
    public int getItemCount() {
        return mNewsList.size();
    }

    public class CardViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.iv_cover) ImageView mCover;
        @Bind(R.id.tv_title) TextView mTitle;

        public CardViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }

        @OnClick(R.id.ll_card_parent)
        void onClick(View v) {
            // TODO do what you want :) you can use WebActivity to load detail content
            ZhihuDaily news = mNewsList.get(getLayoutPosition());
            String news_url = ZhihuApi.getNewsContent(news.id);
            Intent intent = new Intent(v.getContext(), WebActivity.class);
            intent.putExtra(WebActivity.EXTRA_TITLE, news.title);
            intent.putExtra(WebActivity.EXTRA_URL, news_url);
            v.getContext().startActivity(intent);
        }
    }
}
