package com.letter2sea.be.letter.service;

import com.letter2sea.be.letter.domain.Letter;
import com.letter2sea.be.letter.dto.request.LetterCreateRequest;
import com.letter2sea.be.letter.dto.request.ReplyCreateRequest;
import com.letter2sea.be.letter.dto.response.LetterDetailResponse;
import com.letter2sea.be.letter.dto.response.LetterListResponse;
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

    @Transactional
    public LetterDetailResponse read(Long id, Long memberId) {
        Member member = findMember(memberId);
        Letter letter = letterRepository.findById(id).orElseThrow();

        boolean existsByIdAndWriterId = letterRepository.existsByIdAndWriterId(id, memberId);
        boolean existAlreadyReadLetter = member.getMailBoxes().stream()
            .anyMatch(mailBox -> mailBox.getLetter().getId().equals(id));

        if (existsByIdAndWriterId || existAlreadyReadLetter) {
            throw new RuntimeException("잘못된 id입니다.");
        }
        mailBoxRepository.save(new MailBox(letter, member));
        return new LetterDetailResponse(letter);
    }

    @Transactional
    public void reply(Long id, Long writerId, ReplyCreateRequest letterReplyRequest) {
        Member member = findMember(writerId);
        Letter letter = letterRepository.findById(id).orElseThrow();
        boolean existsByIdAndWriterId = letterRepository.existsByIdAndWriterId(id, writerId);
        if (existsByIdAndWriterId || letter.getReplyLetterId() != null) {
            throw new RuntimeException("존재하지 않은 편지입니다.");
        }
        boolean existsByWriterIdAndReplyLetterId = letterRepository
            .existsByWriterIdAndReplyLetterId(writerId, id);

        if (existsByWriterIdAndReplyLetterId) {
            throw new RuntimeException("이미 답장한 편지에는 답장할 수 없습니다.");
        }
        Letter replyLetter = letterReplyRequest.toEntity(member, letter);
        letterRepository.save(replyLetter);
    }

    public List<LetterListResponse> findReplyList(Long id, Long memberId) {
        findMember(memberId);
        boolean existsByIdAndWriterId = letterRepository.existsByIdAndWriterId(id, memberId);
        if (!existsByIdAndWriterId) {
            throw new RuntimeException("존재하지 않은 편지입니다.");
        }
        return letterRepository.findAllByReplyLetterId(id).stream()
            .map(LetterListResponse::new)
            .toList();
    }

    @Transactional
    public LetterDetailResponse findReplyDetail(Long id, Long replyId, Long memberId) {
        Member member = findMember(memberId);
        boolean existsByIdAndWriterId = letterRepository.existsByIdAndWriterId(id, memberId);

        //답장할 때 본인 편지에 답장을 못하게 막았음 -> memberId와 replyId를 가지고 있는지 검증이 필요한지?

        Letter reply = letterRepository.findById(replyId).orElseThrow();
        if (!existsByIdAndWriterId) {
            throw new RuntimeException("존재하지 않은 편지입니다.");
        }
        boolean existsAlreadySavedLetter = reply.getMailBoxes().stream()
            .anyMatch(mailBox -> mailBox.getMember().equals(member)
                && mailBox.getLetter().equals(reply));
        if (!existsAlreadySavedLetter) {
            mailBoxRepository.save(new MailBox(reply, member));
        }
        return new LetterDetailResponse(reply);
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
