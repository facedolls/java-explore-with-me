package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.model.ParticipationRequest;
import ru.practicum.model.ParticipationState;

import java.util.List;

public interface ParticipationRequestRepository extends JpaRepository<ParticipationRequest, Long> {
    @Query("SELECT COUNT(req) FROM ParticipationRequest req WHERE req.event.id = :eventId AND req.status = :status")
    Long getCountByEventIdAndStatus(@Param("eventId") Long eventId, @Param("status") ParticipationState status);

    List<ParticipationRequest> findAllByEventIdInAndStatus(List<Long> eventIds, ParticipationState status);

    List<ParticipationRequest> findAllByRequesterId(Long requesterId);

    List<ParticipationRequest> findAllByEventId(Long eventId);

    List<ParticipationRequest> findAllByIdIn(List<Long> ids);
}
