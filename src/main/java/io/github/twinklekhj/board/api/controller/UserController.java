package io.github.twinklekhj.board.api.controller;

import io.github.twinklekhj.board.annotation.ApiMapping;
import io.github.twinklekhj.board.api.dto.UserDto;
import io.github.twinklekhj.board.api.exception.UnauthorizedException;
import io.github.twinklekhj.board.api.param.login.LoginParam;
import io.github.twinklekhj.board.api.param.login.RegisterParam;
import io.github.twinklekhj.board.api.service.user.UserService;
import io.github.twinklekhj.board.jwt.Token;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping(path = "/api/authenticate")
    @ApiMapping(order = 1, description = "[사용자 관리] 사용자 인증")
    public ResponseEntity<Token> authenticate(@Valid @RequestBody LoginParam param) {
        return userService.authenticate(param);
    }

    @PostMapping(path = "/api/register")
    @ApiMapping(order = 2, description = "[사용자 관리] 사용자 등록")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterParam param) {
        return userService.register(param);
    }

    @PostMapping(path = "/api/validate")
    @ApiMapping(order = 3, description = "[사용자 관리] 사용자 정보 획득")
    public ResponseEntity<UserDto> validate(@AuthenticationPrincipal UserDetails user) {
        if (user == null) {
            throw new UnauthorizedException();
        }
        return userService.findUserById(user.getUsername());
    }
}
