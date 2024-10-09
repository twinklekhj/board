package io.github.twinklekhj.board.utils;

import io.github.twinklekhj.board.api.dto.ApiFieldDto;
import io.github.twinklekhj.board.api.dto.ApiOperationDto;
import jakarta.validation.constraints.NotNull;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ReflectionUtil {
    public static void parseParameter(Parameter[] parameter, ApiOperationDto.ApiOperationDtoBuilder builder) {
        List<String> pathVariables = new ArrayList<>();

        for (Parameter p : parameter) {
            if (p.isAnnotationPresent(RequestBody.class)) {
                builder.params(parseParamToField(p.getType()));
            }
            if (p.isAnnotationPresent(AuthenticationPrincipal.class)) {
                builder.isAuthenticated(true);
            }

            if (p.isAnnotationPresent(PathVariable.class)) {
                pathVariables.add(p.getClass().getSimpleName());
            }
        }

        builder.pathVariables(pathVariables);
    }

    public static List<ApiFieldDto> parseParamToField(Class<?> paramClass) {
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

    public static Optional<ApiOperationDto.ApiOperationDtoBuilder> parseRequestAnnotation(Method method, Annotation annotation) {
        if (annotation instanceof GetMapping) {
            GetMapping getMapping = (GetMapping) annotation;
            return Optional.of(ApiOperationDto.builder()
                    .path(getMapping.path())
                    .method(RequestMethod.GET));
        } else if (annotation instanceof PutMapping) {
            PutMapping putMapping = (PutMapping) annotation;
            return Optional.of(ApiOperationDto.builder()
                    .path(putMapping.path())
                    .method(RequestMethod.PUT));
        } else if (annotation instanceof PostMapping) {
            PostMapping postMapping = (PostMapping) annotation;
            return Optional.of(ApiOperationDto.builder()
                    .path(postMapping.path())
                    .method(RequestMethod.POST));
        } else if (annotation instanceof PatchMapping) {
            PatchMapping patchMapping = (PatchMapping) annotation;
            return Optional.of(ApiOperationDto.builder()
                    .path(patchMapping.path())
                    .method(RequestMethod.PATCH));
        } else if (annotation instanceof DeleteMapping) {
            DeleteMapping deleteMapping = (DeleteMapping) annotation;
            return Optional.of(ApiOperationDto.builder()
                    .path(deleteMapping.path())
                    .method(RequestMethod.DELETE));
        } else if (annotation instanceof RequestMapping) {
            RequestMapping requestMapping = (RequestMapping) annotation;
            return Optional.of(ApiOperationDto.builder()
                    .path(requestMapping.path())
                    .method(requestMapping.method()));
        }

        return Optional.empty();
    }
}
