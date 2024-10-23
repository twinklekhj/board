package io.github.twinklekhj.board.api.controller;

import io.github.twinklekhj.board.annotation.ApiGroup;
import io.github.twinklekhj.board.annotation.ApiOperation;
import io.github.twinklekhj.board.api.dto.BoardListDto;
import io.github.twinklekhj.board.api.param.board.BoardSearchParam;
import io.github.twinklekhj.board.api.param.board.BoardWriteParam;
import io.github.twinklekhj.board.api.service.board.BoardService;
import io.github.twinklekhj.board.api.vo.PageVO;
import io.github.twinklekhj.board.login.MemberDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api/boards")
@ApiGroup(order = 2, name = "게시판 관리")
@RequiredArgsConstructor
@Slf4j
public class BoardController {
    private final BoardService boardService;

    @PostMapping
    @ApiOperation(order = 1, description = "게시글 목록 조회")
    public ResponseEntity<PageVO<BoardListDto>> board(@RequestBody BoardSearchParam param) {
        return boardService.findBy(param);
    }

    @PutMapping
    @ApiOperation(order = 2, description = "게시글 작성")
    public ResponseEntity<?> write(@AuthenticationPrincipal MemberDetails memberDetails, @Valid @RequestBody BoardWriteParam param) {
        return boardService.write(memberDetails, param);
    }

    @GetMapping(path = "/{id}")
    @ApiOperation(order = 3, description = "게시글 조회")
    public ResponseEntity<?> getBoardDetail(@AuthenticationPrincipal MemberDetails memberDetails, @PathVariable Long id) {
        return boardService.getBoardDetail(memberDetails, id);
    }

    @PatchMapping(path = "/{id}")
    @ApiOperation(order = 4, description = "게시글 수정")
    public ResponseEntity<?> update(@AuthenticationPrincipal MemberDetails memberDetails, @PathVariable Long id, @RequestBody BoardWriteParam param) {
        return boardService.updateBoardDetail(memberDetails, id, param);
    }

    @DeleteMapping(path = "/{id}")
    @ApiOperation(order = 5, description = "게시글 삭제")
    public ResponseEntity<?> delete(@AuthenticationPrincipal MemberDetails memberDetails, @PathVariable Long id) {
        return boardService.delete(memberDetails, id);
    }
}
