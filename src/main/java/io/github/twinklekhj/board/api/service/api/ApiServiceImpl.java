package io.github.twinklekhj.board.api.service.api;

import io.github.twinklekhj.board.annotation.ApiGroup;
import io.github.twinklekhj.board.annotation.ApiOperation;
import io.github.twinklekhj.board.api.controller.BoardController;
import io.github.twinklekhj.board.api.controller.UserController;
import io.github.twinklekhj.board.api.dto.ApiGroupDto;
import io.github.twinklekhj.board.api.dto.ApiOperationDto;
import io.github.twinklekhj.board.utils.ReflectionUtil;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Service
public class ApiServiceImpl implements ApiService {
    private static Class<?>[] classes = {BoardController.class, UserController.class};

    @Override
    public List<ApiGroupDto> getApiList() {
        List<ApiGroupDto> groupList = new ArrayList<>();

        for (Class<?> controllerClass : classes) {
            Method[] methods = controllerClass.getDeclaredMethods();

            String[] basePaths = {};
            ApiGroupDto.ApiGroupDtoBuilder groupBuilder = ApiGroupDto.builder();
            if (controllerClass.isAnnotationPresent(ApiGroup.class)) {
                ApiGroup apiGroup = controllerClass.getAnnotation(ApiGroup.class);
                groupBuilder
                        .order(apiGroup.order())
                        .name(apiGroup.name());
            }

            if (controllerClass.isAnnotationPresent(RequestMapping.class)) {
                RequestMapping requestMapping = controllerClass.getAnnotation(RequestMapping.class);
                basePaths = requestMapping.path();
            }

            List<ApiOperationDto> apiList = new ArrayList<>();
            for (Method method : methods) {
                String[] finalBasePaths = basePaths;
                Stream.of(GetMapping.class, PutMapping.class, PostMapping.class, PatchMapping.class, DeleteMapping.class, RequestMapping.class)
                        .filter(method::isAnnotationPresent).forEach(annotationClass -> {
                            Optional<ApiOperationDto.ApiOperationDtoBuilder> apiInfoDto = ReflectionUtil.parseRequestAnnotation(method.getAnnotation(annotationClass));
                            if (apiInfoDto.isPresent()) {
                                ApiOperationDto.ApiOperationDtoBuilder builder = apiInfoDto.get();
                                if (method.isAnnotationPresent(ApiOperation.class)) {
                                    ApiOperation apiOperation = method.getAnnotation(ApiOperation.class);
                                    ReflectionUtil.parseParameter(builder, method.getParameters());
                                    builder.description(apiOperation.description());
                                    builder.order(apiOperation.order());
                                    ApiOperationDto apiOperationDto = builder.build();
                                    if (finalBasePaths.length > 0){
                                        apiOperationDto.productPaths(finalBasePaths);
                                    }
                                    apiList.add(apiOperationDto);
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
        return groupList;
    }
}
