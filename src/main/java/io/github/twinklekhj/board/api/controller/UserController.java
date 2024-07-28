package io.github.twinklekhj.board.api.controller;

import io.github.twinklekhj.board.api.param.login.LoginParam;
import io.github.twinklekhj.board.api.param.login.RegisterParam;
import io.github.twinklekhj.board.api.service.UserService;
import io.github.twinklekhj.board.jwt.Token;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/api/authenticate")
    public ResponseEntity<Token> authenticate(@Valid @RequestBody LoginParam param) {
        return userService.authenticate(param);
    }

    @PostMapping("/api/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterParam param) {
        return userService.register(param);
    }

    @PostMapping("/api/user/{id}")
    public ResponseEntity<?> findUserById(@PathVariable Long id) {
        return userService.findUserById(id);
    }

    @PostMapping("/api/validate")
    public ResponseEntity<?> validate(@AuthenticationPrincipal UserDetails user) {
        if(user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        return ResponseEntity.ok().build();
    }
}
