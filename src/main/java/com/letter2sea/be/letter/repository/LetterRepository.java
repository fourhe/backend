package com.letter2sea.be.letter.repository;

import com.letter2sea.be.letter.domain.Letter;
import com.letter2sea.be.member.Member;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface LetterRepository extends JpaRepository<Letter, Long> {

    List<Letter> findAllByWriterIdAndReplyLetterIdIsNull(@Param("writerId") Long writerId);

    List<Letter> findAllByReplyLetterId(Long id);

    Optional<Letter> findByIdAndWriterId(Long id, Long writerId);

    @Query(value = "select * from letter as l where l.id = :id", nativeQuery = true)
    Optional<Letter> findByIdIgnoreDeletedAt(@Param("id") Long id);

    boolean existsByWriterIdAndReplyLetterId(Long writerId, Long replyLetterId);

    //작성자가 아니면서, replyLetterId가 null인
    List<Letter> findAllByWriterNotAndReplyLetterIdIsNull(@Param("writer") Member writer);

    boolean existsByIdAndWriterId(Long id, Long writerId);

    //작성자 아니면서 안읽은 메시지가 있으면서 replyLetterId가 null인
    //추후 동적 쿼리 또는 한번에 정렬해서 가져오는 쿼리로 변경 예정
    @Query("SELECT l FROM Letter l WHERE l.replyLetterId is null AND l.writer <> :writer AND (:ids IS NULL OR l.id NOT IN :ids)")
    List<Letter> findAllByWriterNotAndIdNotIn(@Param("writer") Member writer, @Param("ids") List<Long> ids);

//    @Query("select l from Letter l where l.id not in (:id)")
//    List<Letter> findAllByIdNotIn(@Param("id") List<Long> ids);

}
