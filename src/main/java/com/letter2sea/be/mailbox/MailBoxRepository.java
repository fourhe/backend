package com.letter2sea.be.mailbox;

import com.letter2sea.be.mailbox.domain.MailBox;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MailBoxRepository extends JpaRepository<MailBox, Long> {

    MailBox findByLetterIdAndMemberId(Long letterId, Long memberId);
}
