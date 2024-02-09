package com.letter2sea.be.menu.service;

import com.letter2sea.be.exception.Letter2SeaException;
import com.letter2sea.be.exception.type.MemberExceptionType;
import com.letter2sea.be.member.Member;
import com.letter2sea.be.member.repository.MemberRepository;
import com.letter2sea.be.menu.dto.MenuInfoResponse;
import com.letter2sea.be.trash.TrashRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MenuService {
    private final MemberRepository memberRepository;
    private final TrashRepository trashRepository;

    public MenuInfoResponse getMenuInfo(Long memberId) {
        Member member = findMember(memberId);
        int trashCount = trashRepository.countAllByMemberId(member.getId());
        return new MenuInfoResponse(member, trashCount);
    }

    private Member findMember(Long writerId) {
        return memberRepository.findById(writerId)
            .orElseThrow(() -> new Letter2SeaException(MemberExceptionType.MEMBER_NOT_FOUND));
    }
}
