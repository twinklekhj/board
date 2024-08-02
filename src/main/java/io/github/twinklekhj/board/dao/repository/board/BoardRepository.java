package io.github.twinklekhj.board.dao.repository.board;

import io.github.twinklekhj.board.dao.entity.Board;
import jakarta.transaction.Transactional;
import org.springframework.data.repository.CrudRepository;

public interface BoardRepository extends CrudRepository<Board, Long>, BoardRepositoryCustom {
    @Transactional
    Board save(Board entity);
}
