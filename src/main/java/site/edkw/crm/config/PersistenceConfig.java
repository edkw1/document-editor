package site.edkw.crm.config;

import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import site.edkw.crm.domain.User;
import site.edkw.crm.service.UserService;

import javax.persistence.EntityManagerFactory;

@Configuration
@EnableJpaAuditing(auditorAwareRef="auditorProvider")
public class PersistenceConfig {
    private final EntityManagerFactory entityManagerFactory;
    private final UserService userService;

    public PersistenceConfig(EntityManagerFactory entityManagerFactory, UserService userService) {
        this.entityManagerFactory = entityManagerFactory;
        this.userService = userService;
    }

    @Bean
    AuditorAware<User> auditorProvider() {
        return new SpringSecurityAuditorAware(userService);
    }




    @Bean
    AuditReader auditReader() {
        return AuditReaderFactory.get(entityManagerFactory.createEntityManager());
    }

}
