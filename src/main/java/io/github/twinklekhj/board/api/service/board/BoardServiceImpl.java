package io.github.twinklekhj.board.api.service.board;

import io.github.twinklekhj.board.api.dto.BoardDetailDto;
import io.github.twinklekhj.board.api.dto.BoardListDto;
import io.github.twinklekhj.board.api.param.board.BoardSearchParam;
import io.github.twinklekhj.board.api.param.board.BoardWriteParam;
import io.github.twinklekhj.board.api.vo.PageVO;
import io.github.twinklekhj.board.dao.entity.Board;
import io.github.twinklekhj.board.dao.repository.board.BoardRepository;
import io.github.twinklekhj.board.exception.DataNotFoundException;
import io.github.twinklekhj.board.exception.UnauthorizedException;
import io.github.twinklekhj.board.login.MemberDetails;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class BoardServiceImpl implements BoardService {
    private final BoardRepository boardRepository;

    @Override
    public ResponseEntity<PageVO<BoardListDto>> findBy(BoardSearchParam param) {
        return ResponseEntity.ok(PageVO.builder(boardRepository.findBy(param)).build());
    }

    @Override
    @Transactional
    public ResponseEntity<?> write(MemberDetails memberDetails, BoardWriteParam param) {
        Board newBoard = Board.builder()
                .title(param.getTitle())
                .content(param.getContent())
                .memberId(memberDetails.getId())
                .visible(param.getVisible())
                .build();

        Board board = boardRepository.save(newBoard);
        return ResponseEntity
                .created(URI.create("/api/boards/" + board.getId()))
                .body("정상적으로 등록되었습니다!");
    }

    @Override
    public ResponseEntity<BoardDetailDto> getBoardDetail(MemberDetails memberDetails, Long id) throws DataNotFoundException {
        Optional<BoardDetailDto> boardOptional = boardRepository.findBy(id);
        if (boardOptional.isPresent()) {
            BoardDetailDto dto = boardOptional.get();
            if(!dto.getVisible() && !dto.getWriterId().equals(memberDetails.getId())){
                throw new UnauthorizedException();
            }
            return ResponseEntity.ok(dto);
        }
        throw new DataNotFoundException("게시글을 찾을 수 없습니다!");
    }

    @Override
    public ResponseEntity<?> updateBoardDetail(MemberDetails memberDetails, Long id, BoardWriteParam param) {
        Optional<Board> boardOptional = boardRepository.findById(id);
        if (boardOptional.isPresent()) {
            Board board = boardOptional.get();
            if (!board.getMemberId().equals(memberDetails.getId())){
                throw new UnauthorizedException();
            }

            board.setTitle(param.getTitle());
            board.setContent(param.getContent());
            board.setVisible(param.getVisible());
            boardRepository.save(board);
            return ResponseEntity.noContent().build();
        }

        throw new DataNotFoundException("게시글을 찾을 수 없습니다!");
    }

    @Override
    public ResponseEntity<?> delete(MemberDetails memberDetails, Long id) {
        Optional<Board> boardOptional = boardRepository.findById(id);
        if (boardOptional.isPresent()) {
            Board board = boardOptional.get();
            if (!board.getMemberId().equals(memberDetails.getId())){
                throw new UnauthorizedException();
            }
            boardRepository.delete(board);
            return ResponseEntity.noContent().build();
        }
        throw new DataNotFoundException("게시글을 찾을 수 없습니다!");
    }
}
