package ru.practicum;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(StatsController.class)
public class StatsControllerTest {
    public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Autowired
    private MockMvc mvc;

    @MockBean
    private StatsService statsService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @SneakyThrows
    void shouldCreateHit_request_to_endpoint_and_save_data_test() {
        EndpointHitDto endpointHitDto = new EndpointHitDto(1L, "ewm-main-service", "/events/1", "1.1.0.1",
                LocalDateTime.of(2024, 6, 20, 01, 01, 01));

        when(statsService.createHit(any())).thenReturn(endpointHitDto);

        mvc.perform(post("/hit")
                        .content(objectMapper.writeValueAsString(endpointHitDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.app").value(endpointHitDto.getApp()))
                .andExpect(jsonPath("$.uri").value(endpointHitDto.getUri()))
                .andExpect(jsonPath("$.ip").value(endpointHitDto.getIp()))
                .andExpect(jsonPath("$.timestamp").value(endpointHitDto.getTimestamp().format(FORMATTER)));

        verify(statsService).createHit(any());
    }

    @Test
    @SneakyThrows
    void shouldGetStats_of_visit_with_unique_ip_and_uris() {
        List<ViewStatsDto> viewStatsDto = List.of(new ViewStatsDto("ewm-main-service", "/events/1", 1L));
        String start = "2024-06-20 01:01:01";
        String end = "2024-06-21 01:01:01";
        String uris = "/events/1";
        Boolean unique = true;

        when(statsService.getStats(any(), any(), anyList(), anyBoolean())).thenReturn(viewStatsDto);

        mvc.perform(get("/stats?start=" + start + "&end=" + end + "&uris=" + uris + "&unique=" + unique,
                        start, end, uris, unique)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].app").value(viewStatsDto.get(0).getApp()))
                .andExpect(jsonPath("$[0].uri").value(viewStatsDto.get(0).getUri()))
                .andExpect(jsonPath("$[0].hits").value(viewStatsDto.get(0).getHits()));

        verify(statsService).getStats(any(), any(), anyList(), anyBoolean());
    }
}
