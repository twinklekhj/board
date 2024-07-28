package io.github.twinklekhj.board.login;

import io.github.twinklekhj.board.dao.entity.Member;
import io.github.twinklekhj.board.dao.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberDetailService implements UserDetailsService {
    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("사용자 검색: {}", username);
        Optional<Member> memberOptional = memberRepository.findByMemberId(username);
        if(!memberOptional.isPresent()) {
            throw new UsernameNotFoundException("이용자를 찾을 수 없습니다.");
        }
        return new MemberDetails(memberOptional.get());
    }
}
