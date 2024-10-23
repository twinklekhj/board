package io.github.twinklekhj.board.dao.repository.board;

import io.github.twinklekhj.board.dao.entity.Board;
import jakarta.transaction.Transactional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardRepository extends CrudRepository<Board, Long>, BoardRepositoryCustom {
}
