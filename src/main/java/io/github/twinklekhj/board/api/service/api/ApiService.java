package io.github.twinklekhj.board.api.service.api;

import io.github.twinklekhj.board.api.dto.ApiInfoDto;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ApiService {
    ResponseEntity<List<ApiInfoDto>> getApiList();
}
