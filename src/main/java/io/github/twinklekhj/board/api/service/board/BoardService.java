package io.github.twinklekhj.board.api.service.board;

import io.github.twinklekhj.board.api.dto.BoardDetailDto;
import io.github.twinklekhj.board.api.dto.BoardListDto;
import io.github.twinklekhj.board.api.param.board.BoardSearchParam;
import io.github.twinklekhj.board.api.param.board.BoardWriteParam;
import io.github.twinklekhj.board.api.vo.PageVO;
import io.github.twinklekhj.board.exception.DataNotFoundException;
import io.github.twinklekhj.board.login.MemberDetails;
import org.springframework.http.ResponseEntity;

public interface BoardService {
    ResponseEntity<PageVO<BoardListDto>> findBy(BoardSearchParam param);

    ResponseEntity<?> write(MemberDetails memberDetails, BoardWriteParam param);

    ResponseEntity<BoardDetailDto> getBoardDetail(MemberDetails memberDetails, Long id) throws DataNotFoundException;

    ResponseEntity<?> updateBoardDetail(MemberDetails memberDetails, Long id, BoardWriteParam param);

    ResponseEntity<?> delete(MemberDetails memberDetails, Long id);
}
