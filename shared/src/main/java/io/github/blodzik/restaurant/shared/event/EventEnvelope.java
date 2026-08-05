package io.github.blodzik.restaurant.shared.event;

public record EventEnvelope<T>(
        String eventId,
        String correlationId,
        int schemaVersion,
        T payload
        ) {}