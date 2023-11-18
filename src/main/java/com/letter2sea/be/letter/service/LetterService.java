package com.letter2sea.be.letter.service;

import com.letter2sea.be.letter.domain.Letter;
import com.letter2sea.be.letter.dto.LetterCreateRequest;
import com.letter2sea.be.letter.dto.LetterDetailResponse;
import com.letter2sea.be.letter.dto.LetterListResponse;
import com.letter2sea.be.letter.repository.LetterRepository;
import com.letter2sea.be.mailbox.domain.MailBox;
import com.letter2sea.be.member.Member;
import com.letter2sea.be.member.repository.MemberRepository;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LetterService {

    private static final Random random = new SecureRandom();
    private final LetterRepository letterRepository;
    private final MemberRepository memberRepository;



    @Transactional
    public void create(Long writerId, LetterCreateRequest letterCreateRequest) {
        Member member = memberRepository.findById(writerId).orElseThrow();
        Letter letter = letterCreateRequest.toEntity(member);
        letterRepository.save(letter);
    }

    public List<LetterListResponse> findList(Long writerId) {
        return letterRepository.findAllByWriterId(writerId)
            .stream()
            .map(LetterListResponse::new)
            .collect(Collectors.toList());
    }

    public LetterDetailResponse findDetail(Long id, Long writerId) {
        Letter findDetail = letterRepository.findByIdAndWriterId(id, writerId)
            .orElseThrow();

        return new LetterDetailResponse(findDetail);
    }

    public List<LetterListResponse> getRandom(Long memberId) {

        // 0, size()

        Member member = memberRepository.findById(memberId)
            .orElseThrow();

        log.info("member id = {}", member.getId());

        List<Long> readLetters = member.getMailBoxes().stream()
            .map(mailBox -> member.getId())
            .toList();

        log.info("read letter ids = {}", Arrays.toString(readLetters.toArray()));
        List<Letter> result = letterRepository.findAllByWriterNotAndIdNotIn(member, readLetters);

        log.info("result = {}", Arrays.toString(result.toArray()));

        return result.stream()
            .map(LetterListResponse::new)
            .toList();
    }
}
