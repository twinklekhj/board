package io.github.twinklekhj.board.api.controller;

import io.github.twinklekhj.board.annotation.ApiOperation;
import io.github.twinklekhj.board.annotation.ApiGroup;
import io.github.twinklekhj.board.api.dto.BoardListDto;
import io.github.twinklekhj.board.api.param.board.BoardSearchParam;
import io.github.twinklekhj.board.api.param.board.BoardWriteParam;
import io.github.twinklekhj.board.api.service.board.BoardService;
import io.github.twinklekhj.board.api.vo.PageVO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@ApiGroup(order = 2, name = "게시판 관리")
@RequiredArgsConstructor
@Slf4j
public class BoardController {
    private final BoardService boardService;

    @PostMapping(path = "/api/boards")
    @ApiOperation(order = 1, description = "게시글 목록 조회")
    public ResponseEntity<PageVO<BoardListDto>> board(@RequestBody BoardSearchParam param) {
        log.info("param: {}", param);
        return boardService.findBy(param);
    }

    @PutMapping(path = "/api/board")
    @ApiOperation(order = 2, description = "게시글 작성")
    public ResponseEntity<?> write(@AuthenticationPrincipal UserDetails userDetails, @Valid @RequestBody BoardWriteParam param) {
        return boardService.write(userDetails, param);
    }

    @GetMapping(path = "/api/board/{id}")
    @ApiOperation(order = 3, description = "게시글 조회")
    public ResponseEntity<?> getBoardDetail(@AuthenticationPrincipal UserDetails userDetails, @PathVariable Long id) {
        return boardService.getBoardDetail(id);
    }

    @PatchMapping(path = "/api/board/{id}")
    @ApiOperation(order = 4, description = "게시글 수정")
    public ResponseEntity<?> update(@AuthenticationPrincipal UserDetails userDetails, @PathVariable Long id, @RequestBody BoardWriteParam param) {
        return boardService.updateBoardDetail(id, param);
    }

    @DeleteMapping(path = "/api/board/{id}")
    @ApiOperation(order = 5, description = "게시글 삭제")
    public ResponseEntity<?> delete(@AuthenticationPrincipal UserDetails userDetails, @PathVariable Long id) {
        return boardService.delete(id);
    }
}
