package io.github.twinklekhj.board.api.param.type;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PagingParam {
    @NotNull
    private int pageIdx = 1;
    @NotNull
    private int pageSize = 25;
    private String sortField;
    private String sortDir;
}
