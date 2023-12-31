package com.letter2sea.be.trash;

import com.letter2sea.be.letter.repository.LetterRepository;
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
    private final LetterRepository letterRepository;

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

    @Transactional
    public void restore(Long id, Long memberId) {
        findMember(memberId);
        boolean existsByIdAndMemberId = trashRepository.existsByIdAndMemberId(id, memberId);
        Trash deletedLetter = trashRepository.findById(id).orElseThrow();

        //해당부분에서 에러남
        deletedLetter.getLetter().restoreDeletedAt();
        if (!existsByIdAndMemberId) {
            throw new RuntimeException("존재하지 않은 편지입니다.");
        }

        trashRepository.deleteByIdAndMemberId(id,memberId);
    }
    private void findMember(Long memberId) {
        memberRepository.findById(memberId).orElseThrow();
    }
}
