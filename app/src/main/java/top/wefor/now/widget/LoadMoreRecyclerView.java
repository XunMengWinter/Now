package top.wefor.now.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

import com.orhanobut.logger.Logger;

import top.wefor.now.ui.interactor.OnListRefreshListener;
import top.wefor.now.ui.interactor.OnLoadMoreListener;

/**
 * 自定义RecyclerView实现下拉刷新与上拉加载更多
 * 注意，未定义进度条.
 * DIY RecyclerView,dragging up to load more and dragging down to refresh list.
 * Created by ice on 16/4/15 09:56.
 */
public class LoadMoreRecyclerView extends RecyclerView {

    public LoadMoreRecyclerView(Context context) {
        super(context);
    }

    public LoadMoreRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public LoadMoreRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void onScrolled(int dx, int dy) {
        super.onScrolled(dx, dy);
//        Logger.i(dy + " onScrolled " + dx);
        mDy = dy;
    }

    @Override
    public void onScrollStateChanged(int state) {
        super.onScrollStateChanged(state);

        switch (state) {
            case RecyclerView.SCROLL_STATE_DRAGGING:
                //正在滑动，手未放开
//                        Logger.i("SCROLL_STATE_DRAGGING");
                break;
            case RecyclerView.SCROLL_STATE_IDLE:
                //手放开
//                        Logger.i("SCROLL_STATE_IDLE");
                break;
            case RecyclerView.SCROLL_STATE_SETTLING:
                //检验发现SCROLL_STATE_SETTLING为见底往上拉或顶部往上拉状态
                Logger.i("SCROLL_STATE_SETTLING");
                //注意，请保证第一页填满视图，否则无法滑动导致mDy一直为0.
                if (mDy > 0)
                    loadMore();
                else
                    refresh();
                break;
        }


    }

    private int mDy;
    public OnLoadMoreListener mLoadMoreListener;
    public OnListRefreshListener mListRefreshListener;

    protected void loadMore() {
        Logger.i("loadMore");
        if (mLoadMoreListener != null)
            mLoadMoreListener.loadMore();
    }

    protected void refresh() {
        Logger.i("refresh");
        if (mListRefreshListener != null)
            mListRefreshListener.onListRefresh();
    }

}
