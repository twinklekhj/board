package io.github.twinklekhj.board.login;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Getter
public enum Role {
    USER("ROLE_USER", "일반 사용자", 1),
    ADMIN("ROLE_ADMIN", "관리자", 0),
    ;

    private final String name;
    private final String alias;
    private final Integer rank;

    public static List<Role> getRoles(Role role) {
        return Arrays
                .stream(Role.values()).filter(e -> e.rank >= role.rank)
                .collect(Collectors.toList());
    }
}
