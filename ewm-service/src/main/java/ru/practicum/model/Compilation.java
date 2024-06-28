package ru.practicum.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "compilations")
public class Compilation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String title;
    Boolean pinned;
    @ManyToMany
    @JoinTable(name = "events_compilations", inverseJoinColumns = @JoinColumn(name = "event_id")
            , joinColumns = @JoinColumn(name = "compilation_id"))
    List<Event> events;
}
