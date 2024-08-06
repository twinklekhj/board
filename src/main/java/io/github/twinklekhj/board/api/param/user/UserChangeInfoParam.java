package io.github.twinklekhj.board.api.param.user;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UserChangeInfoParam {
    private String name;
    private String email;
}
