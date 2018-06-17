package top.wefor.now.data.model.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created on 2018/6/12.
 *
 * @author ice
 */

public class TeaBean implements Serializable {
    //    public String release_date;
    public int kind;
    //    public String share_text;
//    public String push_msg;
    public String title;
    //    public String sub_title;
//    public String bg_img_url;
//    public AvatarBean bg_img_url_thumb;
//    public String read_time;
//    public String intro;
//    public String share_image;
    public int id;
    public List<EntityListBean> entity_list;

    public static class EntityListBean {
        public MeowBean meow;
    }

    public static class MeowBean {
        //        public int bang_count;
//        public boolean is_folded;
//        public int kind;
//        public String share_text;
//        public GroupBean group;
        public AvatarBean thumb;
        public List<AvatarBean> pics;
        public List<AvatarBean> images;
        //        public String banner_img_url;
//        public boolean is_external_link;
//        public String share_img;
//        public String exposure_url;
//        public String meow_status;
//        public int object_type;
        public String id;
        //        public int comment_count;
//        public int create_time;
        public String title;
        public String description;
        public String rec_url;
//        public UserBean user;
//        public int is_filtered;
//        public int meow_type;
//        public CategoryBean category;
//        public int is_post_by_master;

        public String getCover() {
            if (thumb != null) {
                return thumb.raw;
            } else if (images != null && images.size() > 0) {
                return images.get(0).raw;
            } else if (pics != null && pics.size() > 0) {
                return pics.get(0).raw;
            }
            return null;
        }
    }

    public static class GroupBean {
        public int topic_content_num;
        public UserBean master_info;
        public boolean has_playlist;
        public int id;
        public String category;
        public AvatarBean thumb;
        public String logo_url;
        public String status;
        public String description;
        public int campaign_num;
        public int member_num;
        public int kind;
        public String slogan;
        public String name;
        public int discuss_content_num;
        public int publisher_type;
        public CertBean cert;
        public int cert_kind_id;
        public AvatarBean logo_url_thumb;
        public int category_id;
        public List<?> admin_list;
        public List<?> admin_data;
    }

    public static class CategoryBean {
        public int id;
        public String name;
    }

    public static class CertBean {
        public String app_name;
        public String weibo_url;
        public String home_page;
        public String ios_download_url;
        public int id;
        public int cert_kind_id;
        public int cert_url_type;
        public String wechat_id;
        public String ios_stats_download_url;
        public String android_download_url;
    }

    public static class UserBean {
        //        public String user_id;
        public String name;
//        public boolean is_anonymous;
//        public int horoscope;
//        public int gender;
//        public String self_description;
//        public String avatar_url;
//        public AvatarBean avatar_url_thumb;
//        public CoordinateBean coordinate;

        public static class CoordinateBean {
            public int latitude;
            public String area_name;
            public int longitude;
        }
    }

    public static class AvatarBean {
        public String raw;
        public int height;
        public int width;
        public String format;
    }
}
