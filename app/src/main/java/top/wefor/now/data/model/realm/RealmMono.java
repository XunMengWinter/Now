package top.wefor.now.data.model.realm;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import io.realm.RealmList;
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


    @SerializedName("thumb")
    public RealmAvatar thumb;
    /* 显示全部 */
    @SerializedName("pics")
    public RealmList<RealmAvatar> pics;
    /* 取第一张 */
    @SerializedName("images")
    public RealmList<RealmAvatar> images;

    @SerializedName("content")
    public String content;

    @Override
    public TeaBean.MeowBean toEntity() {
        TeaBean.MeowBean meowBean = new TeaBean.MeowBean();
        meowBean.id = pk;
        meowBean.title = title;
        meowBean.description = content;
        meowBean.rec_url = url;

        if (thumb != null) {
            meowBean.thumb = thumb.toEntity();
        }

        if (images != null) {
            meowBean.images = new ArrayList<>();
            for (RealmAvatar realmAvatar : images) {
                meowBean.images.add(realmAvatar.toEntity());
            }
        }

        if (pics != null) {
            meowBean.pics = new ArrayList<>();
            for (RealmAvatar realmAvatar : pics) {
                meowBean.pics.add(realmAvatar.toEntity());
            }
        }
        return meowBean;
    }

    @Override
    public void setFromEntity(TeaBean.MeowBean meowBean) {
        title = meowBean.title;
        content = meowBean.description;
        url = meowBean.rec_url;
        if (meowBean.thumb != null) {
            thumb = RealmAvatar.getAvatar(meowBean.thumb);
        }
        if (meowBean.images != null) {
            images = new RealmList<>();
            for (TeaBean.AvatarBean avatarBean : meowBean.images) {
                images.add(RealmAvatar.getAvatar(avatarBean));
            }
        }
        if (meowBean.pics != null) {
            pics = new RealmList<>();
            for (TeaBean.AvatarBean avatarBean : meowBean.pics) {
                pics.add(RealmAvatar.getAvatar(avatarBean));
            }
        }
        pk = meowBean.id;
    }

}
