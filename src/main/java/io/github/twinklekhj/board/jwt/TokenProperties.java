package io.github.twinklekhj.board.jwt;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("jwt")
@Getter
@Setter
public class TokenProperties {
    private String authorizeHeader = "Authorization";
    private String authorizeKey = "auth";
    private String bearerType = "Bearer";
    private Integer tokenExpireTime;
    private String secretKey;
}
