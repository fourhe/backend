package com.letter2sea.be.letter.repository;

import com.letter2sea.be.letter.domain.Letter;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LetterRepository extends JpaRepository<Letter, Long> {

    List<Letter> findByWriterId(Long writerId);

    Letter findByIdAndWriterId(Long id, Long writerId);
}
