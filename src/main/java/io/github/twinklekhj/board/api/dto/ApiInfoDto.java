package io.github.twinklekhj.board.api.dto;

import lombok.Builder;

@Builder
public class ApiInfoDto {
    private String name;
    private String version;
    private String description;
    private Integer port;
}
