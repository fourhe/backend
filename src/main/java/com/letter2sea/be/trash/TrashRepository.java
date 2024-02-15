package com.letter2sea.be.trash;

import com.letter2sea.be.trash.domain.Trash;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TrashRepository extends JpaRepository<Trash, Long> {
    int countAllByMemberId(Long memberId);

    @Query("select t from Trash t join Letter l on t.letterId = l.id "
        + "where t.member.id = :memberId and l.replyLetterId is null")
    Page<Trash> findAllByLetterTrash(@Param("memberId") Long memberId, Pageable pageable);

    @Query("select t from Trash t join Letter l on t.letterId = l.id "
        + "where t.member.id = :memberId and l.replyLetterId is not null")
    Page<Trash> findAllByReplyTrash(@Param("memberId") Long memberId, Pageable pageable);

//    Page<Trash> findAllByMemberId(Long memberId, Pageable pageable)

    Optional<Trash> findByIdAndMemberId(Long id, Long memberId);

    boolean existsByIdAndMemberId(Long id, Long memberId);

    void deleteByIdAndMemberId(Long id, Long memberId);

    boolean existsByLetterIdAndMemberId(Long id, Long memberId);
}
