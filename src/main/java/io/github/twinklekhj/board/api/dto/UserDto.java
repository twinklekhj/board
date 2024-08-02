package io.github.twinklekhj.board.api.dto;

import io.github.twinklekhj.board.login.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
public class UserDto {
    private Long id;
    private String userId;
    private String name;
    private String email;
    private List<Role> roles;

    public UserDto(Long id, String userId, String name, String email, Role role){
        this.id = id;
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.roles = Role.getRoles(role);
    }
}
