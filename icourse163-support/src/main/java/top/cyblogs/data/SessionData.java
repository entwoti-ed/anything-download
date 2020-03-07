package top.cyblogs.data;

import cn.hutool.core.util.StrUtil;

public class SessionData {

    public static final String cookie = "";

    public static final String csrfKey = StrUtil.subBetween(cookie, "NTESSTUDYSI=", ";");
}
