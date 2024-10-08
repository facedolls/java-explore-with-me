package ru.practicum.model.event;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import ru.practicum.model.Category;
import ru.practicum.model.Location;
import ru.practicum.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "events")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "event_id")
    Long id;
    @Column(name = "event_annotation")
    String annotation;
    @Column(name = "event_title")
    String title;
    @Column(name = "event_description")
    String description;
    @ManyToOne
    @JoinColumn(name = "event_category_id")
    Category category;
    @ManyToOne
    @ToString.Exclude
    @JoinColumn(name = "event_initiator_id")
    User initiator;
    @OneToOne
    @ToString.Exclude
    @JoinColumn(name = "event_location_id")
    Location location;
    @Column(name = "event_created_on")
    LocalDateTime createdOn;
    @Column(name = "event_published_on")
    LocalDateTime publishedOn;
    @Column(name = "event_date")
    LocalDateTime eventDate;
    @Enumerated(EnumType.STRING)
    @Column(name = "event_state")
    EventState state;
    @Column(name = "event_paid")
    Boolean paid;
    @Column(name = "event_participant_limit")
    Integer participantLimit;
    @Column(name = "event_request_moderation")
    Boolean requestModeration;
}
