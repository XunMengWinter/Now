package top.wefor.now.data.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created on 2017/7/18.
 *
 * @author ice
 */

public class NowCollectResult extends BaseResult {
    @SerializedName("error")
    public boolean error;

    @SerializedName("error_msg")
    public String errorMsg;
}
