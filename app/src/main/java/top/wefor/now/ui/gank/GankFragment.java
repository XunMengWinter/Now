package top.wefor.now.ui.gank;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import top.wefor.now.R;
import top.wefor.now.data.model.entity.Gank;

/**
 * Created on 16/8/4.
 *
 * @author ice
 */
public class GankFragment extends Fragment {

    @BindView(R.id.recyclerView) RecyclerView mRecyclerView;

    GankAdapter mGankAdapter;
    ArrayList<Gank> mList = new ArrayList<>();

    public static final String GANK_LIST = "gank_list";

    public static GankFragment get(ArrayList<Gank> gankList) {
        GankFragment recyclerViewFragment = new GankFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(GANK_LIST, gankList);
        recyclerViewFragment.setArguments(bundle);
        return recyclerViewFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gank, null);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        if (getArguments() != null) {
            mList.clear();
            mList.addAll(getArguments().getParcelableArrayList(GANK_LIST));
        }
        mGankAdapter = new GankAdapter(getActivity(), mList, mRecyclerView);
        mRecyclerView.setAdapter(mGankAdapter);

    }
}
