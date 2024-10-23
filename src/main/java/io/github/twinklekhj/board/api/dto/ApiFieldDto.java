package io.github.twinklekhj.board.api.dto;

import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ApiFieldDto {
    public String name;
    public String type;
    public boolean required;
}
