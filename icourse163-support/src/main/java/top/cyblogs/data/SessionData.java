package top.cyblogs.data;

import cn.hutool.core.util.StrUtil;

public class SessionData {

    public static String cookie = "";

    public static String csrfKey() {
        return StrUtil.subBetween(cookie, "NTESSTUDYSI=", ";");
    }
}
