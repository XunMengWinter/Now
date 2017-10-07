package top.wefor.now.data.model.entity;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by ice on 15/10/27.
 */
public class Zcool implements Serializable {
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

    public NowItem toNow() {
        NowItem nowItem = new NowItem();
        nowItem.url = this.url;
        nowItem.collectedDate = new Date().getTime();
        nowItem.imageUrl = this.imgUrl;
        nowItem.title = this.title;
        nowItem.subTitle = "by " + this.name;
        nowItem.from = "ZCOOL";
        return nowItem;
    }

}