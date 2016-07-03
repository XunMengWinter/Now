package top.wefor.now.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import top.wefor.now.data.model.entity.GankMeizhi;

/**
 * Created on 16/7/4.
 *
 * @author ice
 */
public class GankMeizhiResult extends BaseResult {


    @SerializedName("count")
    public int count;

    @SerializedName("error")
    public boolean error;

    @SerializedName("results")
    public List<GankMeizhi> results;

}
