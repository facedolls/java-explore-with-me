package ru.practicum;

import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class StatsClient extends BaseClient {
    private static final String API_PRFX_STATS = "/stats";
    private static final String API_PRFX_HIT = "/hit";
    private static final String DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    @Autowired
    public StatsClient(@Value("${stats-service.url}") String serverUrl, RestTemplateBuilder builder) {
        super(builder.uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl))
                .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                .build());
    }

    public ResponseEntity<Object> createHit(EndpointHitDto dto) {
        return post(API_PRFX_HIT, dto);
    }

    public List<ViewStatsDto> getViews(LocalDateTime start, LocalDateTime end, List<String> uriList) {
        Gson gson = new Gson();
        ResponseEntity<Object> viewsObject = getStats(start, end, uriList, true);
        String json = gson.toJson(viewsObject.getBody());
        ViewStatsDto[] viewsArray = gson.fromJson(json, ViewStatsDto[].class);
        return Arrays.asList(viewsArray);
    }

    public ResponseEntity<Object> getStats(LocalDateTime start, LocalDateTime end,
                                           List<String> uris, boolean unique) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATETIME_FORMAT);
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("start", start.format(formatter));
        parameters.put("end", end.format(formatter));
        if (uris != null && !uris.isEmpty()) {
            parameters.put("uris", uris);
        }
        parameters.put("unique", unique);

        if (parameters.containsKey("uris")) {
            return get(API_PRFX_STATS + "?start={start}&end={end}&uris={uris}&unique={unique}", parameters);
        } else {
            return get(API_PRFX_STATS + "?start={start}&end={end}&unique={unique}", parameters);
        }
    }
}
