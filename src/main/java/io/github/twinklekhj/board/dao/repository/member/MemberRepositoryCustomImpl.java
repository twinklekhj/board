package io.github.twinklekhj.board.dao.repository.member;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import io.github.twinklekhj.board.api.dto.UserDto;
import io.github.twinklekhj.board.dao.entity.QMember;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class MemberRepositoryCustomImpl implements MemberRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<UserDto> findById(String id) {
        QMember qMember = QMember.member;

        return Optional.ofNullable(queryFactory
                .select(Projections.constructor(UserDto.class, qMember.id, qMember.memberId, qMember.name, qMember.email, qMember.role))
                .from(qMember)
                .where(qMember.memberId.eq(id))
                .fetchOne());
    }
}
