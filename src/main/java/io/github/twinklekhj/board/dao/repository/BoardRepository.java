package io.github.twinklekhj.board.dao.repository;

import io.github.twinklekhj.board.dao.entity.Board;
import org.springframework.data.repository.CrudRepository;

public interface BoardRepository extends CrudRepository<Board, Long>, BoardRepositoryCustom {
}
