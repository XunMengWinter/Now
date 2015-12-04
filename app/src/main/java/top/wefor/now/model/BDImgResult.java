package top.wefor.now.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by ice on 15/11/27.
 */
public class BDImgResult {
    @SerializedName("col")
    public String col;
    @SerializedName("tag")
    public String tag;
    @SerializedName("imgs")
    public List<BDImg> imgs;
}
