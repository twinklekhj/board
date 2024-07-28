package io.github.twinklekhj.board.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@ConfigurationProperties("cors")
@Setter
@Getter
public class CorsProperties {
    private List<String> allowedOrigins;
    private List<String> allowedMethods;
}
