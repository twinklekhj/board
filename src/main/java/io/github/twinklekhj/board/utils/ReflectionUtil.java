package io.github.twinklekhj.board.utils;

import io.github.twinklekhj.board.api.dto.ApiFieldDto;
import io.github.twinklekhj.board.api.dto.ApiOperationDto;
import jakarta.validation.constraints.NotNull;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ReflectionUtil {
    /**
     * [ReflectionUtil] Request Annotation으로 ApiOperationDtoBuilder 생성 후 반환
     *
     * @param annotation Request Annotation - GET, POST, PUT, PATCH, DELETE 등
     * @return ApiOperationDtoBuilder
     */
    public static Optional<ApiOperationDto.ApiOperationDtoBuilder> parseRequestAnnotation(Annotation annotation) {
        if (annotation instanceof GetMapping getMapping) {
            return Optional.of(ApiOperationDto.builder()
                    .path(getMapping.path())
                    .method(RequestMethod.GET));
        } else if (annotation instanceof PostMapping postMapping) {
            return Optional.of(ApiOperationDto.builder()
                    .path(postMapping.path())
                    .method(RequestMethod.POST));
        } else if (annotation instanceof PutMapping putMapping) {
            return Optional.of(ApiOperationDto.builder()
                    .path(putMapping.path())
                    .method(RequestMethod.PUT));
        } else if (annotation instanceof PatchMapping patchMapping) {
            return Optional.of(ApiOperationDto.builder()
                    .path(patchMapping.path())
                    .method(RequestMethod.PATCH));
        } else if (annotation instanceof DeleteMapping deleteMapping) {
            return Optional.of(ApiOperationDto.builder()
                    .path(deleteMapping.path())
                    .method(RequestMethod.DELETE));
        } else if (annotation instanceof RequestMapping requestMapping) {
            return Optional.of(ApiOperationDto.builder()
                    .path(requestMapping.path())
                    .method(requestMapping.method()));
        }

        return Optional.empty();
    }

    /**
     * [ReflectionUtil] 각 Method 별 Parameter 목록을 통해 ApiOperationDto에 정보 주입
     *
     * @param builder ApiOperationDto 생성 Builder
     * @param parameter 각 Method 별 Parameter 목록
     */
    public static void parseParameter(ApiOperationDto.ApiOperationDtoBuilder builder, Parameter[] parameter) {
        List<ApiFieldDto> pathVariables = new ArrayList<>();

        for (Parameter p : parameter) {
            if (p.isAnnotationPresent(RequestBody.class)) {
                builder.params(parseRequestBodyToField(p.getType()));
            }
            if (p.isAnnotationPresent(AuthenticationPrincipal.class)) {
                builder.isAuthenticated(true);
            }

            if (p.isAnnotationPresent(PathVariable.class)) {
                pathVariables.add(ApiFieldDto.builder()
                        .name(p.getName())
                        .type(p.getType().getSimpleName())
                        .required(true)
                        .build());
            }
        }

        builder.pathVariables(pathVariables);
    }

    /**
     * [ReflectionUtil] Controller에서 RequestBody로 받은 파라미터를 제어 가능한 필드 형태(ApiFieldDto)의 목록으로 변환
     *
     * @param paramClass RequestBody로 받은 파라미터
     * @return 요청 가능한 필드 목록
     */
    public static List<ApiFieldDto> parseRequestBodyToField(Class<?> paramClass) {
        List<ApiFieldDto> fieldList = new ArrayList<>();
        for (Field field : paramClass.getDeclaredFields()) {
            ApiFieldDto fieldDto = new ApiFieldDto();
            fieldDto.setName(field.getName());
            fieldDto.setType(field.getType().getSimpleName());
            if (field.isAnnotationPresent(NotNull.class)) {
                fieldDto.setRequired(true);
            }
            fieldList.add(fieldDto);
        }
        return fieldList;
    }
}
