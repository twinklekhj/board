package io.github.twinklekhj.board.api.service.api;

import io.github.twinklekhj.board.api.dto.ApiGroupDto;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ApiService {
    ResponseEntity<List<ApiGroupDto>> getApiList();
}
