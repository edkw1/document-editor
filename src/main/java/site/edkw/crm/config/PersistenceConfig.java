package site.edkw.crm.config;

import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import site.edkw.crm.domain.User;

import javax.persistence.EntityManagerFactory;

@Configuration
@EnableJpaAuditing(auditorAwareRef="auditorProvider")
public class PersistenceConfig {
    private final EntityManagerFactory entityManagerFactory;

    public PersistenceConfig(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    @Bean
    AuditorAware<User> auditorProvider() {
        return new SpringSecurityAuditorAware();
    }




    @Bean
    AuditReader auditReader() {
        return AuditReaderFactory.get(entityManagerFactory.createEntityManager());
    }

}
