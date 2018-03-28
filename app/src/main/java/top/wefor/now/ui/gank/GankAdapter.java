package top.wefor.now.ui.gank;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import top.wefor.now.R;
import top.wefor.now.data.model.entity.Gank;
import top.wefor.now.ui.adapter.BaseRecyclerViewAdapter;

/**
 * Created on 16/7/7.
 *
 * @author ice
 */
public class GankAdapter extends BaseRecyclerViewAdapter<Gank> {
    public GankAdapter(Context context, List<Gank> list, RecyclerView recyclerView) {
        super(context, list, recyclerView);
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.item_gank;
    }

    @Override
    protected RecyclerView.ViewHolder getViewHolder(View view) {
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        MyViewHolder myViewHolder = (MyViewHolder) holder;
        Gank gank = mList.get(position);
        myViewHolder.mNameTv.setText(gank.desc);
        myViewHolder.mUrlTv.setText(gank.url);
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.name_tv) TextView mNameTv;
        @BindView(R.id.url_tv) TextView mUrlTv;

        MyViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
