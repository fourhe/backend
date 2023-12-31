package com.letter2sea.be.trash;

import com.letter2sea.be.member.repository.MemberRepository;
import com.letter2sea.be.trash.domain.Trash;
import com.letter2sea.be.trash.dto.TrashDetailResponse;
import com.letter2sea.be.trash.dto.TrashListResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TrashService {

    private final TrashRepository trashRepository;
    private final MemberRepository memberRepository;

    public List<TrashListResponse> findList(Long memberId) {
        findMember(memberId);
        return trashRepository.findAllByMemberId(memberId).stream()
            .map(TrashListResponse::new)
            .toList();
    }

    public TrashDetailResponse findDetail(Long id, Long memberId) {
        findMember(memberId);
        Trash findTrash = trashRepository.findByIdAndMemberId(id, memberId).orElseThrow();
       return new TrashDetailResponse(findTrash);
    }

    private void findMember(Long memberId) {
        memberRepository.findById(memberId).orElseThrow();
    }
}
