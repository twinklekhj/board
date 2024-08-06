package io.github.twinklekhj.board.api.service.user;

import io.github.twinklekhj.board.api.dto.UserDto;
import io.github.twinklekhj.board.exception.DataNotFoundException;
import io.github.twinklekhj.board.api.param.user.LoginParam;
import io.github.twinklekhj.board.api.param.user.RegisterParam;
import io.github.twinklekhj.board.api.param.user.UserChangeInfoParam;
import io.github.twinklekhj.board.jwt.Token;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

public interface UserService {
    ResponseEntity<Token> authenticate(LoginParam param);
    ResponseEntity<?> register(RegisterParam param);
    ResponseEntity<UserDto> findUserById(String id) throws DataNotFoundException;
    ResponseEntity<?> uploadImage(String id, MultipartHttpServletRequest request, MultipartFile file);
    void downloadImage(HttpServletRequest request, HttpServletResponse response, String id);

    ResponseEntity<?> changeInfo(String username, UserChangeInfoParam param);
}
