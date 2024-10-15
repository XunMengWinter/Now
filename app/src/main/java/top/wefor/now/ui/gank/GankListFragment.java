package top.wefor.now.ui.gank;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.ArrayList;

import top.wefor.now.data.model.entity.Gank;
import top.wefor.now.databinding.FragmentListGankBinding;
import top.wefor.now.ui.fragment.BaseFragment;

/**
 * Created on 16/8/4.
 * Updated for modern Android practices.
 *
 * @author ice
 */
public class GankListFragment extends BaseFragment {

    private FragmentListGankBinding binding;
    private GankAdapter mGankAdapter;
    private ArrayList<Gank> mList = new ArrayList<>();

    public static final String GANK_LIST = "gank_list";

    public static GankListFragment get(ArrayList<Gank> gankList) {
        GankListFragment recyclerViewFragment = new GankListFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(GANK_LIST, gankList);
        recyclerViewFragment.setArguments(bundle);
        return recyclerViewFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Use ViewBinding instead of ButterKnife
        binding = FragmentListGankBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Set up RecyclerView
        binding.recyclerView.setHasFixedSize(false);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        if (getArguments() != null) {
            mList.clear();
            mList.addAll(getArguments().<Gank>getParcelableArrayList(GANK_LIST));
        }

        mGankAdapter = new GankAdapter(getActivity(), mList, binding.recyclerView);
        binding.recyclerView.setAdapter(mGankAdapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null; // Avoid memory leaks
    }
}