package top.wefor.now.ui.search;

import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

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
import top.wefor.now.databinding.ActivitySearchBinding;
import top.wefor.now.ui.BaseAppCompatActivity;
import top.wefor.now.ui.activity.WebActivity;

/**
 * Created on 2018/9/23.
 * Updated for modern Android practices.
 *
 * @author ice
 */
public class SearchActivity extends BaseAppCompatActivity {

    private ActivitySearchBinding binding;
    private SearchAdapter mSearchAdapter;
    private List<NowItem> mSearchList = new ArrayList<>();
    private SearchView mSearchView;
    private Realm mRealm;
    private RealmSearchHelper mRealmSearchHelper;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_search;
    }

    @Override
    protected void initViews(@Nullable Bundle savedInstanceState) {
        // Initialize ViewBinding
        binding = ActivitySearchBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Realm configuration
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder()
                .schemaVersion(2)
                .deleteRealmIfMigrationNeeded()
                .build();
        mRealm = Realm.getInstance(realmConfiguration);
        mRealmSearchHelper = new RealmSearchHelper(mRealm);

        // Set up RecyclerView
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.setHasFixedSize(true);
        mSearchAdapter = new SearchAdapter(this, mSearchList, binding.recyclerView);
        mSearchAdapter.setOnItemClickListener(position -> {
            NowItem item = mSearchList.get(position);
            WebActivity.startThis(this, item.url, item.title, item.imageUrl, item.from);
        });
        binding.recyclerView.setAdapter(mSearchAdapter);
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
                // Clear the search list and update with new results
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

    // Method to close the soft keyboard
    private void closeSoftKeyboard(){
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mRealm != null && !mRealm.isClosed()) {
            mRealm.close();
        }
        binding = null; // Avoid memory leaks
    }
}