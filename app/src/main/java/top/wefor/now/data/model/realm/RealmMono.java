package top.wefor.now.data.model.realm;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;
import top.wefor.now.data.model.entity.TeaBean;

/**
 * Created by ice on 16/4/13 10:56.
 */
public class RealmMono extends RealmObject implements AbsNowRealmObject<TeaBean.MeowBean> {
    @PrimaryKey
    @Required
    public String pk;
    @SerializedName("url")
    public String url;
    @SerializedName("title")
    public String title;
    @SerializedName("imgUrls")
    public String imgUrls;
    @SerializedName("content")
    public String content;

    @Override
    public TeaBean.MeowBean toEntity() {
        TeaBean.MeowBean meowBean = new TeaBean.MeowBean();
        meowBean.title = title;
        meowBean.description = content;
        meowBean.rec_url = url;

        TeaBean.AvatarBean avatarBean = new TeaBean.AvatarBean();
        avatarBean.raw = imgUrls;
        meowBean.thumb = avatarBean;

        return meowBean;
    }

    @Override
    public void setFromEntity(TeaBean.MeowBean meowBean) {
        title = meowBean.title;
        content = meowBean.description;
        url = meowBean.rec_url;
        if (meowBean.thumb != null)
            imgUrls = meowBean.thumb.raw;
        pk = meowBean.id;
    }

}
