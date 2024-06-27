DROP TABLE IF EXISTS users, categories, events, locations, compilations, events_compilations
    , participation_requests CASCADE;

CREATE TABLE IF NOT EXISTS users
(
    id    BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY NOT NULL,
    name  VARCHAR(255)                                    NOT NULL,
    email VARCHAR(255)                                    NOT NULL,
    CONSTRAINT email_unique UNIQUE (email)
);

CREATE TABLE IF NOT EXISTS categories
(
    id   BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY NOT NULL,
    name VARCHAR(255)                                    NOT NULL,
    CONSTRAINT name_unique UNIQUE (name)
);

CREATE TABLE IF NOT EXISTS locations
(
    id        BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY NOT NULL,
    latitude  FLOAT                                           NOT NULL,
    longitude FLOAT                                           NOT NULL
);

CREATE TABLE IF NOT EXISTS events
(
    id                 BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY NOT NULL,
    annotation         VARCHAR(2555)                                   NOT NULL,
    title              VARCHAR(255)                                    NOT NULL,
    description        VARCHAR(5555)                                   NOT NULL,
    category_id        BIGINT                                          NOT NULL,
    initiator_id       BIGINT                                          NOT NULL,
    location_id        BIGINT                                          NOT NULL,
    paid               BOOLEAN DEFAULT FALSE,
    created_on         TIMESTAMP WITHOUT TIME ZONE                     NOT NULL,
    published_on       TIMESTAMP WITHOUT TIME ZONE,
    date               TIMESTAMP WITHOUT TIME ZONE                     NOT NULL,
    state              VARCHAR(25)                                     NOT NULL,
    participant_limit  INTEGER DEFAULT 0,
    request_moderation BOOLEAN DEFAULT FALSE,
    CONSTRAINT fk_events_to_users FOREIGN KEY (initiator_id) REFERENCES users (id),
    CONSTRAINT fk_events_to_categories FOREIGN KEY (category_id) REFERENCES categories (id),
    CONSTRAINT fk_events_to_locations FOREIGN KEY (location_id) REFERENCES locations (id)
);

CREATE TABLE IF NOT EXISTS compilations
(
    id     BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY NOT NULL,
    title  VARCHAR(255) UNIQUE                             NOT NULL,
    pinned BOOLEAN                                         NOT NULL
);

CREATE TABLE IF NOT EXISTS events_compilations
(
    event_id       BIGINT,
    compilation_id BIGINT,
    PRIMARY KEY (compilation_id, event_id),
    CONSTRAINT fk_event_id FOREIGN KEY (event_id) REFERENCES events (id),
    CONSTRAINT fk_compilation_id FOREIGN KEY (compilation_id) REFERENCES compilations (id)
);

CREATE TABLE IF NOT EXISTS participation_requests
(
    id           BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY NOT NULL,
    request_id   BIGINT                                          NOT NULL,
    event_id     BIGINT                                          NOT NULL,
    created      TIMESTAMP WITHOUT TIME ZONE                     NOT NULL,
    status       VARCHAR(25)                                     NOT NULL,
    CONSTRAINT unique_requester_event UNIQUE (request_id, event_id),
    CONSTRAINT fk_attendee_request_to_events FOREIGN KEY (event_id) REFERENCES events (id),
    CONSTRAINT fk_attendee_request_to_users FOREIGN KEY (request_id) REFERENCES users (id)
);
