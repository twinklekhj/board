package io.github.twinklekhj.board.api.service.board;

import io.github.twinklekhj.board.api.dto.BoardDetailDto;
import io.github.twinklekhj.board.api.dto.BoardListDto;
import io.github.twinklekhj.board.exception.DataNotFoundException;
import io.github.twinklekhj.board.api.param.board.BoardSearchParam;
import io.github.twinklekhj.board.api.param.board.BoardWriteParam;
import io.github.twinklekhj.board.api.vo.PageVO;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;

public interface BoardService {
    ResponseEntity<PageVO<BoardListDto>> findBy(BoardSearchParam param);

    ResponseEntity<?> write(UserDetails userDetails, BoardWriteParam param);

    ResponseEntity<BoardDetailDto> getBoardDetail(Long id) throws DataNotFoundException;

    ResponseEntity<?> updateBoardDetail(Long id, BoardWriteParam param);

    ResponseEntity<?> delete(Long id);
}
