package io.github.twinklekhj.board.api.service;

import io.github.twinklekhj.board.api.dto.BoardDetailDto;
import io.github.twinklekhj.board.api.dto.BoardListDto;
import io.github.twinklekhj.board.api.exception.DataNotFoundException;
import io.github.twinklekhj.board.api.exception.UnauthorizedException;
import io.github.twinklekhj.board.api.param.board.BoardSearchParam;
import io.github.twinklekhj.board.api.param.board.BoardWriteParam;
import io.github.twinklekhj.board.api.vo.PageVO;
import io.github.twinklekhj.board.dao.entity.Board;
import io.github.twinklekhj.board.dao.entity.Member;
import io.github.twinklekhj.board.dao.repository.board.BoardRepository;
import io.github.twinklekhj.board.dao.repository.member.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService {
    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;

    @Override
    public PageVO<BoardListDto> findBy(BoardSearchParam param) {
        return PageVO.builder(boardRepository.findBy(param)).build();
    }

    @Override
    @Transactional
    public ResponseEntity<?> write(UserDetails userDetails, BoardWriteParam param) {
        Optional<Member> memberOptional = memberRepository.findByMemberId(userDetails.getUsername());
        if (memberOptional.isEmpty()) {
            throw new UnauthorizedException();
        }
        Board newBoard = Board.builder()
                .title(param.getTitle())
                .content(param.getContent())
                .member(memberOptional.get())
                .visible(param.getVisible())
                .build();

        Board board = boardRepository.save(newBoard);
        return ResponseEntity
                .created(URI.create("/api/board/" + board.getId()))
                .body("정상적으로 등록되었습니다!");
    }

    @Override
    public ResponseEntity<BoardDetailDto> getBoardDetail(Long id) throws DataNotFoundException {
        Optional<BoardDetailDto> boardOptional = boardRepository.findBy(id);
        if (boardOptional.isPresent()) {
            BoardDetailDto dto = boardOptional.get();
            return ResponseEntity.ok(dto);
        }
        throw new DataNotFoundException("게시글을 찾을 수 없습니다!");
    }

    @Override
    public ResponseEntity<?> updateBoardDetail(Long id, BoardWriteParam param) {
        Optional<Board> boardOptional = boardRepository.findById(id);
        if (boardOptional.isPresent()) {
            Board board = boardOptional.get();
            board.setTitle(param.getTitle());
            board.setContent(param.getContent());
            board.setVisible(param.getVisible());
            board.setEditDate(LocalDateTime.now());
            boardRepository.save(board);
            return ResponseEntity.noContent().build();
        }

        throw new DataNotFoundException("게시글을 찾을 수 없습니다!");
    }

    @Override
    public ResponseEntity<?> delete(Long id) {
        Optional<Board> boardOptional = boardRepository.findById(id);
        if (boardOptional.isPresent()) {
            boardRepository.delete(boardOptional.get());
            return ResponseEntity.noContent().build();
        }
        throw new DataNotFoundException("게시글을 찾을 수 없습니다!");
    }
}
