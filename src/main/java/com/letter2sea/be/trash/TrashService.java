package com.letter2sea.be.trash;

import static com.letter2sea.be.exception.type.LetterExceptionType.LETTER_NOT_FOUND;
import static com.letter2sea.be.exception.type.MemberExceptionType.MEMBER_NOT_FOUND;

import com.letter2sea.be.exception.Letter2SeaException;
import com.letter2sea.be.letter.domain.Letter;
import com.letter2sea.be.letter.repository.LetterRepository;
import com.letter2sea.be.member.repository.MemberRepository;
import com.letter2sea.be.trash.domain.Trash;
import com.letter2sea.be.trash.dto.TrashDetailResponse;
import com.letter2sea.be.trash.dto.TrashListResponse;
import com.letter2sea.be.trash.dto.TrashPaginatedResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TrashService {

    private static final int MAX_PAGE_SIZE = 1000;

    private final TrashRepository trashRepository;
    private final MemberRepository memberRepository;
    private final LetterRepository letterRepository;

    public TrashPaginatedResponse findList(String type, Pageable pageable, Long memberId) {
        findMember(memberId);

        Pageable pageRequest = exchangePageRequest(pageable);

        if (type.equals("letter")) {
            Page<Trash> findList = trashRepository.findAllByLetterTrash(memberId, pageRequest);
            List<TrashListResponse> trashListResponses = findList.getContent().stream()
                .map(TrashListResponse::new).toList();

            return new TrashPaginatedResponse(pageable.getPageNumber(), findList.getSize(),
                findList.getTotalPages(), trashListResponses);
        }

        Page<Trash> findList = trashRepository.findAllByReplyTrash(memberId, pageRequest);
        List<TrashListResponse> trashListResponses = findList.getContent().stream()
            .map(TrashListResponse::new).toList();

        return new TrashPaginatedResponse(pageable.getPageNumber(), findList.getSize(),
            findList.getTotalPages(), trashListResponses);
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

        if (!existsByIdAndMemberId) {
            throw new Letter2SeaException(LETTER_NOT_FOUND);
        }

        Trash deletedLetter = trashRepository.findById(id)
            .orElseThrow(() -> new Letter2SeaException(LETTER_NOT_FOUND));
        Letter letter = letterRepository.findByIdIgnoreDeletedAt(deletedLetter.getLetterId())
            .orElseThrow(() -> new Letter2SeaException(LETTER_NOT_FOUND));

        letter.restoreDeletedAt();

        trashRepository.deleteByIdAndMemberId(id, memberId);
    }

    @Transactional
    public void delete(Long id, Long memberId) {
        findMember(memberId);
        boolean existsByIdAndMemberId = trashRepository.existsByIdAndMemberId(id, memberId);

        if (!existsByIdAndMemberId) {
            throw new Letter2SeaException(LETTER_NOT_FOUND);
        }

        trashRepository.deleteByIdAndMemberId(id, memberId);
    }


    private Pageable exchangePageRequest(Pageable pageable) {
        int pageSize = pageable.getPageSize();

        if (pageSize <= MAX_PAGE_SIZE) {
            return pageable;
        }
        return PageRequest.of(pageable.getPageNumber(), MAX_PAGE_SIZE);
    }

    private void findMember(Long memberId) {
        memberRepository.findById(memberId)
            .orElseThrow(() -> new Letter2SeaException(MEMBER_NOT_FOUND));
    }
}
