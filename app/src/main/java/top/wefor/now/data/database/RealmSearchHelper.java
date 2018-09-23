package top.wefor.now.data.database;

import android.support.annotation.NonNull;

import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

import io.realm.Case;
import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmResults;
import top.wefor.now.data.model.entity.INow;
import top.wefor.now.data.model.entity.NowItem;
import top.wefor.now.data.model.realm.AbsNowRealmObject;

/**
 * Created on 2018/9/24.
 *
 * @author ice
 */
public class RealmSearchHelper {

    private Realm mRealm;

    public RealmSearchHelper(Realm realm) {
        mRealm = realm;
    }

    public List<NowItem> search(@NonNull Class<? extends RealmObject> clazz, String keyword) {
        RealmResults realmResults = mRealm.where(clazz)
                .contains("title", keyword, Case.INSENSITIVE).findAll();
        List<NowItem> list = new ArrayList<>();
        try {
            for (int i = realmResults.size() - 1; i >= 0; i--) {
                list.add(((AbsNowRealmObject<INow>) (realmResults.get(i))).toEntity().toNow());
            }
        } catch (Exception e) {
            e.printStackTrace();
            Logger.e(clazz.getSimpleName() + " not implement INow");
        }
        return list;
    }

}
