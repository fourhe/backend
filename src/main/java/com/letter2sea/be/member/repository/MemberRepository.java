package com.letter2sea.be.member.repository;

import com.letter2sea.be.member.Member;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member,Long> {

    Optional<Member> findByKakaoId(Long kakaoId);
}
