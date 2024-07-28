package io.github.twinklekhj.board.jwt;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Token {
    private String grantType;
    private String accessToken;
    private Long tokenExpiresIn;
}
