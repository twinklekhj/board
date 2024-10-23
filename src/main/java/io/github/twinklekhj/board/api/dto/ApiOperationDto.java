package io.github.twinklekhj.board.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;
import java.util.List;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ApiOperationDto {
    private int order;
    private String[] path;
    private String description;
    private RequestMethod[] method;
    private List<ApiFieldDto> params;
    private List<ApiFieldDto> pathVariables;

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

    public void productPaths(String[] parent) {
        String[] children = this.path;
        List<String> newPaths = new ArrayList<>();
        for (String p: parent){
            if (children != null && children.length > 0){
                for (String c: children){
                    newPaths.add(String.format("%s%s", p, c));
                }
            } else {
                newPaths.add(p);
            }
        }
        this.path = newPaths.toArray(new String[0]);
    }
}
