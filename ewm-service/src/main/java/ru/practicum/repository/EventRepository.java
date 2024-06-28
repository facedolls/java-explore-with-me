package ru.practicum.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.model.Event;
import ru.practicum.model.EventState;

import java.time.LocalDateTime;
import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {
    @Query("SELECT evt FROM Event evt " +
            "WHERE ((:users) IS NULL OR evt.initiator.id IN :users) " +
            "AND ((:states) IS NULL OR evt.state IN :states) " +
            "AND ((:categories) IS NULL OR evt.category.id IN :categories) " +
            "AND (evt.date BETWEEN :rangeStart AND :rangeEnd)")
    List<Event> findAllByAdmin(@Param("users") List<Long> users,
                               @Param("states") List<EventState> states,
                               @Param("categories") List<Long> categories,
                               @Param("rangeStart") LocalDateTime rangeStart,
                               @Param("rangeEnd") LocalDateTime rangeEnd,
                               Pageable pageable);

    List<Event> findAllByInitiatorId(Long initiatorId, Pageable pageable);

    @Query("SELECT evt FROM Event evt " +
            "WHERE (evt.state = 'PUBLISHED') " +
            "AND (LOWER(evt.annotation) LIKE LOWER(CONCAT('%', :text, '%')) OR LOWER(evt.description) LIKE LOWER(CONCAT('%', :text, '%'))) " +
            "AND ((:categories) IS NULL OR evt.category.id IN :categories) " +
            "AND ((:paid) IS NULL OR evt.paid = :paid) " +
            "AND (evt.date BETWEEN :rangeStart AND :rangeEnd)")
    List<Event> getAllPublicByParams(@Param("text") String text,
                                     @Param("categories") List<Long> categories,
                                     @Param("paid") Boolean paid,
                                     @Param("rangeStart") LocalDateTime rangeStart,
                                     @Param("rangeEnd") LocalDateTime rangeEnd,
                                     Pageable pageable);
}
