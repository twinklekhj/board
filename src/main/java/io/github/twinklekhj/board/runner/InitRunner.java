package io.github.twinklekhj.board.runner;

import io.github.twinklekhj.board.dao.entity.Member;
import io.github.twinklekhj.board.dao.repository.MemberRepository;
import io.github.twinklekhj.board.login.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class InitRunner implements ApplicationRunner {
    private final MemberRepository memberRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        Member member = Member.builder()
                .memberId("admin")
                .name("관리자")
                .password("admin")
                .email("admin@domain.com")
                .role(Role.ADMIN)
                .build();

        memberRepository.save(member);
    }
}
