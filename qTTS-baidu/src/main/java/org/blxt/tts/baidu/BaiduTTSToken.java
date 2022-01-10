package org.blxt.tts.baidu;

/**
 * 百度tts的token对象
 * @author OpenJialei
 * @date 2021年12月18日 14:55
 */
public class BaiduTTSToken {

    /** tokeb */
    String access_token;
    /** 有效期 */
    Integer expires_in;
    String refresh_token;
    String scope;
    String session_key;
    String session_secret;

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public Integer getExpires_in() {
        return expires_in;
    }

    public void setExpires_in(Integer expires_in) {
        this.expires_in = expires_in;
    }

    public String getRefresh_token() {
        return refresh_token;
    }

    public void setRefresh_token(String refresh_token) {
        this.refresh_token = refresh_token;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getSession_key() {
        return session_key;
    }

    public void setSession_key(String session_key) {
        this.session_key = session_key;
    }

    public String getSession_secret() {
        return session_secret;
    }

    public void setSession_secret(String session_secret) {
        this.session_secret = session_secret;
    }

    @Override
    public String toString() {
        return "BaiduTTSToken{" +
            "access_token='" + access_token + '\'' +
            ", expires_in=" + expires_in +
            ", refresh_token='" + refresh_token + '\'' +
            ", scope='" + scope + '\'' +
            ", session_key='" + session_key + '\'' +
            ", session_secret='" + session_secret + '\'' +
            '}';
    }
}
