package top.wefor.now.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import top.wefor.now.model.entity.Zhihu;

/**
 * Created by tangqi on 8/20/15.
 */
public class ZhihuDailyResult extends BaseResult {
    @SerializedName("date")
    public String date;
    @SerializedName("stories")
    public List<Zhihu> stories;
}
