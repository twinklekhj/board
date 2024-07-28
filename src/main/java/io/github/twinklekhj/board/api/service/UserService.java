package io.github.twinklekhj.board.api.service;

import io.github.twinklekhj.board.api.param.login.LoginParam;
import io.github.twinklekhj.board.api.param.login.RegisterParam;
import io.github.twinklekhj.board.jwt.Token;
import org.springframework.http.ResponseEntity;

public interface UserService {
    ResponseEntity<Token> authenticate(LoginParam param);
    ResponseEntity<?> register(RegisterParam param);
    ResponseEntity<?> findUserById(Long id);
}
