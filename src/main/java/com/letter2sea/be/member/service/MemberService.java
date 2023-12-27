package com.letter2sea.be.member.service;

import com.letter2sea.be.exception.Letter2SeaException;
import com.letter2sea.be.exception.type.MemberExceptionType;
import com.letter2sea.be.member.Member;
import com.letter2sea.be.member.dto.MemberUpdateRequest;
import com.letter2sea.be.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

    @Transactional
    public void update(Long memberId, MemberUpdateRequest updateRequest) {
        Member member = findMember(memberId);
        member.updateEmail(updateRequest.email());
    }

    private Member findMember(Long writerId) {
        return memberRepository.findById(writerId)
            .orElseThrow(() -> new Letter2SeaException(MemberExceptionType.MEMBER_NOT_FOUND));
    }
}
