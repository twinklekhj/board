package io.github.twinklekhj.board.api.param.board;

import io.github.twinklekhj.board.api.param.type.PagingParam;
import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class BoardSearchParam extends PagingParam {
    private String title; // 제목 검색
    private String content; // 내용 검색
    private String writer; // 글쓴이 검색
    private String all; // 전체 검색
}
