package top.wefor.now.data.model.realm;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import io.realm.RealmModel;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;
import top.wefor.now.data.model.entity.Zhihu;

/**
 * Created by ice on 16/4/13 15:49.
 */
public class RealmZhihu extends AbsNowRealmObject<Zhihu> implements Serializable, RealmModel {
    @PrimaryKey
    @Required
    public String pk;
    @SerializedName("images")
    public String images;
    @SerializedName("type")
    public int type;
    @SerializedName("id")
    public int id;
    @SerializedName("ga_prefix")
    public String ga_prefix;
    @SerializedName("title")
    public String title;
    @SerializedName("multipic")
    public boolean multipic;

    @Override
    public Zhihu toEntity() {
        Zhihu zhihu = new Zhihu();
        List<String> imgList = new ArrayList<>();
        imgList.add(images);
        zhihu.images = imgList;
        zhihu.type = type;
        zhihu.id = id;
        zhihu.ga_prefix = ga_prefix;
        zhihu.title = title;
        zhihu.multipic = multipic;
        return zhihu;
    }

    @Override
    public void setFromEntity(Zhihu zhihu) {
        if (zhihu.images != null && zhihu.images.size() > 0)
            images = zhihu.images.get(0);
        else
            images = "";
        type = zhihu.type;
        id = zhihu.id;
        ga_prefix = zhihu.ga_prefix;
        title = zhihu.title;
        multipic = zhihu.multipic;
        pk = id + title;
    }
}
