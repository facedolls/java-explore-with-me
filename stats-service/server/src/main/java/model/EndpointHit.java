package model;

import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDate;

@Entity
@NoArgsConstructor
public class EndpointHit {
    @Id
    @GeneratedValue
    private Long id;
    private String app;
    private String uri;
    private String ip;
    private LocalDate timestamp;
}
