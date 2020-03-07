package top.cyblogs.data;

import lombok.Data;
import lombok.experimental.Accessors;
import top.cyblogs.util.JsonSerializeUtils;

/**
 * 登录状态保存
 *
 * @author CY
 */
@Data
@Accessors(chain = true)
public class LoginSession {

    /**
     * 用来做LoginSession的缓存
     */
    private static LoginSession loginSession;

    /**
     * BiliBili
     */
    private String bilibiliSession;

    /**
     * Imooc
     */
    private String imoocSession;

    private LoginSession() {
    }

    public static LoginSession getSession() {
        if (loginSession != null) {
            return loginSession;
        }
        loginSession = JsonSerializeUtils.deserialize(PathData.SESSION_FILE_PATH, LoginSession.class);
        if (loginSession == null) {
            return new LoginSession();
        }
        return loginSession;
    }

    public void saveSession() {
        JsonSerializeUtils.serialize(this, PathData.SESSION_FILE_PATH);
    }
}
