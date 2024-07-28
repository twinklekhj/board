package io.github.twinklekhj.board.api.service;

import io.github.twinklekhj.board.api.dto.BoardListDto;
import io.github.twinklekhj.board.api.param.board.BoardSearchParam;
import io.github.twinklekhj.board.api.param.board.BoardWriteParam;
import io.github.twinklekhj.board.api.vo.PageVO;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;

public interface BoardService {
    PageVO<BoardListDto> findBy(BoardSearchParam param);

    ResponseEntity<?> write(UserDetails userDetails, BoardWriteParam param);
}
