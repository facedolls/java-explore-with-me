package ru.practicum;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = StatsController.class)
@FieldDefaults(level = AccessLevel.PRIVATE)
class StatsControllerTest {
    @MockBean
    StatsService statService;
    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;

    @Test
    void getViewStats() throws Exception {
        ViewStatsDto viewStatDto = DataForTests.testViewStatsDto();
        List<ViewStatsDto> viewStatDtoList = List.of(viewStatDto);

        when(statService.getViewStats(any(ViewStatsRequestDto.class)))
                .thenReturn(viewStatDtoList);

        mockMvc.perform(get("/stats").characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("start", "2024-01-01 00:00:00")
                        .param("end", "2026-01-01 00:00:00"))
                .andExpect(status().isOk());
    }

    @Test
    void createHit() throws Exception {
        EndpointHitDto hitDto = DataForTests.testEndpointHitDto();

        when(statService.createHit(any(EndpointHitDto.class)))
                .thenReturn(hitDto);
        mockMvc.perform(post("/hit")
                        .content(objectMapper.writeValueAsString(hitDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(201));
    }
}
