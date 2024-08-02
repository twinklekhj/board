package io.github.twinklekhj.board.api.service.api;

import io.github.twinklekhj.board.annotation.ApiMapping;
import io.github.twinklekhj.board.api.controller.ApiController;
import io.github.twinklekhj.board.api.controller.BoardController;
import io.github.twinklekhj.board.api.controller.UserController;
import io.github.twinklekhj.board.api.dto.ApiInfoDto;
import io.github.twinklekhj.board.utils.ReflectionUtil;
import org.springframework.http.ResponseEntity;
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
    private static Class<?>[] classes = {ApiController.class, BoardController.class, UserController.class};

    @Override
    public ResponseEntity<List<ApiInfoDto>> getApiList() {
        List<ApiInfoDto> apiList = new ArrayList<>();

        for (Class<?> controllerClass : classes) {
            Method[] methods = controllerClass.getDeclaredMethods();

            for (Method method : methods) {
                Stream.of(GetMapping.class, PutMapping.class, PostMapping.class, PatchMapping.class, DeleteMapping.class, RequestMapping.class)
                        .filter(method::isAnnotationPresent).forEach(annotationClass -> {
                            Optional<ApiInfoDto.ApiInfoDtoBuilder> apiInfoDto = ReflectionUtil.parseRequestAnnotation(method, method.getAnnotation(annotationClass));
                            if (apiInfoDto.isPresent()) {
                                ApiInfoDto.ApiInfoDtoBuilder builder = apiInfoDto.get();
                                if (method.isAnnotationPresent(ApiMapping.class)) {
                                    ApiMapping apiMapping = method.getAnnotation(ApiMapping.class);
                                    ReflectionUtil.parseParameter(method.getParameters(), builder);
                                    builder.description(apiMapping.description());
                                    builder.order(apiMapping.order());
                                    builder.category(controllerClass.getSimpleName());
                                    apiList.add(builder.build());
                                }
                            }
                        });
            }
        }

        apiList.sort(Comparator.comparingInt(ApiInfoDto::getOrder));

        return ResponseEntity.ok(apiList);
    }
}
