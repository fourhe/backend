package com.letter2sea.be.letter.service;

import com.letter2sea.be.letter.domain.Letter;
import com.letter2sea.be.letter.dto.LetterCreateRequest;
import com.letter2sea.be.letter.dto.LetterDetailResponse;
import com.letter2sea.be.letter.dto.LetterListResponse;
import com.letter2sea.be.letter.repository.LetterRepository;
import com.letter2sea.be.member.Member;
import com.letter2sea.be.member.repository.MemberRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LetterService {

    private final LetterRepository letterRepository;
    private final MemberRepository memberRepository;


    @Transactional
    public void create(Long writerId, LetterCreateRequest letterCreateRequest) {
        Member member = memberRepository.findById(writerId).orElseThrow();
        Letter letter = letterCreateRequest.toEntity(member);
        letterRepository.save(letter);
    }

    public List<LetterListResponse> findList(Long writerId) {
        return letterRepository.findByWriterId(writerId)
            .stream()
            .map(LetterListResponse::new)
            .collect(Collectors.toList());
    }

    public LetterDetailResponse findDetail(Long id, Long writerId) {
        Letter findDetail = letterRepository.findByIdAndWriterId(id, writerId);
        return new LetterDetailResponse(findDetail);
    }
}
