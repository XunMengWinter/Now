package top.wefor.now.ui.search;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import top.wefor.now.R;
import top.wefor.now.data.database.RealmSearchHelper;
import top.wefor.now.data.model.entity.NowItem;
import top.wefor.now.data.model.realm.RealmMoment;
import top.wefor.now.data.model.realm.RealmMono;
import top.wefor.now.data.model.realm.RealmNG;
import top.wefor.now.data.model.realm.RealmZcool;
import top.wefor.now.data.model.realm.RealmZhihu;
import top.wefor.now.ui.BaseAppCompatActivity;
import top.wefor.now.ui.activity.WebActivity;

/**
 * Created on 2018/9/23.
 *
 * @author ice
 */
public class SearchActivity extends BaseAppCompatActivity {

    @BindView(R.id.recyclerView) RecyclerView mRecyclerView;
    SearchAdapter mSearchAdapter;
    List<NowItem> mSearchList = new ArrayList<>();
    SearchView mSearchView;
    private Realm mRealm;
    private RealmSearchHelper mRealmSearchHelper;

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
        mRealmSearchHelper = new RealmSearchHelper(mRealm);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setHasFixedSize(true);
        mSearchAdapter = new SearchAdapter(this, mSearchList, mRecyclerView);
        mSearchAdapter.setOnItemClickListener(position -> {
            NowItem item = mSearchList.get(position);
            WebActivity.startThis(this, item.url, item.title, item.imageUrl, item.from);
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        mSearchView = (SearchView) searchItem.getActionView();
        mSearchView.setIconified(false);
        mSearchView.setQueryHint("📖历史条目");
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                mSearchList.clear();
                mSearchList.addAll(mRealmSearchHelper.search(RealmZcool.class, query));
                mSearchList.addAll(mRealmSearchHelper.search(RealmNG.class, query));
                mSearchList.addAll(mRealmSearchHelper.search(RealmMono.class, query));
                mSearchList.addAll(mRealmSearchHelper.search(RealmZhihu.class, query));
                mSearchList.addAll(mRealmSearchHelper.search(RealmMoment.class, query));
                mSearchAdapter.notifyDataSetChanged();
                closeSoftKeyboard();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return true;
    }

    private void closeSoftKeyboard(){
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        // 隐藏软键盘
        if (imm != null) {
            imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mRealm != null && !mRealm.isClosed())
            mRealm.close();
    }
}
