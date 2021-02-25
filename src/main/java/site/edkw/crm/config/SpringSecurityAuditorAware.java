package site.edkw.crm.config;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import site.edkw.crm.domain.User;
import site.edkw.crm.security.jwt.JwtUser;
import site.edkw.crm.service.UserService;

import java.util.Optional;

public class SpringSecurityAuditorAware implements AuditorAware<User> {
    private final UserService userService;

    public SpringSecurityAuditorAware(UserService userService) {
        this.userService = userService;
    }

    @Override
    public Optional<User> getCurrentAuditor() {
        Object obj = Optional.ofNullable(SecurityContextHolder.getContext())
                .map(SecurityContext::getAuthentication)
                .filter(Authentication::isAuthenticated)
                .map(Authentication::getPrincipal).orElse(null);
        if (obj instanceof String || obj == null) {
            return Optional.empty();
        }
        return Optional.of(userService.findById(((JwtUser) obj).getId()));
    }
}
