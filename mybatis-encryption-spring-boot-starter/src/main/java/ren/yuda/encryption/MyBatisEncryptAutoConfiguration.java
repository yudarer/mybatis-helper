package ren.yuda.encryption;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

import java.util.List;

@Configuration
@ConditionalOnProperty(prefix = "mybatis-encrypt", name = "enabled", havingValue = "true", matchIfMissing = true)
@EnableConfigurationProperties(MybatisEncryptProperties.class)
public class MyBatisEncryptAutoConfiguration {
    private final List<EncryptAlgorithm> algorithms;

    @Bean
    @ConditionalOnMissingBean(EncryptionPlugin.class)
    @Lazy
    public EncryptionPlugin encryptionPlugin() {
        algorithms.forEach(algorithm -> AlgorithmRegister.getInstance().registerAlgorithm(algorithm));
        return new EncryptionPlugin();
    }

    @Autowired
    public MyBatisEncryptAutoConfiguration(List<EncryptAlgorithm> algorithms) {
        this.algorithms = algorithms;
    }
}
