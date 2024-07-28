package io.github.twinklekhj.board.dao.repository;

import io.github.twinklekhj.board.api.dto.BoardListDto;
import io.github.twinklekhj.board.api.param.board.BoardSearchParam;
import org.springframework.data.domain.Page;

public interface BoardRepositoryCustom {
    Page<BoardListDto> findBy(BoardSearchParam param);
}
