package top.wefor.now.data.model.realm;

import io.realm.RealmModel;
import io.realm.RealmObject;

/**
 * Created on 2018/3/28.
 *
 * @author ice
 * @GitHub https://github.com/XunMengWinter
 */

public abstract class AbsNowRealmObject<T> implements RealmModel {
    public abstract T toEntity();

    public abstract void setFromEntity(T model);
}
