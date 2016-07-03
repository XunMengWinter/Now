package top.wefor.now.ui.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

import com.orhanobut.logger.Logger;

/**
 * 自定义RecyclerView实现上拉加载更多
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
                /**
                 * 注意，请保证第一页填满视图，否则无法滑动导致mDy一直为0.
                 * 若将条件设为 mDy >= 0,会导致下拉刷新触发loadMore().
                 */
                if (mDy > 0 && getLayoutManager() instanceof LinearLayoutManager) {
                    LinearLayoutManager linearLayoutManager = (LinearLayoutManager) getLayoutManager();
                    int lastPos = linearLayoutManager.findLastVisibleItemPosition();
                    int itemCount = getAdapter().getItemCount();
                    if (lastPos == itemCount - 1)
                        loadMore();
                }
                break;
        }

    }

    private int mDy;
    public OnLoadMoreListener mOnLoadMoreListener;

    public interface OnLoadMoreListener {
        void loadMore();
    }

    protected void loadMore() {
        Logger.i("loadMore");
        if (mOnLoadMoreListener != null)
            mOnLoadMoreListener.loadMore();
    }

}
