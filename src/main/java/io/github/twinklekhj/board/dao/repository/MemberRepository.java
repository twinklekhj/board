package io.github.twinklekhj.board.dao.repository;

import io.github.twinklekhj.board.dao.entity.Member;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface MemberRepository extends CrudRepository<Member, Long> {
    Optional<Member> findByMemberId(String memberId);
}
