package top.wefor.now.ui.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by ice on 15/12/10.
 */
public abstract class BaseRecyclerViewAdapter<T> extends RecyclerView.Adapter {
    protected Context context;
    protected LayoutInflater inflater;
    protected List<T> mList;
    protected RecyclerView mRecyclerView;
    protected View mRootView;
    protected OnAdapterItemClickListener<T> mItemClickListener;

    public BaseRecyclerViewAdapter(Context context, RecyclerView recyclerView) {
        recyclerView.setAdapter(this);
        mRecyclerView = recyclerView;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    public BaseRecyclerViewAdapter(Context context, List<T> list) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        mList = list;
    }

    public BaseRecyclerViewAdapter(Context context, List<T> list, RecyclerView recyclerView) {
        recyclerView.setAdapter(this);

        mRecyclerView = recyclerView;
        this.context = context;
        inflater = LayoutInflater.from(context);
        mList = list;

//        setAnimation();
    }

    protected abstract int getLayoutRes();
    protected abstract RecyclerView.ViewHolder getViewHolder(View view);

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mRootView = inflater.inflate(getLayoutRes(), parent, false);
        return getViewHolder(mRootView);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void setList(List<T> list) {
        mList = list;
        this.notifyDataSetChanged();
    }

    public List<T> getList() {
        return mList;
    }

    public void setOnItemClickListener(OnAdapterItemClickListener<T> onAdapterItemClickListener) {
        mItemClickListener = onAdapterItemClickListener;
    }

    public void update(List<T> newList) {
        mList.clear();
        mList.addAll(newList);
        this.notifyDataSetChanged();
    }

    protected String pass(String anyStr) {
        if (anyStr == null)
            anyStr = "";
        return anyStr;
    }

    public interface OnAdapterItemClickListener<T> {
        void onRecyclerViewItemClick(int position);
    }

}
