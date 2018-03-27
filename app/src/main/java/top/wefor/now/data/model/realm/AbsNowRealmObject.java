package top.wefor.now.data.model.realm;

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
