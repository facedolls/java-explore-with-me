package ru.practicum.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "events")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String annotation;
    String title;
    String description;
    @ManyToOne
    @JoinColumn(name = "category_id")
    Category category;
    @ManyToOne
    @JoinColumn(name = "initiator_id")
    User initiator;
    @ManyToOne
    @JoinColumn(name = "location_id")
    Location location;
    Boolean paid;
    @Column(name = "created_on")
    LocalDateTime createdOn;
    @Column(name = "published_on")
    LocalDateTime publishedOn;
    LocalDateTime date;
    EventState state;
    @Column(name = "participant_limit")
    Integer participantLimit;
    @Column(name = "request_moderation")
    Boolean requestModeration;
}
