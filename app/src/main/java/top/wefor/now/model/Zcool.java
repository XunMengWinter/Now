package top.wefor.now.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

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
}