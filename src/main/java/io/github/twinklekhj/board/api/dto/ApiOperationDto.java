package io.github.twinklekhj.board.api.dto;

import lombok.*;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@Builder
@Getter
@AllArgsConstructor
public class ApiOperationDto {
    private int order;
    private String[] path;
    private String description;
    private RequestMethod[] method;
    private List<ApiFieldDto> params;
    private List<String> pathVariables;

    @Builder.Default
    private boolean isAuthenticated = false;

    public static <T> ApiOperationDtoBuilder builder(){
        return new ApiOperationDtoBuilder();
    }

    public static class ApiOperationDtoBuilder{
        public ApiOperationDtoBuilder method(RequestMethod... method){
            this.method = method;
            return this;
        }
    }
}
