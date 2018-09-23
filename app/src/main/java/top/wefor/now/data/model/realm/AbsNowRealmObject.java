package top.wefor.now.data.model.realm;

import top.wefor.now.data.model.entity.INow;

/**
 * Created on 2018/3/28.
 *
 * @author ice
 * @GitHub https://github.com/XunMengWinter
 */

public interface AbsNowRealmObject<T> {
    T toEntity();

    void setFromEntity(T model);
}
