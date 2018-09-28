package top.wefor.now.data.model.entity;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Date;

import top.wefor.now.data.http.Urls;

/**
 * Created by ice on 15/10/27.
 */
public class NG implements Serializable, INow {
    @SerializedName("url")
    public String url;
    @SerializedName("title")
    public String title;
    @SerializedName("imgUrl")
    public String imgUrl;
    @SerializedName("content")
    public String content;

    public String getUrl() {
        String httpUrl = this.url + "";
        if (!httpUrl.contains("http://") && !httpUrl.contains("https://")) {
            httpUrl = Urls.NG_BASE_URL + httpUrl;
        }
        return httpUrl;
    }

    @Override
    public NowItem toNow() {
        NowItem nowItem = new NowItem();
        nowItem.url = getUrl();
        nowItem.collectedDate = new Date().getTime();
        nowItem.imageUrl = this.imgUrl;
        nowItem.title = this.title;
        nowItem.subTitle = this.content;
        nowItem.from = "NG";
        return nowItem;
    }

}