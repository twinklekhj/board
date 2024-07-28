package io.github.twinklekhj.board.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class BoardListDto {
    private long id;
    private String title;
    private String writer;
    private int hits;
    private boolean hide;
}
