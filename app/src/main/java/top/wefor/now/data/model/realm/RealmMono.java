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
    @SerializedName("author")
    public String author;

    @Override
    public TeaBean.MeowBean toEntity() {
        TeaBean.MeowBean moment = new TeaBean.MeowBean();
        moment.title = title;
        moment.description = content;
        moment.rec_url = url;

        TeaBean.AvatarBean avatarBean = new TeaBean.AvatarBean();
        avatarBean.raw = imgUrls;
        moment.thumb = avatarBean;

        TeaBean.UserBean userBean = new TeaBean.UserBean();
        userBean.name = author;
        moment.user = userBean;
        return moment;
    }

    @Override
    public void setFromEntity(TeaBean.MeowBean moment) {
        title = moment.title;
        content = moment.description;
        url = moment.rec_url;
        if (moment.thumb != null)
            imgUrls = moment.thumb.raw;
        if (moment.user != null)
            author = moment.user.name;
        pk = moment.id;
    }

}
