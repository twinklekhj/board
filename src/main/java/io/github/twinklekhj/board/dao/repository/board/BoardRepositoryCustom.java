package io.github.twinklekhj.board.dao.repository.board;

import io.github.twinklekhj.board.api.dto.BoardDetailDto;
import io.github.twinklekhj.board.api.dto.BoardListDto;
import io.github.twinklekhj.board.api.param.board.BoardSearchParam;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;

import java.util.Optional;

public interface BoardRepositoryCustom {
    Page<BoardListDto> findBy(BoardSearchParam param);

    Optional<BoardDetailDto> findBy(Long id);

    @Transactional
    void increaseHits(Long id);
}
