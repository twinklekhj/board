package io.github.twinklekhj.board.login;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class CustomAuthenticationProvider implements AuthenticationProvider {
    private final BCryptPasswordEncoder encoder;
    private final MemberDetailService memberDetailService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = encoder.encode(authentication.getCredentials().toString());

        log.info("접속 아이디: {}", username);
        MemberDetails memberDetails = (MemberDetails) memberDetailService.loadUserByUsername(username);
        if (memberDetails == null) {
            throw new UsernameNotFoundException("일치하는 사용자 아이디가 없습니다.");
        }

        if (!memberDetails.isEnabled()) {
            throw new DisabledException("아직 승인되지 않은 계정입니다.<br>관리자에게 문의하시기 바랍니다.");
        } else if (!memberDetails.isAccountNonExpired()) {
            throw new LockedException("로그인 시도 가능 횟수를 초과했습니다.<br>관리자에게 문의하시기 바랍니다.");
        } else if (!this.encoder.matches(memberDetails.getPassword(), password)) {
            throw new BadCredentialsException("비밀번호가 일치하지 않습니다.");
        }

        return new UsernamePasswordAuthenticationToken(memberDetails, password, memberDetails.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
