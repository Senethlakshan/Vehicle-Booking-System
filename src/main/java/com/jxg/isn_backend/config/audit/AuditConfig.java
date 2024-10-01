package com.jxg.isn_backend.config.audit;

import com.jxg.isn_backend.model.User;
import com.jxg.isn_backend.security.config.SecurityAuditorAwareImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EnableJpaAuditing
public class AuditConfig {

    @Bean
    public AuditorAware<User> auditorProvider() {
        return new SecurityAuditorAwareImpl();
    }

}
