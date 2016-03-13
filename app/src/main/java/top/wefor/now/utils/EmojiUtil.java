package top.wefor.now.utils;

/**
 * Created by ice on 2/29/16.
 */
public class EmojiUtil {
    public static String getEmojiByUnicode(int unicode){
        return new String(Character.toChars(unicode));
    }

    public static String smile(){
        int unicode = 0x1F601;
        return getEmojiByUnicode(unicode);
    }
}
