package top.wefor.now.data.model.realm;

import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;
import top.wefor.now.data.model.entity.TeaBean;

/**
 * Created by ice on 16/4/13 10:56.
 */
public class RealmMono extends RealmObject implements AbsNowRealmObject<TeaBean.MeowBean> {
    private static final String separate = ",,,";

    @PrimaryKey
    @Required
    public String pk;
    @SerializedName("url")
    public String url;
    @SerializedName("title")
    public String title;

    /* 显示全部 */
    @SerializedName("pics")
    public String pics;
    /* 取第一张 */
    @SerializedName("images")
    public String images;

    @SerializedName("content")
    public String content;

    @Override
    public TeaBean.MeowBean toEntity() {
        TeaBean.MeowBean meowBean = new TeaBean.MeowBean();
        meowBean.title = title;
        meowBean.description = content;
        meowBean.rec_url = url;

        if (!TextUtils.isEmpty(images)) {
            String[] imageList = images.split(separate);
            if (imageList.length <= 1) {
                meowBean.thumb = getAvatarBean(imageList[0]);
            } else {
                meowBean.images = new ArrayList<>();
                for (String raw : imageList) {
                    meowBean.images.add(getAvatarBean(raw));
                }
            }
        } else if (!TextUtils.isEmpty(pics)) {
            String[] picList = pics.split(separate);
            meowBean.pics = new ArrayList<>();
            for (String raw : picList) {
                meowBean.pics.add(getAvatarBean(raw));
            }
        }

        return meowBean;
    }

    private TeaBean.AvatarBean getAvatarBean(String raw) {
        TeaBean.AvatarBean avatarBean = new TeaBean.AvatarBean();
        avatarBean.raw = raw;
        return avatarBean;
    }

    @Override
    public void setFromEntity(TeaBean.MeowBean meowBean) {
        title = meowBean.title;
        content = meowBean.description;
        url = meowBean.rec_url;
        if (meowBean.thumb != null) {
            images = meowBean.thumb.raw;
        } else if (meowBean.images != null) {
            StringBuilder stringBuilder = new StringBuilder();
            for (TeaBean.AvatarBean avatarBean : meowBean.images) {
                stringBuilder.append(avatarBean.raw).append(separate);
            }
            images = stringBuilder.toString();
        } else if (meowBean.pics != null) {
            StringBuilder stringBuilder = new StringBuilder();
            for (TeaBean.AvatarBean avatarBean : meowBean.pics) {
                stringBuilder.append(avatarBean.raw).append(separate);
            }
            pics = stringBuilder.toString();
        }
        pk = meowBean.id;
    }

}
