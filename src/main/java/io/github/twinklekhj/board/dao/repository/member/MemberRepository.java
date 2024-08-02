package io.github.twinklekhj.board.dao.repository.member;

import io.github.twinklekhj.board.dao.entity.Member;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface MemberRepository extends CrudRepository<Member, Long>, MemberRepositoryCustom {
    Optional<Member> findByMemberId(String memberId);
}
