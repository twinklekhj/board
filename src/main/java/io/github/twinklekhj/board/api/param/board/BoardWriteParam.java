package io.github.twinklekhj.board.api.param.board;

import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class BoardWriteParam {
    private String title;
    private String content;
    private Boolean hide = false;
}
