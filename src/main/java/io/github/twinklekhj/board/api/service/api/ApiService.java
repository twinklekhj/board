package io.github.twinklekhj.board.api.service.api;

import io.github.twinklekhj.board.api.dto.ApiGroupDto;

import java.util.List;

public interface ApiService {
    List<ApiGroupDto> getApiList();
}
