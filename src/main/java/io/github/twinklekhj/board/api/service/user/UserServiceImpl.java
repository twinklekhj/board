package io.github.twinklekhj.board.api.service.user;

import io.github.twinklekhj.board.api.dto.UserDto;
import io.github.twinklekhj.board.api.exception.DataNotFoundException;
import io.github.twinklekhj.board.api.param.login.LoginParam;
import io.github.twinklekhj.board.api.param.login.RegisterParam;
import io.github.twinklekhj.board.exception.DataNotFoundException;
import io.github.twinklekhj.board.dao.entity.Member;
import io.github.twinklekhj.board.dao.repository.member.MemberRepository;
import io.github.twinklekhj.board.jwt.Token;
import io.github.twinklekhj.board.jwt.TokenProvider;
import io.github.twinklekhj.board.login.CustomAuthenticationHandler;
import io.github.twinklekhj.board.utils.FileUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private static final String USER_IMAGE_PATH = "/resources/upload/user";
    private final AuthenticationManager authenticationManager;
    private final MemberRepository memberRepository;
    private final TokenProvider tokenProvider;
    private final CustomAuthenticationHandler loginHandler;

    @Override
    public ResponseEntity<Token> authenticate(LoginParam param) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(param.getId(), param.getPassword())
            );
            Token token = tokenProvider.generateAccessToken(authentication);
            loginHandler.onAuthenticationSuccess(authentication);
            return ResponseEntity.ok(token);
        } catch (AuthenticationException e) {
            int failureCnt = loginHandler.onAuthenticationFailure(param, e);
            StringBuilder builder = new StringBuilder(e.getMessage());
            if (failureCnt > 0) {
                builder.append(String.format("<br>로그인 시도 횟수: %d/5", failureCnt));
                builder.append("<br>로그인 시도 횟수가 초과하면 계정이 잠기니 유의해주세요!");
            }
            throw new BadCredentialsException(builder.toString());
        }
    }

    @Override
    public ResponseEntity<?> register(RegisterParam param) {
        Optional<Member> memberOptional = memberRepository.findByMemberId(param.getId());
        if (memberOptional.isPresent()) {
            return ResponseEntity.badRequest().body("이미 존재하는 ID입니다.");
        } else {
            Member member = memberRepository.save(param.toMember());
            return ResponseEntity.created(URI.create("/api/member/" + member.getId())).build();
        }
    }

    @Override
    public ResponseEntity<UserDto> findUserById(String id) throws DataNotFoundException {
        Optional<UserDto> memberOptional = memberRepository.findById(id);
        if (memberOptional.isPresent()) {
            return ResponseEntity.ok(memberOptional.get());
        }
        throw new DataNotFoundException("이용자를 찾을 수 없습니다.");
    }

    @Override
    public ResponseEntity<?> uploadImage(String id, MultipartHttpServletRequest request, MultipartFile file) {
        Optional<Member> memberOptional = memberRepository.findByMemberId(id);
        if (memberOptional.isPresent()) {
            Member member = memberOptional.get();
            Optional<String> uploadNameOptional = FileUtil.uploadFile(USER_IMAGE_PATH, file, member.getMemberId());
            if (uploadNameOptional.isPresent()) {
                member.setImageUrl("/api/user/image?id=" + member.getMemberId());
                member.setEditDate(LocalDateTime.now());
                memberRepository.save(member);

                return ResponseEntity.ok("성공적으로 수정되었습니다.");
            }
        }
        throw new DataNotFoundException("이용자를 찾을 수 없습니다.");
    }

    @Override
    public void downloadImage(HttpServletRequest request, HttpServletResponse response, String id) {
        FileUtil.downloadFile(request, response, USER_IMAGE_PATH, id + ".png");
    }
}
