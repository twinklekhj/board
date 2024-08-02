package io.github.twinklekhj.board.api.service;

import io.github.twinklekhj.board.api.dto.UserDto;
import io.github.twinklekhj.board.api.exception.DataNotFoundException;
import io.github.twinklekhj.board.api.param.login.LoginParam;
import io.github.twinklekhj.board.api.param.login.RegisterParam;
import io.github.twinklekhj.board.jwt.Token;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;

public interface UserService {
    ResponseEntity<Token> authenticate(LoginParam param);
    ResponseEntity<?> register(RegisterParam param);
    ResponseEntity<UserDto> findUserById(String id) throws DataNotFoundException;
}
