package top.wefor.now.ui;

import androidx.annotation.DrawableRes;
import androidx.annotation.StringRes;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import top.wefor.now.ui.adapter.BaseRecyclerViewAdapter;

/**
 * Created on 16/6/21 09:38.
 *
 * @author ice, GitHub: https://github.com/XunMengWinter
 */
public interface IRecyclerViewPresenter<T> {

    @DrawableRes
    int getEmptyImageRes();

    @StringRes
    int getEmptyTextRes();

    BaseRecyclerViewAdapter<T> getAdapter(ArrayList<T> list, RecyclerView recyclerView);

    void putDataToList(ArrayList<T> list);

    void loadMore();

    void refresh();
}
