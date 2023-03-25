package ren.yuda.encryption;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ren.yuda.encryption.support.DESEncryptAlgorithm;

@Configuration
@EnableConfigurationProperties(MybatisEncryptProperties.class)
@ConditionalOnProperty(prefix = "mybatis-encrypt", name = "enabled", havingValue = "true", matchIfMissing = true)
public class DefaultEncryptConfiguration {
    @Bean
    @ConditionalOnMissingBean(EncryptAlgorithm.class)
    public EncryptAlgorithm defaultEncryptAlgorithm(MybatisEncryptProperties properties) {
        return new DESEncryptAlgorithm(properties.getDefaultDesKey());
    }
}
