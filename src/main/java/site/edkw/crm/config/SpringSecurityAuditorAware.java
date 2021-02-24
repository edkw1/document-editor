package site.edkw.crm.config;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import site.edkw.crm.domain.User;

import java.util.Optional;

public class SpringSecurityAuditorAware implements AuditorAware<User> {

    @Override
    public Optional<User> getCurrentAuditor() {
        Object obj = Optional.ofNullable(SecurityContextHolder.getContext())
                .map(SecurityContext::getAuthentication)
                .filter(Authentication::isAuthenticated)
                .map(Authentication::getPrincipal).orElse(null);
        if (obj instanceof String || obj == null) {
            return Optional.empty();
        }
        return Optional.of((User) obj);
    }
}
