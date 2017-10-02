package top.wefor.now.data.model.entity;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by ice on 15/10/27.
 */
public class NG implements Serializable {
    @SerializedName("url")
    public String url;
    @SerializedName("title")
    public String title;
    @SerializedName("imgUrl")
    public String imgUrl;
    @SerializedName("content")
    public String content;

    public NowItem ToNow() {
        NowItem nowItem = new NowItem();
        nowItem.url = this.url;
        nowItem.collectedDate = new Date().getTime();
        nowItem.imageUrl = this.imgUrl;
        nowItem.title = this.title;
        nowItem.subTitle = this.content;
        nowItem.from = "NG";
        return nowItem;
    }

}