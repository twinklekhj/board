package io.github.twinklekhj.board.api.service.api;

import io.github.twinklekhj.board.annotation.ApiOperation;
import io.github.twinklekhj.board.annotation.ApiGroup;
import io.github.twinklekhj.board.api.controller.BoardController;
import io.github.twinklekhj.board.api.controller.UserController;
import io.github.twinklekhj.board.api.dto.ApiGroupDto;
import io.github.twinklekhj.board.api.dto.ApiOperationDto;
import io.github.twinklekhj.board.utils.ReflectionUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Stream;

@Service
public class ApiServiceImpl implements ApiService {
    private static Class<?>[] classes = {BoardController.class, UserController.class};

    @Override
    public ResponseEntity<List<ApiGroupDto>> getApiList() {
        List<ApiGroupDto> groupList = new ArrayList<>();

        for (Class<?> controllerClass : classes) {
            Method[] methods = controllerClass.getDeclaredMethods();

            ApiGroupDto.ApiGroupDtoBuilder groupBuilder = ApiGroupDto.builder();
            if (controllerClass.isAnnotationPresent(ApiGroup.class)) {
                ApiGroup apiGroup = controllerClass.getAnnotation(ApiGroup.class);
                groupBuilder
                        .order(apiGroup.order())
                        .name(apiGroup.name());
            }

            List<ApiOperationDto> apiList = new ArrayList<>();
            for (Method method : methods) {
                Stream.of(GetMapping.class, PutMapping.class, PostMapping.class, PatchMapping.class, DeleteMapping.class, RequestMapping.class)
                        .filter(method::isAnnotationPresent).forEach(annotationClass -> {
                            Optional<ApiOperationDto.ApiOperationDtoBuilder> apiInfoDto = ReflectionUtil.parseRequestAnnotation(method, method.getAnnotation(annotationClass));
                            if (apiInfoDto.isPresent()) {
                                ApiOperationDto.ApiOperationDtoBuilder builder = apiInfoDto.get();
                                if (method.isAnnotationPresent(ApiOperation.class)) {
                                    ApiOperation apiOperation = method.getAnnotation(ApiOperation.class);
                                    ReflectionUtil.parseParameter(method.getParameters(), builder);
                                    builder.description(apiOperation.description());
                                    builder.order(apiOperation.order());
                                    apiList.add(builder.build());
                                }
                            }
                        });
            }

            apiList.sort(Comparator.comparingInt(ApiOperationDto::getOrder));
            groupBuilder
                    .className(controllerClass.getSimpleName())
                    .operations(apiList);

            groupList.add(groupBuilder.build());
        }

        groupList.sort(Comparator.comparingInt(ApiGroupDto::getOrder));

        return ResponseEntity.ok(groupList);
    }
}
