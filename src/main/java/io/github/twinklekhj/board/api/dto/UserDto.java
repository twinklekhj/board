package io.github.twinklekhj.board.api.dto;

import io.github.twinklekhj.board.login.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class UserDto {
    private int id;
    private String memberId;
    private String email;
    private Role role;
    private boolean enabled;
    private int failureCnt;
}
