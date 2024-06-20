package ru.practicum;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class ViewStatsDto {
    private String app;
    private String uri;
    private Long hits;
}
