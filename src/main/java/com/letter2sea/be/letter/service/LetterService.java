package com.letter2sea.be.letter.service;

import static com.letter2sea.be.exception.type.LetterExceptionType.LETTER_ALREADY_DELETED;
import static com.letter2sea.be.exception.type.LetterExceptionType.LETTER_ALREADY_READ;
import static com.letter2sea.be.exception.type.LetterExceptionType.LETTER_ALREADY_REPLY;
import static com.letter2sea.be.exception.type.LetterExceptionType.LETTER_NOT_FOUND;
import static com.letter2sea.be.exception.type.LetterExceptionType.LETTER_NOT_READ;

import com.letter2sea.be.common.util.MailSender;
import com.letter2sea.be.exception.Letter2SeaException;
import com.letter2sea.be.exception.type.MailBoxExceptionType;
import com.letter2sea.be.letter.domain.Letter;
import com.letter2sea.be.letter.dto.request.LetterCreateRequest;
import com.letter2sea.be.letter.dto.request.ReplyCreateRequest;
import com.letter2sea.be.letter.dto.response.LetterDetailResponse;
import com.letter2sea.be.letter.dto.response.LetterListResponse;
import com.letter2sea.be.letter.dto.response.LetterPaginatedResponse;
import com.letter2sea.be.letter.dto.response.ReplyListResponse;
import com.letter2sea.be.letter.repository.LetterRepository;
import com.letter2sea.be.mailbox.MailBoxRepository;
import com.letter2sea.be.mailbox.domain.MailBox;
import com.letter2sea.be.member.Member;
import com.letter2sea.be.member.repository.MemberRepository;
import com.letter2sea.be.trash.TrashRepository;
import com.letter2sea.be.trash.domain.Trash;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LetterService {

    private static final int MAX_PAGE_SIZE = 1000;

    private static final Random random = new SecureRandom();
    private final LetterRepository letterRepository;
    private final MemberRepository memberRepository;
    private final MailBoxRepository mailBoxRepository;
    private final TrashRepository trashRepository;
    private final MailSender mailSender;

    @Transactional
    public void create(Long writerId, LetterCreateRequest letterCreateRequest) {
        Member member = findMember(writerId);
        Letter letter = letterCreateRequest.toEntity(member);
        letterRepository.save(letter);
        mailBoxRepository.save(new MailBox(letter, member));
    }

//    //일반 list 반환
//    public List<LetterListResponse> findList(Long writerId) {
//        List<Letter> letterList = letterRepository.findAllByWriterIdAndReplyLetterIdIsNull(writerId);
//
//        List<LetterListResponse> result = new ArrayList<>();
//
//        for (Letter letter : letterList) {
//            List<Letter> replyList = letterRepository.findAllByReplyLetterId(
//                letter.getId());
//
//            boolean hasNewReply = replyList.stream()
//                .anyMatch(
//                    reply -> !mailBoxRepository.existsByLetterIdAndMemberId(reply.getId(), writerId));
//
//            result.add(new LetterListResponse(letter, hasNewReply));
//        }
//        return result;
//    }

    public LetterPaginatedResponse findAll(Pageable pageable, Long writerId) {
        Pageable pageRequest = exchangePageRequest(pageable);

        Page<Letter> findList = letterRepository.findAllByWriterIdAndReplyLetterIdIsNull(
            writerId, pageRequest);

        List<Letter> contents = findList.getContent();

        List<LetterListResponse> result = new ArrayList<>();

        for (Letter content : contents) {
            List<Letter> replyList = letterRepository.findAllByReplyLetterId(
                content.getId());

            boolean hasNewReply = replyList.stream()
                .anyMatch(reply -> !mailBoxRepository.existsByLetterIdAndMemberId(reply.getId(),
                    writerId));

            result.add(new LetterListResponse(content, hasNewReply));
        }

        return new LetterPaginatedResponse(pageable.getPageNumber(), contents.size(),
            findList.getTotalPages(), result);

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
            List<Letter> unreadLetters = letterRepository.findAllByWriterNotAndReplyLetterIdIsNull(member);
            if (unreadLetters.isEmpty()) {
                return null;
            }
            return unreadLetters.get(random.nextInt(unreadLetters.size())).getId();
            //읽을 편지가 없다면 에러메시지를 응답값으로 전달 예정
        }
        List<Letter> unReadLetters = letterRepository.findAllByWriterNotAndIdNotIn(
            member, readLetters);
        if (unReadLetters.isEmpty()) {
            return null;
        }
        return unReadLetters.get(random.nextInt(unReadLetters.size())).getId();
    }

    //랜덤줍기 전용
    @Transactional
    public LetterDetailResponse read(Long id, Long memberId) {
        Member member = findMember(memberId);
        Letter letter = letterRepository.findById(id).orElseThrow();

        Long replyLetterId = letter.getReplyLetterId();
//        boolean isValidReply = letterRepository.existsByIdAndWriterId(letter.getReplyLetterId(), memberId);
        boolean existsByIdAndWriterId = letterRepository.existsByIdAndWriterId(id, memberId);
        boolean existAlreadyReadLetter = member.getMailBoxes().stream()
            .anyMatch(mailBox -> mailBox.getLetter().getId().equals(id));

        if (replyLetterId != null || existsByIdAndWriterId || existAlreadyReadLetter) {
            throw new Letter2SeaException(LETTER_ALREADY_READ);
        }
        mailBoxRepository.save(new MailBox(letter, member));
        return new LetterDetailResponse(letter);
    }

    @Transactional
    public void reply(Long id, Long writerId, ReplyCreateRequest letterReplyRequest) {
        Member member = findMember(writerId);
        Letter letter = letterRepository.findById(id)
            .orElseThrow(() -> new Letter2SeaException(LETTER_NOT_FOUND));

        validateLetterToReply(letter, id, writerId);

        Letter replyLetter = letterReplyRequest.toEntity(member, letter);
        letterRepository.save(replyLetter);

        sendEmailNotification(letter);
    }

    public List<ReplyListResponse> findReplyList(Long id, Long memberId) {
        Member member = findMember(memberId);
        boolean existsByIdAndWriterId = letterRepository.existsByIdAndWriterId(id, memberId);
        if (!existsByIdAndWriterId) {
            throw new Letter2SeaException(LETTER_NOT_FOUND);
        }

        return letterRepository.findAllByReplyLetterId(id).stream()
            .map(l -> new ReplyListResponse(l, findReply(l, member)))
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

    @Transactional
    public void delete(Long id, Long memberId) {
        Member member = findMember(memberId);
        Letter letter = letterRepository.findById(id).orElseThrow();

        mailBoxRepository.findByLetterIdAndMemberId(id, memberId);
        boolean existsByLetterIdAndMemberId = trashRepository.existsByLetterIdAndMemberId(id, memberId);

        if (existsByLetterIdAndMemberId) {
            throw new Letter2SeaException(LETTER_ALREADY_DELETED);
        }
        letter.updateDeletedAt();
        trashRepository.save(new Trash(letter, member));
    }

    @Transactional
    public void thanks(Long id, Long memberId) {
        Member member = findMember(memberId);

        MailBox replyMailBox = mailBoxRepository.findByLetterIdAndMember(id, member)
            .orElseThrow(() -> new Letter2SeaException(LETTER_NOT_READ));

        if (replyMailBox.isThanked()) {
            throw new Letter2SeaException(MailBoxExceptionType.MAILBOX_ALREADY_THANKED);
        }

        replyMailBox.thanks();
    }

    private Pageable exchangePageRequest(Pageable pageable) {
        int pageSize = pageable.getPageSize();

        if (pageSize <= MAX_PAGE_SIZE) {
            return pageable;
        }
        return PageRequest.of(pageable.getPageNumber(), MAX_PAGE_SIZE);
    }

    private Member findMember(Long writerId) {
        return memberRepository.findById(writerId).orElseThrow();
    }

    private void validateLetterToReply(Letter letter, Long letterId, Long userId) {
        if (letter.getDeletedAt() != null) {
            throw new Letter2SeaException(LETTER_NOT_FOUND);
        }

        // 자신이 작성한 편지인 경우
        boolean existsByIdAndWriterId = letterRepository.existsByIdAndWriterId(letterId, userId);
        if (existsByIdAndWriterId) {
            throw new Letter2SeaException(LETTER_ALREADY_REPLY);
        }

        // 이미 답장한 편지인 경우 또는 답장에 다시 답장하려고 하는 경우
        boolean existsByWriterIdAndReplyLetterId = letterRepository
            .existsByWriterIdAndReplyLetterId(userId, letterId);
        if (existsByWriterIdAndReplyLetterId || letter.getReplyLetterId() != null) {
            throw new Letter2SeaException(LETTER_ALREADY_REPLY);
        }
    }

    private MailBox findReply(Letter reply, Member member) {
        return mailBoxRepository.findByLetterIdAndMember(
            reply.getId(), member).orElse(null);
    }

    private void sendEmailNotification(Letter letter) {
        Member writer = letter.getWriter();
        if (writer.getEmail() == null || writer.getEmail().isBlank() || !writer.isNotificationEnabled()) return;
        mailSender.send(letter.getTitle(), writer.getEmail());
    }


    //랜덤 줍기 구현 중 리스트를 응답으로 주는 메서드 임시 구현
//    public List<LetterListResponse> randomTest(Long memberId) {
//        Member member = findMember(memberId);
//
//        List<Long> readLetters = member.getMailBoxes().stream()
//            .map(mailBox -> mailBox.getLetter().getId())
//            .toList();
//
//        List<Letter> unReadLetters = letterRepository.findAllByWriterNotAndIdNotIn(
//            member, readLetters);
//
//        return unReadLetters.stream().map(LetterListResponse::new).toList();
//    }
}
