package io.github.twinklekhj.board.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class BoardDetailDto {
    private int id;
    private String title;
    private String writer;
    private String content;
    private int hits;
    private boolean hide;
}
