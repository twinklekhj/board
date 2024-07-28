package io.github.twinklekhj.board.api.controller;

import io.github.twinklekhj.board.api.dto.BoardListDto;
import io.github.twinklekhj.board.api.param.board.BoardSearchParam;
import io.github.twinklekhj.board.api.param.board.BoardWriteParam;
import io.github.twinklekhj.board.api.service.BoardService;
import io.github.twinklekhj.board.api.vo.PageVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class BoardController {
    private final BoardService boardService;

    @GetMapping("/api/boards")
    public ResponseEntity<PageVO<BoardListDto>> board(@RequestBody BoardSearchParam param) {
        log.info("param: {}", param);
        return ResponseEntity.ok(boardService.findBy(param));
    }

    public ResponseEntity<?> write(@AuthenticationPrincipal UserDetails userDetails, @RequestBody BoardWriteParam param) {
        log.info("write");
        return boardService.write(userDetails, param);
    }
}
