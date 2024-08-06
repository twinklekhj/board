package io.github.twinklekhj.board.api.controller;

import io.github.twinklekhj.board.annotation.ApiMapping;
import io.github.twinklekhj.board.api.dto.UserDto;
import io.github.twinklekhj.board.exception.UnauthorizedException;
import io.github.twinklekhj.board.api.param.user.LoginParam;
import io.github.twinklekhj.board.api.param.user.RegisterParam;
import io.github.twinklekhj.board.api.param.user.UserChangeInfoParam;
import io.github.twinklekhj.board.api.service.user.UserService;
import io.github.twinklekhj.board.jwt.Token;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

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

    @PatchMapping(path = "/api/user/image")
    public ResponseEntity<?> uploadImage(MultipartHttpServletRequest request, @RequestParam("file") MultipartFile file, @AuthenticationPrincipal UserDetails user) {
        return userService.uploadImage(user.getUsername(), request, file);
    }

    @PatchMapping(path = "/api/user/info")
    public ResponseEntity<?> patchName(@AuthenticationPrincipal UserDetails user, @RequestBody UserChangeInfoParam param) {
        return userService.changeInfo(user.getUsername(), param);
    }

    @GetMapping(path = "/api/user/image")
    public void uploadImage(HttpServletRequest request, HttpServletResponse response, @RequestParam(name = "id") String id) {
        userService.downloadImage(request, response, id);
    }
}
