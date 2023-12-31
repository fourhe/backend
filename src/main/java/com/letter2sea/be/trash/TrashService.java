package com.letter2sea.be.trash;

import com.letter2sea.be.member.repository.MemberRepository;
import com.letter2sea.be.trash.dto.TrashListResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TrashService {

    private final MemberRepository memberRepository;
    private final TrashRepository trashRepository;
    public List<TrashListResponse> findList(Long memberId) {
        findMember(memberId);
        return trashRepository.findAllByMemberId(memberId).stream()
            .map(TrashListResponse::new)
            .toList();
    }

    private void findMember(Long memberId) {
        memberRepository.findById(memberId).orElseThrow();
    }
}
