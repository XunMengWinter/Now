package top.wefor.now.data.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import top.wefor.now.data.model.entity.NowItem;

/**
 * Created on 2017/7/18.
 *
 * @author ice
 */

public class NowCollectRequest implements Serializable {

    @SerializedName("token_tree_hole_name")
    public String treeHoleName;

    @SerializedName("now_item")
    public NowItem nowItem;

}
