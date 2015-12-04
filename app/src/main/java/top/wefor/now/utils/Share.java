package top.wefor.now.utils;

import android.app.Activity;
import android.graphics.Bitmap;
import android.widget.Toast;

import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

/**
 * Created by ice on 15/10/30.
 */
public class Share {

    // 最小支持的版本
    private static final int MIN_SUPPORTED_VERSION = 0x21020001;

    public static void shareToWechatcircle(Activity context, String title, String content, String url, Bitmap thumbnailMap) {
        IWXAPI api = WXAPIFactory.createWXAPI(context, Constants.WECHAT_APP_ID, true);
        api.registerApp(Constants.WECHAT_APP_ID);
        if (!api.isWXAppInstalled()) {
            Toast.makeText(context, "抱歉，您尚未安装微信客户端，无法进行微信分享！", Toast.LENGTH_SHORT).show();
            return;
        }
        if (api.getWXAppSupportAPI() < MIN_SUPPORTED_VERSION) {
            Toast.makeText(context, "抱歉，您的微信版本不支持分享到朋友圈！", Toast.LENGTH_SHORT).show();
            return;
        }
        WXWebpageObject webpage = new WXWebpageObject();
        if (webpage != null)
            webpage.webpageUrl = url;

        WXMediaMessage msg = new WXMediaMessage(webpage);
        if (title != null)
            msg.title = title;
        if (content != null)
            msg.description = content;
        if (thumbnailMap != null)
            msg.setThumbImage(thumbnailMap);

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = String.valueOf(System.currentTimeMillis());
        req.message = msg;
        req.scene = SendMessageToWX.Req.WXSceneTimeline;
        api.sendReq(req);
    }


    public static void shareToWechat(Activity context, String title, String content, String url, Bitmap thumbnailMap) {
        IWXAPI api = WXAPIFactory.createWXAPI(context, Constants.WECHAT_APP_ID, true);
        api.registerApp(Constants.WECHAT_APP_ID);
        if (!api.isWXAppInstalled()) {
            Toast.makeText(context, "抱歉，您尚未安装微信客户端，无法进行微信分享！", Toast.LENGTH_SHORT).show();
            return;
        }
        WXWebpageObject webpage = new WXWebpageObject();
        if (webpage != null)
            webpage.webpageUrl = url;

        WXMediaMessage msg = new WXMediaMessage(webpage);
        if (title != null)
            msg.title = title;
        if (content != null)
            msg.description = content;
        if (thumbnailMap != null)
            msg.setThumbImage(thumbnailMap);

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = String.valueOf(System.currentTimeMillis());
        req.message = msg;
        req.scene = SendMessageToWX.Req.WXSceneSession;
        api.sendReq(req);
    }


}
