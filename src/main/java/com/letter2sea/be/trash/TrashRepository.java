package com.letter2sea.be.trash;

import com.letter2sea.be.trash.domain.Trash;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TrashRepository extends JpaRepository<Trash, Long> {

    List<Trash> findAllByMemberId(Long memberId);

}
