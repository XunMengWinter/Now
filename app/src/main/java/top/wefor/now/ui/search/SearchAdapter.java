package top.wefor.now.ui.search;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import top.wefor.now.data.model.entity.NowItem;
import top.wefor.now.ui.adapter.BaseRecyclerViewAdapter;

/**
 * Created on 2018/9/23.
 *
 * @author ice
 */
public class SearchAdapter extends BaseRecyclerViewAdapter<NowItem> {
    public SearchAdapter(Context context, RecyclerView recyclerView) {
        super(context, recyclerView);
    }

    @Override
    protected int getLayoutRes() {
        return 0;
    }

    @Override
    protected RecyclerView.ViewHolder getViewHolder(View view) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

    }
}
