package top.wefor.now.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import top.wefor.now.data.model.entity.BDImg;

/**
 * Created by ice on 15/11/27.
 */
public class BDImgResult extends BaseResult {
    @SerializedName("col")
    public String col;
    @SerializedName("tag")
    public String tag;
    @SerializedName("imgs")
    public List<BDImg> imgs;
}
