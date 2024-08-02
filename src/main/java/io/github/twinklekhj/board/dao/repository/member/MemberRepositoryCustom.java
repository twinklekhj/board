package io.github.twinklekhj.board.dao.repository.member;

import io.github.twinklekhj.board.api.dto.UserDto;

import java.util.Optional;

public interface MemberRepositoryCustom {
    Optional<UserDto> findById(String id);
}
