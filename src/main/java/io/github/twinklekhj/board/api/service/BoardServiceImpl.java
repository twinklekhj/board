package io.github.twinklekhj.board.api.service;

import io.github.twinklekhj.board.api.dto.BoardListDto;
import io.github.twinklekhj.board.api.param.board.BoardSearchParam;
import io.github.twinklekhj.board.api.param.board.BoardWriteParam;
import io.github.twinklekhj.board.api.vo.PageVO;
import io.github.twinklekhj.board.dao.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService {
    private final BoardRepository boardRepository;

    @Override
    public PageVO<BoardListDto> findBy(BoardSearchParam param) {
        return PageVO.builder(boardRepository.findBy(param)).build();
    }

    @Override
    public ResponseEntity<?> write(UserDetails userDetails, BoardWriteParam param) {
        return null;
    }
}
