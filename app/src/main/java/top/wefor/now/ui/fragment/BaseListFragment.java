package top.wefor.now.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import top.wefor.now.App;
import top.wefor.now.Constants;
import top.wefor.now.R;
import top.wefor.now.data.model.entity.NowItem;
import top.wefor.now.ui.widget.LoadMoreRecyclerView;
import top.wefor.now.utils.CommonUtils;

/**
 * Created on 15/10/28.
 * @author ice
 */
public abstract class BaseListFragment<T> extends BaseFragment implements LoadMoreRecyclerView.OnLoadMoreListener {
    protected LoadMoreRecyclerView mRecyclerView;
    protected int mPage = Constants.LIST_FIRST_PAGE;
    protected final int PAGE_SIZE = Constants.LIST_PAGE_SIZE;
    public static final int REQUEST_SAVE_NOTE = 77;


    protected RealmConfiguration mRealmConfig;
    protected Realm mRealm;
    protected List<T> mList = new ArrayList<>();


    public abstract void getData();

    public abstract void showList();

    protected int getLayoutId() {
        return R.layout.fragment_recyclerview;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        mRealmConfig = new RealmConfiguration.Builder(App.getInstance().getApplicationContext()).build();

        mRealmConfig = new RealmConfiguration.Builder().build();

        mRealm = Realm.getInstance(mRealmConfig);
        // try catch 是个坑，一定要少用！！！一不小心把以前的数据全删了！
//        try {
//        } catch (RealmMigrationNeededException e) {
//            Realm.deleteRealm(mRealmConfig);
//            mRealm = Realm.getInstance(mRealmConfig);
//        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(getLayoutId(), container, false);
        mRecyclerView = (LoadMoreRecyclerView) view.findViewById(R.id.recyclerView);
        mRecyclerView.mOnLoadMoreListener = this;
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mRealm != null)
            mRealm.close();
    }

    @Override
    public void loadMore() {
        //如果页数*分页数目大于列表长度，那么只能是列表正在加载或数据库数据见底，两种情况都不需要继续加载。
        if ((mPage - 1) * Constants.LIST_PAGE_SIZE > mList.size())
            return;
        showList();
    }


    public void saveToNote(NowItem nowItem) {
        if (CommonUtils.isAppInstalled(App.getInstance(), Constants.MyTableNote.PACKAGE_NAME)) {
            new AlertDialog.Builder(getActivity())
                    .setMessage(getString(R.string.dialog_save_to_note, nowItem.title))
                    .setPositiveButton(R.string.yes, (dialog, which) -> saveNowToNote(nowItem))
                    .setNegativeButton(R.string.no, null)
                    .create()
                    .show();
        }
    }

    private void saveNowToNote(NowItem nowItem) {
        String content = "";
        if (TextUtils.isEmpty(nowItem.subTitle)) {
            content = content + nowItem.title;
        } else {
            content = content + nowItem.subTitle;
        }
        content = content + " \n " + getString(R.string.pin_urls, nowItem.imageUrl, nowItem.url);
        Intent intent = new Intent(Constants.MyTableNote.ACTION_NAME);
        intent.putExtra(Constants.MyTableNote.TITLE, nowItem.title);
        intent.putExtra(Constants.MyTableNote.CONTENT, content);
        intent.putExtra(Constants.MyTableNote.FROM, "Now " + nowItem.from);
        intent.putExtra(Constants.MyTableNote.TYPE, Constants.MyTableNote.TYPE_NOW);
        startActivityForResult(intent, REQUEST_SAVE_NOTE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SAVE_NOTE && resultCode == Activity.RESULT_OK) {
            if (data != null
                    && data.getIntExtra(Constants.MyTableNote.RESULT_TYPE, 0)
                    == Constants.MyTableNote.RESULT_TYPE_SUCCESS) {
                App.showToast(R.string.pin_success);
            }
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
