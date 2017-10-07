package top.wefor.now.data.model.entity;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by ice on 15/10/27.
 */
public class Moment implements Serializable {
    @SerializedName("url")
    public String url;
    @SerializedName("title")
    public String title;
    @SerializedName("imgUrls")
    public String imgUrls;
    @SerializedName("content")
    public String content;

    public NowItem toNow() {
        NowItem nowItem = new NowItem();
        nowItem.url = this.url;
        nowItem.collectedDate = new Date().getTime();

        if (this.imgUrls != null) {
            JSONArray jsonArray = JSON.parseArray(this.imgUrls);
            if (jsonArray.size() > 0)
                nowItem.imageUrl = jsonArray.getString(0);
        } else if (this.content != null)
            nowItem.subTitle = this.content;

        nowItem.title = this.title;
        nowItem.from = "Moment";
        return nowItem;
    }

}