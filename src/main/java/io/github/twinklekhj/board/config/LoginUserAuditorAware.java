package io.github.twinklekhj.board.config;

import io.github.twinklekhj.board.dao.entity.Member;
import io.github.twinklekhj.board.login.MemberDetails;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class LoginUserAuditorAware implements AuditorAware<Long> {

    @Override
    public Optional<Long> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            // 로그인이 안된 경우
            return Optional.of(999999L);
        }

        Object principal = authentication.getPrincipal();
        if (principal instanceof MemberDetails) {
            Long id = ((MemberDetails) principal).getId();
            return Optional.of(id);
        }
        
        // 로그인 했지만 가져올 수 없는 경우
        return Optional.of(888888L);
    }
}
