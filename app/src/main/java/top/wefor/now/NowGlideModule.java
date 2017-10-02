package top.wefor.now;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.module.AppGlideModule;
import com.bumptech.glide.request.target.ViewTarget;
import com.orhanobut.logger.Logger;

/**
 * Created on 2017/10/3.
 *
 * @author ice
 */

@GlideModule
public class NowGlideModule extends AppGlideModule {

    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
        ViewTarget.setTagId(R.id.id_glide_tag);
        super.applyOptions(context, builder);
        Logger.i("applyOptions setTagId");
    }
}
