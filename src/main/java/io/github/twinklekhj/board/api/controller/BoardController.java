package io.github.twinklekhj.board.api.controller;

import io.github.twinklekhj.board.annotation.ApiMapping;
import io.github.twinklekhj.board.api.dto.BoardListDto;
import io.github.twinklekhj.board.api.param.board.BoardSearchParam;
import io.github.twinklekhj.board.api.param.board.BoardWriteParam;
import io.github.twinklekhj.board.api.service.BoardService;
import io.github.twinklekhj.board.api.vo.PageVO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
public class BoardController {
    private final BoardService boardService;

    @PostMapping(path = "/api/boards")
    @ApiMapping(order = 10, description = "[게시판 관리] 게시판 목록 조회")
    public ResponseEntity<PageVO<BoardListDto>> board(@RequestBody BoardSearchParam param) {
        log.info("param: {}", param);
        return ResponseEntity.ok(boardService.findBy(param));
    }

    @PutMapping(path = "/api/board")
    @ApiMapping(order = 11, description = "[게시판 관리] 게시판 글 작성")
    public ResponseEntity<?> write(@AuthenticationPrincipal UserDetails userDetails, @Valid @RequestBody BoardWriteParam param) {
        return boardService.write(userDetails, param);
    }

    @GetMapping(path = "/api/board/{id}")
    @ApiMapping(order = 12, description = "[게시판 관리] 게시판 상세 정보 조회")
    public ResponseEntity<?> getBoardDetail(@AuthenticationPrincipal UserDetails userDetails, @PathVariable Long id) {
        return boardService.getBoardDetail(id);
    }

    @PatchMapping(path = "/api/board/{id}")
    @ApiMapping(order = 13, description = "[게시판 관리] 게시판 상세 정보 수정")
    public ResponseEntity<?> update(@AuthenticationPrincipal UserDetails userDetails, @PathVariable Long id, @RequestBody BoardWriteParam param) {
        return boardService.updateBoardDetail(id, param);
    }

    @DeleteMapping(path = "/api/board/{id}")
    @ApiMapping(order = 14, description = "[게시판 관리] 게시판 글 삭제")
    public ResponseEntity<?> delete(@AuthenticationPrincipal UserDetails userDetails, @PathVariable Long id) {
        return boardService.delete(id);
    }
}
