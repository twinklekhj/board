package io.github.twinklekhj.board.api.param.user;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class LoginParam {
    @NotNull
    private String id;
    @NotNull
    private String password;
}
