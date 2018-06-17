package top.wefor.now.data.model.realm;

import io.realm.RealmObject;
import top.wefor.now.data.model.entity.TeaBean;

/**
 * Created on 2018/6/17.
 *
 * @author ice
 */
public class RealmAvatar extends RealmObject implements AbsNowRealmObject<TeaBean.AvatarBean> {
    public String raw;
    public int height;
    public int width;
    public String format;

    @Override
    public TeaBean.AvatarBean toEntity() {
        TeaBean.AvatarBean avatarBean = new TeaBean.AvatarBean();
        avatarBean.raw = raw;
        avatarBean.height = height;
        avatarBean.width = width;
        avatarBean.format = format;
        return avatarBean;
    }

    @Override
    public void setFromEntity(TeaBean.AvatarBean model) {
        raw = model.raw;
        height = model.height;
        width = model.width;
        format = model.format;
    }

    public static RealmAvatar getAvatar(TeaBean.AvatarBean avatarBean) {
        RealmAvatar realmAvatar = new RealmAvatar();
        realmAvatar.setFromEntity(avatarBean);
        return realmAvatar;
    }

}
