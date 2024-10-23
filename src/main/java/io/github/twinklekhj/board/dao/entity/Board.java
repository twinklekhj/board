package io.github.twinklekhj.board.dao.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Comment;

@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity(name = "tbl_board")
@SequenceGenerator(name = "SEQ_BOARD_GENERATOR", sequenceName = "SEQ_BOARD", initialValue = 1, allocationSize = 1)
public class Board extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_BOARD_GENERATOR")
    private Long id;

    @Column(name = "member_id", nullable = false)
    private Long memberId;

    @Column(name = "title", length = 100)
    @Comment("제목")
    private String title;

    @Column(name = "content")
    @Lob
    @Comment("내용")
    private String content;

    @Column(name = "hits")
    @Comment("조회수")
    @Builder.Default
    private Integer hits = 0;

    @Column(name = "visible")
    @Comment("비공개")
    @Builder.Default
    private Boolean visible = true;
}
