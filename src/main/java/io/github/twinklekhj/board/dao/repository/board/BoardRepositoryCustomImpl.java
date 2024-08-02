package io.github.twinklekhj.board.dao.repository.board;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.*;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import io.github.twinklekhj.board.api.dto.BoardDetailDto;
import io.github.twinklekhj.board.api.dto.BoardListDto;
import io.github.twinklekhj.board.api.param.board.BoardSearchParam;
import io.github.twinklekhj.board.dao.entity.QBoard;
import io.github.twinklekhj.board.dao.entity.QMember;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class BoardRepositoryCustomImpl implements BoardRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    private BooleanBuilder getCondition(BoardSearchParam param, boolean isJoin) {
        QBoard board = QBoard.board;
        QMember qMember = QMember.member;

        BooleanBuilder builder = new BooleanBuilder();
        if (param.getAll() != null && !param.getAll().isEmpty()){
            BooleanBuilder orBuilder = new BooleanBuilder(board.title.like("%" + param.getAll() + "%"));
            orBuilder.or(board.content.like("%" + param.getAll() + "%"));
            orBuilder.or(qMember.memberId.like("%" + param.getAll() + "%"));

            builder.and(orBuilder);
        }
        else {
            if (param.getTitle() != null && !param.getTitle().isEmpty()) {
                builder.and(board.title.like("%" + param.getTitle() + "%"));
            }
            if (param.getContent() != null && !param.getContent().isEmpty()) {
                builder.and(board.content.like("%" + param.getContent() + "%"));
            }
            if (param.getWriter() != null && !param.getWriter().isEmpty()) {
                builder.and(qMember.memberId.like("%" + param.getWriter() + "%"));
            }
        }

        return builder;
    }

    private OrderSpecifier<?> getSortedColumn(Order order, String fieldName) {
        Path<Object> fieldPath = Expressions.path(Object.class, QBoard.board, fieldName);
        return new OrderSpecifier(order, fieldPath);
    }

    public List<BoardListDto> getBy(BoardSearchParam param) {
        QBoard qBoard = QBoard.board;
        QMember qMember = QMember.member;

        OrderSpecifier<?> specifier = qBoard.id.desc();
        if (param.getSortField() != null && !param.getSortField().isEmpty()) {
            Order order = Order.ASC;
            if (param.getSortDir() != null && param.getSortDir().equalsIgnoreCase("desc")) {
                order = Order.DESC;
            }
            specifier = getSortedColumn(order, param.getSortField());
        }

        return queryFactory
                .select(Projections.constructor(BoardListDto.class,
                        qBoard.id, qBoard.title, qMember.memberId, qBoard.hits, qBoard.visible, qBoard.createDate, qBoard.editDate)
                )
                .from(qBoard)
                .where(getCondition(param, true))
                .leftJoin(qMember)
                .on(qMember.id.eq(qBoard.member.id))
                .offset((long) (param.getPageIdx() - 1) * param.getPageSize())
                .limit(param.getPageSize())
                .orderBy(specifier)
                .fetch();
    }

    public Long countBy(BoardSearchParam param) {
        QBoard qBoard = QBoard.board;
        QMember qMember = QMember.member;
        return queryFactory
                .select(qBoard.count())
                .from(qBoard)
                .where(getCondition(param, false))
                .leftJoin(qMember)
                .on(qMember.id.eq(qBoard.member.id))
                .fetchOne();
    }

    @Override
    public Page<BoardListDto> findBy(BoardSearchParam param) {
        PageRequest request = PageRequest.of(param.getPageIdx() - 1, param.getPageSize());
        return new PageImpl<>(getBy(param), request, countBy(param));
    }

    @Override
    public Optional<BoardDetailDto> findBy(Long id) {
        QBoard qBoard = QBoard.board;
        QMember qMember = QMember.member;
        return Optional.ofNullable(queryFactory
                .select(Projections.constructor(BoardDetailDto.class,
                        qBoard.id, qBoard.title, qMember.id, qMember.memberId, qBoard.content, qBoard.hits, qBoard.visible, qBoard.createDate, qBoard.editDate))
                .from(qBoard)
                .where(qBoard.id.eq(id))
                .leftJoin(qMember)
                .on(qMember.id.eq(qBoard.member.id))
                .fetchOne());
    }

    @Transactional
    public void increaseHits(Long id) {
        QBoard qBoard = QBoard.board;
        queryFactory.update(qBoard)
                .set(qBoard.hits, qBoard.hits.add(1))
                .where(qBoard.id.eq(id))
                .execute();
    }
}
