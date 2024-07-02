DROP TABLE IF EXISTS users CASCADE;
DROP TABLE IF EXISTS categories CASCADE;
DROP TABLE IF EXISTS locations CASCADE;
DROP TABLE IF EXISTS events CASCADE;
DROP TABLE IF EXISTS participation_requests CASCADE;
DROP TABLE IF EXISTS compilations CASCADE;
DROP TABLE IF EXISTS compilations_events CASCADE;

CREATE TABLE IF NOT EXISTS users
(
    id    BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name  VARCHAR(255)        NOT NULL,
    email VARCHAR(254) UNIQUE NOT NULL,
    CONSTRAINT unique_user_email UNIQUE (email)
);

CREATE TABLE IF NOT EXISTS categories
(
    id   BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name VARCHAR(55) UNIQUE NOT NULL,

    CONSTRAINT unique_category_name UNIQUE (name)
);

CREATE TABLE IF NOT EXISTS locations
(
    location_id  BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    location_lat FLOAT NOT NULL,
    location_lon FLOAT NOT NULL
);

CREATE TABLE IF NOT EXISTS events
(
    event_id                 BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,

    event_annotation         VARCHAR(2000)               NOT NULL,
    event_title              VARCHAR(155)                NOT NULL,
    event_description        VARCHAR(7000)               NOT NULL,

    event_category_id        BIGINT                      NOT NULL,
    event_initiator_id       BIGINT                      NOT NULL,
    event_location_id        BIGINT                      NOT NULL,

    event_created_on         TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    event_published_on       TIMESTAMP WITHOUT TIME ZONE,
    event_date               TIMESTAMP WITHOUT TIME ZONE NOT NULL,

    event_state              VARCHAR                     NOT NULL,
    event_participant_limit  INTEGER default 0,
    event_paid               BOOLEAN DEFAULT false,
    event_request_moderation BOOLEAN default false,

    CONSTRAINT fk_events_to_users FOREIGN KEY (event_initiator_id) REFERENCES users (id),
    CONSTRAINT fk_events_to_categories FOREIGN KEY (event_category_id) REFERENCES categories (id),
    CONSTRAINT fk_events_to_locations FOREIGN KEY (event_location_id) REFERENCES locations (location_id)
);

CREATE TABLE IF NOT EXISTS participation_requests
(
    request_id           BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    request_requester_id BIGINT                      NOT NULL,
    request_event_id     BIGINT                      NOT NULL,
    request_status       VARCHAR(15)                 NOT NULL,
    request_created_date TIMESTAMP WITHOUT TIME ZONE NOT NULL,

    CONSTRAINT unique_requester_event UNIQUE (request_requester_id, request_event_id),
    CONSTRAINT fk_attendee_request_to_users FOREIGN KEY (request_requester_id) REFERENCES users (id),
    CONSTRAINT fk_attendee_request_to_events FOREIGN KEY (request_event_id) REFERENCES events (event_id)
);

CREATE TABLE IF NOT EXISTS compilations
(
    compilation_id        BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    compilation_title     VARCHAR(50) UNIQUE NOT NULL,
    compilation_is_pinned BOOLEAN            NOT NULL
);

CREATE TABLE IF NOT EXISTS compilations_events
(
    compilation_id BIGINT,
    event_id       BIGINT,

    PRIMARY KEY (compilation_id, event_id),
    CONSTRAINT fk_compilation_id FOREIGN KEY (compilation_id) REFERENCES compilations (compilation_id),
    CONSTRAINT fk_event_id FOREIGN KEY (event_id) REFERENCES events (event_id)
);


