package top.wefor.now.model.entity;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

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

}