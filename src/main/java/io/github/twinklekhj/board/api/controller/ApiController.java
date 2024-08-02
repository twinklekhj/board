package io.github.twinklekhj.board.api.controller;

import io.github.twinklekhj.board.api.dto.ApiInfoDto;
import io.github.twinklekhj.board.api.service.api.ApiService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ApiController {
    private final ApiService apiService;

    @GetMapping("/api/list")
    public ResponseEntity<List<ApiInfoDto>> getApiList() {
        return apiService.getApiList();
    }

    @GetMapping(path = {"/favicon", "/favicon.ico"})
    public String favicon() {
        return "forward:/static/favicon.png";
    }
}
