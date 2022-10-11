package com.hau.ketnguyen.it.config.auth;

import com.hau.ketnguyen.it.common.constant.Constants;
import com.hau.ketnguyen.it.entity.auth.CustomUser;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Objects;
import java.util.Optional;

public class AuditAwareImpl implements AuditorAware<String> {
    @Override
    public Optional<String> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (Objects.isNull(authentication)) {
            return Optional.of(Constants.ANONYMOUS);
        }
        Object principal = authentication.getPrincipal();
        if (Objects.isNull(principal)) {
            return Optional.of(Constants.ANONYMOUS);
        }
        if (principal instanceof CustomUser) {
            return Optional.of(((CustomUser) principal).getUsername());
        }
        return Optional.of(Constants.ANONYMOUS);
    }
}
