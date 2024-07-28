package io.github.twinklekhj.board.login;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum Role {
    USER("ROLE_USER", "일반 사용자", 1),
    ADMIN("ROLE_ADMIN", "관리자", 0),
    ;

    private final String name;
    private final String alias;
    private final Integer rank;
}
