package top.wefor.now.data.model.realm;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;
import top.wefor.now.data.model.entity.NG;

/**
 * Created by ice on 16/4/13 15:49.
 */
public class RealmNG extends RealmObject implements AbsNowRealmObject<NG> {
    @PrimaryKey
    @Required
    public String pk;
    @SerializedName("url")
    public String url;
    @SerializedName("title")
    public String title;
    @SerializedName("imgUrl")
    public String imgUrl;
    @SerializedName("content")
    public String content;

    @Override
    public NG toEntity() {
        NG ng = new NG();
        ng.url = url;
        ng.imgUrl = imgUrl;
        ng.title = title;
        ng.content = content;
        return ng;
    }

    @Override
    public void setFromEntity(NG ng) {
        url = ng.url;
        imgUrl = ng.imgUrl;
        title = ng.title;
        content = ng.content;
        pk = url + title;
    }
}
