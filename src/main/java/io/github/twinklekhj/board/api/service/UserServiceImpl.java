package io.github.twinklekhj.board.api.service;

import io.github.twinklekhj.board.api.param.login.LoginParam;
import io.github.twinklekhj.board.api.param.login.RegisterParam;
import io.github.twinklekhj.board.dao.entity.Member;
import io.github.twinklekhj.board.dao.repository.MemberRepository;
import io.github.twinklekhj.board.jwt.Token;
import io.github.twinklekhj.board.jwt.TokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final AuthenticationManager authenticationManager;
    private final MemberRepository memberRepository;
    private final TokenProvider tokenProvider;

    @Override
    public ResponseEntity<Token> authenticate(LoginParam param) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(param.getId(), param.getPassword())
            );
            Token token = tokenProvider.generateAccessToken(authentication);
            log.info("User {} logged in successfully", param.getId());
            return ResponseEntity.ok().body(token);
        } catch (AuthenticationException e) {
            throw new BadCredentialsException(e.getMessage());
        }
    }

    @Override
    public ResponseEntity<?> register(RegisterParam param) {
        Optional<Member> memberOptional = memberRepository.findByMemberId(param.getId());
        if (memberOptional.isPresent()) {
            return ResponseEntity.badRequest().body("Member already exists");
        } else {
            Member member = memberRepository.save(param.toMember());
            return ResponseEntity.created(URI.create("/api/member/" + member.getId())).build();
        }
    }

    @Override
    public ResponseEntity<?> findUserById(Long id) {
        Optional<Member> member = memberRepository.findById(id);
        if (member.isEmpty()) {
            return ResponseEntity.badRequest().body("Member is not found");
        } else {
            return ResponseEntity.ok(member.get());
        }
    }
}
