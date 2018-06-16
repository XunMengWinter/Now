package top.wefor.now.data.database;

import android.support.annotation.NonNull;

import com.orhanobut.logger.Logger;

import java.util.List;

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
        if (mEntityList.size() > 0)
            for (int i = mEntityList.size() - 1; i >= 0; i--) {
                mRealm.beginTransaction();
                try {
                    T realmMoment = mNowRealmClass.newInstance();
                    ((AbsNowRealmObject) realmMoment).setFromEntity(mEntityList.get(i));
                    mRealm.copyToRealmOrUpdate(realmMoment);
                    mRealm.commitTransaction();
                } catch (InstantiationException | IllegalAccessException e) {
                    e.printStackTrace();
                    Logger.i("data save err " + e.getMessage() + " " + mNowRealmClass.getSimpleName());
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
        if (page == 1) mEntityList.clear();
        RealmResults<T> zcoolRealmResults = mRealm.where(mNowRealmClass).findAll();
        Logger.i("data size " + zcoolRealmResults.size() + " " + mNowRealmClass.getSimpleName());
        //反序取数据,即 startPos > endPos.
        int startPos = Math.max(zcoolRealmResults.size() - 1 - (page - 1) * pageSize, 0);
        int endPos = Math.max(startPos - pageSize, 0);

        for (int i = startPos; i > endPos; i--) {
            mEntityList.add(((AbsNowRealmObject<M>) (zcoolRealmResults.get(i))).toEntity());
        }
    }

}
