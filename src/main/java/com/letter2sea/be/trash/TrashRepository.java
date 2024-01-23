package com.letter2sea.be.trash;

import com.letter2sea.be.trash.domain.Trash;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TrashRepository extends JpaRepository<Trash, Long> {

//    List<Trash> findAllByMemberId(Long memberId);

    Page<Trash> findAllByMemberId(Long memberId, Pageable pageable);

    Optional<Trash> findByIdAndMemberId(Long id, Long memberId);

    boolean existsByIdAndMemberId(Long id, Long memberId);

    void deleteByIdAndMemberId(Long id, Long memberId);

    boolean existsByLetterIdAndMemberId(Long id, Long memberId);
}
