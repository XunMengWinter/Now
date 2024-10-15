package top.wefor.now.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSONArray;

import java.util.List;

import top.wefor.now.Constants;
import top.wefor.now.data.model.entity.TeaBean;
import top.wefor.now.databinding.ActivityImageListBinding;
import top.wefor.now.ui.BaseSwipeBackCompatActivity;
import top.wefor.now.ui.adapter.MonoImageAdapter;

/**
 * Mono多图list。
 * Created on 2018/6/17.
 *
 * @author ice
 */
public class MonoImageListActivity extends BaseSwipeBackCompatActivity {

    public static Intent getIntent(@NonNull Context context, @NonNull List<TeaBean.AvatarBean> images) {
        String imagesStr = JSONArray.toJSONString(images);
        Intent intent = new Intent(context, MonoImageListActivity.class);
        intent.putExtra(Constants.KEY_IMAGES_STR, imagesStr);
        return intent;
    }

    private MonoImageAdapter mMonoImageAdapter;
    private List<TeaBean.AvatarBean> mList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityImageListBinding binding = ActivityImageListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        String imagesStr = getIntent().getStringExtra(Constants.KEY_IMAGES_STR);
        mList = JSONArray.parseArray(imagesStr, TeaBean.AvatarBean.class);

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mMonoImageAdapter = new MonoImageAdapter(this, mList);
        binding.recyclerView.setAdapter(mMonoImageAdapter);
    }
}
