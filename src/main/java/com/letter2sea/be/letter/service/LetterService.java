package com.letter2sea.be.letter.service;

import com.letter2sea.be.letter.domain.Letter;
import com.letter2sea.be.letter.dto.LetterCreateRequest;
import com.letter2sea.be.letter.dto.LetterDetailResponse;
import com.letter2sea.be.letter.dto.LetterListResponse;
import com.letter2sea.be.letter.repository.LetterRepository;
import com.letter2sea.be.mailbox.MailBoxRepository;
import com.letter2sea.be.mailbox.domain.MailBox;
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
    private final MailBoxRepository mailBoxRepository;


    @Transactional
    public void create(Long writerId, LetterCreateRequest letterCreateRequest) {
        Member member = findMember(writerId);
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

        Member member = findMember(memberId);

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

    public LetterDetailResponse read(Long id, Long memberId) {
        Member member = findMember(memberId);
        Letter letter = letterRepository.findById(id).orElseThrow();

        boolean existReadLetter = member.getMailBoxes().stream()
            .anyMatch(mailBox -> mailBox.getLetter().getId().equals(id));
        boolean existsByIdAndWriterId = letterRepository.existsByIdAndWriterId(id, memberId);

        if (existsByIdAndWriterId || existReadLetter) {
            throw new RuntimeException("잘못된 id입니다.");
        }
        mailBoxRepository.save(new MailBox(letter, member));
        return new LetterDetailResponse(letter);
    }

    private Member findMember(Long writerId) {
        return memberRepository.findById(writerId).orElseThrow();
    }

    //랜덤 줍기 구현 중 리스트를 응답으로 주는 메서드 임시 구현
    public List<LetterListResponse> randomTest(Long memberId) {
        Member member = findMember(memberId);

        List<Long> readLetters = member.getMailBoxes().stream()
            .map(mailBox -> mailBox.getLetter().getId())
            .toList();

        List<Letter> unReadLetters = letterRepository.findAllByWriterNotAndIdNotIn(
            member, readLetters);

        return unReadLetters.stream().map(LetterListResponse::new).toList();
    }
}
