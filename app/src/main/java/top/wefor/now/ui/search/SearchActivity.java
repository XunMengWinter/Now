package top.wefor.now.ui.search;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import top.wefor.now.R;
import top.wefor.now.data.database.RealmDbHelper;
import top.wefor.now.data.model.entity.Zcool;
import top.wefor.now.data.model.realm.RealmZcool;
import top.wefor.now.ui.BaseAppCompatActivity;
import top.wefor.now.ui.activity.WebActivity;
import top.wefor.now.ui.adapter.ZcoolAdapter;

/**
 * Created on 2018/9/23.
 *
 * @author ice
 */
public class SearchActivity extends BaseAppCompatActivity {

    @BindView(R.id.recyclerView) RecyclerView mRecyclerView;
    ZcoolAdapter mZcoolAdapter;
    List<Zcool> mSearchList = new ArrayList<>();
    SearchView mSearchView;
    RealmDbHelper<Zcool, RealmZcool> mZcoolRealmDbHelper;
    protected Realm mRealm;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_search;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder()
                .schemaVersion(2)
                .deleteRealmIfMigrationNeeded()
                .build();
        mRealm = Realm.getInstance(realmConfiguration);
        mZcoolRealmDbHelper = new RealmDbHelper<>(mSearchList, mRealm, RealmZcool.class);

        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        mRecyclerView.setHasFixedSize(true);
        mZcoolAdapter = new ZcoolAdapter(this, mSearchList);
        mZcoolAdapter.setHeadViewCount(0);
        mZcoolAdapter.setImageWidthAndHeight(2);
        mRecyclerView.setAdapter(mZcoolAdapter);

        mZcoolAdapter.setOnItemClickListener(model -> {
            WebActivity.startThis(this, model.url, model.title, model.imgUrl,
                    getString(R.string.share_summary_zcool));
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        mSearchView = (SearchView) searchItem.getActionView();
        mSearchView.setIconified(false);
        mSearchView.setQueryHint("input now item keyword");
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                mZcoolRealmDbHelper.search(query);
                mZcoolAdapter.notifyDataSetChanged();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mRealm != null && !mRealm.isClosed())
            mRealm.close();
    }
}
