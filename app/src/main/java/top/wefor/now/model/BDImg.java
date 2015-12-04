package top.wefor.now.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ice on 15/11/27.
 */
public class BDImg {
    @SerializedName("fromPageTitle")
    public String fromPageTitle;
    @SerializedName("column")
    public String column;
    @SerializedName("date")
    public String date;
    @SerializedName("imageUrl")
    public String imageUrl;
    @SerializedName("imageWidth")
    public String imageWidth;
    @SerializedName("imageHeight")
    public String imageHeight;
    @SerializedName("thumbnailUrl")
    public String thumbnailUrl;
    @SerializedName("thumbnailWidth")
    public String thumbnailWidth;
    @SerializedName("thumbnailHeight")
    public String thumbnailHeight;
}
