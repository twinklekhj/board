package io.github.twinklekhj.board.login;

import io.github.twinklekhj.board.api.param.login.LoginParam;
import io.github.twinklekhj.board.dao.entity.Member;
import io.github.twinklekhj.board.dao.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class CustomAuthenticationHandler {
    private final MemberRepository memberRepository;

    public void onAuthenticationSuccess(Authentication authentication) {
        Optional<Member> memberOptional = memberRepository.findByMemberId(authentication.getName());

        if (memberOptional.isPresent()) {
            Member member = memberOptional.get();
            member.setFailureCnt(0);
            member.setAccessDate(LocalDateTime.now());
            memberRepository.save(member);
        }
    }

    public int onAuthenticationFailure(LoginParam param, AuthenticationException exception) {
        if (exception instanceof BadCredentialsException) {
            Optional<Member> memberOptional = memberRepository.findByMemberId(param.getId());
            if (memberOptional.isPresent()) {
                Member member = memberOptional.get();
                member.setFailureCnt(member.getFailureCnt() + 1);
                memberRepository.save(member);

                return member.getFailureCnt();
            }
        }

        return 0;
    }
}
