package com.letter2sea.be.letter.service;

import com.letter2sea.be.letter.domain.Letter;
import com.letter2sea.be.letter.dto.LetterCreateRequest;
import com.letter2sea.be.letter.dto.LetterDetailResponse;
import com.letter2sea.be.letter.dto.LetterListResponse;
import com.letter2sea.be.letter.repository.LetterRepository;
import com.letter2sea.be.member.Member;
import com.letter2sea.be.member.repository.MemberRepository;
import java.security.SecureRandom;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    public Long getRandom(Long memberId) {

        Member member = memberRepository.findById(memberId).orElseThrow();

        List<Long> readLetters = member.getMailBoxes().stream()
            .map(mailBox -> mailBox.getLetter().getId())
            .toList();

        if (readLetters.isEmpty()) {
            List<Letter> unreadLetters = letterRepository.findAllByWriterNot(member);
            if (unreadLetters.isEmpty()) {
                throw new RuntimeException("더이상 읽을 편지가 없습니다.");
            }
            return unreadLetters.get(random.nextInt(unreadLetters.size())).getId();
            //읽을 편지가 없다면 에러메시지를 응답값으로 전달 예정
        }
        List<Letter> unReadLetters = letterRepository.findAllByWriterNotAndIdNotIn(
            member, readLetters);
        if (unReadLetters.isEmpty()) {
            throw new RuntimeException("더이상 읽을 편지가 없습니다.");
        }
        return unReadLetters.get(random.nextInt(unReadLetters.size())).getId();
    }
}
