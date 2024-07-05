package ru.practicum.model.participationRequest;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import ru.practicum.model.User;
import ru.practicum.model.event.Event;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "participation_requests")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ParticipationRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "request_id")
    Long id;
    @ManyToOne
    @JoinColumn(name = "request_requester_id")
    User requester;
    @ManyToOne
    @JoinColumn(name = "request_event_id")
    Event event;
    @Enumerated(EnumType.STRING)
    @Column(name = "request_status")
    ParticipationState status;
    @Column(name = "request_created_date")
    LocalDateTime created;
}
