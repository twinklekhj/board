package io.github.twinklekhj.board.api.param.user;

import io.github.twinklekhj.board.dao.entity.Member;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class RegisterParam {
    @NotNull
    private String id;
    @NotNull
    private String password;
    @NotNull
    private String name;
    private String email;

    public Member toMember() {
        return Member.builder()
                .memberId(id)
                .password(password)
                .name(name)
                .email(email)
                .build();
    }
}
