package ren.yuda.encryption;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "mybatis-encrypt")
public class MybatisEncryptProperties {
    private boolean enabled;
    private String defaultDesKey;

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getDefaultDesKey() {
        return defaultDesKey;
    }

    public void setDefaultDesKey(String defaultDesKey) {
        this.defaultDesKey = defaultDesKey;
    }
}
