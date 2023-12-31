package com.letter2sea.be.trash;

import com.letter2sea.be.trash.domain.Trash;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TrashRepository extends JpaRepository<Trash, Long> {

}
