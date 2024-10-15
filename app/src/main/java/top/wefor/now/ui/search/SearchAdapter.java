package top.wefor.now.ui.search;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import top.wefor.now.R;
import top.wefor.now.data.model.entity.NowItem;
import top.wefor.now.databinding.ItemSearchBinding;
import top.wefor.now.ui.activity.BigImageActivity;
import top.wefor.now.ui.adapter.BaseRecyclerViewAdapter;

/**
 * Created on 2018/9/23.
 * Updated for modern Android practices.
 *
 * @author ice
 */
public class SearchAdapter extends BaseRecyclerViewAdapter<NowItem> {

    public SearchAdapter(Context context, List<NowItem> list, RecyclerView recyclerView) {
        super(context, list, recyclerView);
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.item_search;
    }

    @NonNull
    @Override
    protected RecyclerView.ViewHolder getViewHolder(View view) {
        // Use ViewBinding to create the ViewHolder
        ItemSearchBinding binding = ItemSearchBinding.bind(view);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        NowItem item = mList.get(position);
        MyViewHolder myViewHolder = (MyViewHolder) holder;

        // Bind data to the views
        myViewHolder.binding.titleTv.setText(item.title);
        myViewHolder.binding.fromTv.setText(item.from);

        final String imageUrl = item.imageUrl;
        Glide.with(context)
                .load(imageUrl)
                .into(myViewHolder.binding.imageIv);

        myViewHolder.binding.imageIv.setOnClickListener(v -> BigImageActivity.startThis(context, v, imageUrl));
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        ItemSearchBinding binding;

        MyViewHolder(ItemSearchBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}