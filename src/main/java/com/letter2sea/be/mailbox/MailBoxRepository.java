package com.letter2sea.be.mailbox;

import com.letter2sea.be.mailbox.domain.MailBox;
import com.letter2sea.be.member.Member;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MailBoxRepository extends JpaRepository<MailBox, Long> {

    MailBox findByLetterIdAndMemberId(Long letterId, Long memberId);

    boolean existsByLetterIdAndMemberId(Long letterId, Long memberId);

    Optional<MailBox> findByIdAndMember(Long letterId, Member member);
}
