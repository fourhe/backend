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

    List<Letter> findAllByWriterId(Long writerId);

    Optional<Letter> findByIdAndWriterId(Long id, Long writerId);

    List<Letter> findAllByWriterNot(Member writer);

    //추후 동적 쿼리 또는 한번에 정렬해서 가져오는 쿼리로 변경 예정
    @Query("SELECT l FROM Letter l WHERE l.writer <> :writer AND (:ids IS NULL OR l.id NOT IN :ids)")
    List<Letter> findAllByWriterNotAndIdNotIn(@Param("writer") Member writer, @Param("ids") List<Long> ids);
//    @Query("select l from Letter l where l.id not in (:id)")
//    List<Letter> findAllByIdNotIn(@Param("id") List<Long> ids);

}
