package ru.practicum.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.model.Compilation;

import java.util.List;

public interface CompilationRepository extends JpaRepository<Compilation, Long> {
    @Query("SELECT com FROM Compilation AS com " +
            "WHERE (:pinned IS NULL OR com.pinned = :pinned)")
    List<Compilation> findAllByPinned(@Param("pinned") Boolean pinned, Pageable pageable);
}
