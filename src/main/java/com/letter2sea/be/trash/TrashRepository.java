package com.letter2sea.be.trash;

import com.letter2sea.be.trash.domain.Trash;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TrashRepository extends JpaRepository<Trash, Long> {

    List<Trash> findAllByMemberId(Long memberId);

    Optional<Trash> findByIdAndMemberId(Long id, Long memberId);

    boolean existsByIdAndMemberId(Long id, Long memberId);

    void deleteByIdAndMemberId(Long id, Long memberId);
}
