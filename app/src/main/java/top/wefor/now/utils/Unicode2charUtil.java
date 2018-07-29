package top.wefor.now.utils;/*
 * 这里给出了两个静态方法，可以直接用类名调用，尽量调用第一个decode2(String) 这个函数
 */
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author OldKing
 */
public class Unicode2charUtil {

    /**
     * 修改字符串中的unicode码
     *
     * @param s 源str
     * @return 修改后的str
     */
    public static String decode2(String s) {
        StringBuilder sb = new StringBuilder(s.length());
        char[] chars = s.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            char c = chars[i];
            if (c == '\\' && chars[i + 1] == 'u') {
                char cc = 0;
                for (int j = 0; j < 4; j++) {
                    char ch = Character.toLowerCase(chars[i + 2 + j]);
                    if ('0' <= ch && ch <= '9' || 'a' <= ch && ch <= 'f') {
                        cc |= (Character.digit(ch, 16) << (3 - j) * 4);
                    } else {
                        cc = 0;
                        break;
                    }
                }
                if (cc > 0) {
                    i += 5;
                    sb.append(cc);
                    continue;
                }
            }
            sb.append(c);
        }
        return sb.toString();
    }

    /**
     * 获取修复后的字符串
     *
     * @param str
     * @return
     */
    public static String getFixStr(String str) {
        String ret = str;
        Pattern p = Pattern.compile("(\\\\u.{4})");
        Matcher m = p.matcher(ret);
        while (m.find()) {
            String xxx = m.group(0);
            ret = str.replaceAll(String.format("\\%s", xxx), decode2(xxx));
        }
        return ret;
    }
}

