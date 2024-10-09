package io.github.twinklekhj.board.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
@AllArgsConstructor
public class ApiGroupDto {
    private int order;
    private String className;
    private String name;
    private List<ApiOperationDto> operations;
}
