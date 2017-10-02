package top.wefor.now.data.model.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created on 2017/7/17.
 *
 * @author ice
 */

public class NowItem {

    @SerializedName("url")
    public String url;
    @SerializedName("title")
    public String title;
    @SerializedName("image_url")
    public String imageUrl;
    @SerializedName("sub_title")
    public String subTitle;
    @SerializedName("collected_date")
    public Long collectedDate;
    @SerializedName("from")
    public String from;

}
