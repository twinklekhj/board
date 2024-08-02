package io.github.twinklekhj.board.api.param.board;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class BoardWriteParam {
    @NotNull
    private String title;
    @NotNull
    private String content;
    @Builder.Default
    private Boolean visible = true;
}
