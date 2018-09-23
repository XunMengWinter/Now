package top.wefor.now.data.database;

import android.support.annotation.NonNull;

import com.orhanobut.logger.Logger;

import java.util.List;

import io.realm.Case;
import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmResults;
import top.wefor.now.data.model.realm.AbsNowRealmObject;

/**
 * 用与Realm数据库的的
 * Created on 2018/3/28.
 *
 * @author ice
 * @GitHub https://github.com/XunMengWinter
 */
public class RealmDbHelper<M, T extends RealmObject> {

    private List<M> mEntityList;
    private Realm mRealm;

    private Class<T> mNowRealmClass;

    public RealmDbHelper(@NonNull List<M> entityList, @NonNull Realm realm, @NonNull Class<T> nowRealmClass) {
        mEntityList = entityList;
        mRealm = realm;
        mNowRealmClass = nowRealmClass;
    }

    public void saveToDatabase() {
        if (mEntityList.size() > 0) {
            try {
                mRealm.beginTransaction();
                for (int i = mEntityList.size() - 1; i >= 0; i--) {
                    if (mEntityList.get(i) != null) {
                        T realmMoment = mNowRealmClass.newInstance();
                        ((AbsNowRealmObject) realmMoment).setFromEntity(mEntityList.get(i));
                        mRealm.copyToRealmOrUpdate(realmMoment);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                Logger.i("data save err " + e.getMessage() + " " + mNowRealmClass.getSimpleName());
            } finally {
                mRealm.commitTransaction();
            }
        }
    }

    public void getFromDatabase() {
        mEntityList.clear();

        RealmResults<T> zcoolRealmResults = mRealm.where(mNowRealmClass).findAll();
        for (int i = zcoolRealmResults.size() - 1; i >= 0; i--) {
            mEntityList.add(((AbsNowRealmObject<M>) (zcoolRealmResults.get(i))).toEntity());
        }
    }

    /**
     * @param pageSize 分页大小.
     * @param page     第几页，从1开始.
     */
    public void getFromDatabase(int pageSize, int page) {
        RealmResults<T> zcoolRealmResults = mRealm.where(mNowRealmClass).findAll();
//        Logger.i("data size " + zcoolRealmResults.size() + " " + mNowRealmClass.getSimpleName());
        //反序取数据,即 startPos > endPos.
        int startPos = Math.max(zcoolRealmResults.size() - 1 - (page - 1) * pageSize, 0);
        int endPos = Math.max(startPos - pageSize, 0);

        if (page == 1 && zcoolRealmResults.size() > 0) mEntityList.clear();

        for (int i = startPos; i > endPos; i--) {
            mEntityList.add(((AbsNowRealmObject<M>) (zcoolRealmResults.get(i))).toEntity());
        }
    }

    public void search(String keyword) {
        RealmResults<T> realmResults = mRealm.where(mNowRealmClass)
                .contains("title", keyword, Case.INSENSITIVE).findAll();
        mEntityList.clear();
        for (int i = realmResults.size() - 1; i >= 0; i--) {
            mEntityList.add(((AbsNowRealmObject<M>) (realmResults.get(i))).toEntity());
        }
    }

}
