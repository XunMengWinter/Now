package top.wefor.now.data.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created on 2018/5/5.
 *
 * @author ice
 */
public class DribbbleTokenResult {
    @SerializedName("access_token")
    private String token;

    @SerializedName("token_type")
    private String tokenType;

    @SerializedName("created_at")
    private Long createdAt;
}
