package top.wefor.now.data.model.realm;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import top.wefor.now.data.model.entity.Zcool;

/**
 * Created by ice on 16/4/13 10:56.
 */
public class RealmZcool extends RealmObject implements Serializable {

    @SerializedName("pk")
    public String pk;

    @PrimaryKey
    @SerializedName("url")
    public String url;

    @SerializedName("imgUrl")
    public String imgUrl;

    @SerializedName("title")
    public String title;

    @SerializedName("name")
    public String name;

    @SerializedName("readCount")
    public String readCount;

    @SerializedName("likeCount")
    public String likeCount;

    public Zcool toEntity() {
        Zcool zcool = new Zcool();
        zcool.url = url;
        zcool.imgUrl = imgUrl;
        zcool.title = title;
        zcool.name = name;
        zcool.readCount = readCount;
        zcool.likeCount = likeCount;
        return zcool;
    }

    public void setFromEntity(Zcool zcool) {
        url = zcool.url;
        imgUrl = zcool.imgUrl;
        title = zcool.title;
        name = zcool.name;
        readCount = zcool.readCount;
        likeCount = zcool.likeCount;
        pk = getPk(zcool);
    }

    public static String getPk(Zcool zcool) {
        return zcool.url + zcool.title;
    }

}
