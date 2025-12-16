package com.itmo.is.lz.pipivo.events;

import java.time.Instant;
import java.util.UUID;

public record TasteProfileEvent(
        UUID eventId,
        Instant occurredAt,
        TasteProfileEventType type,
        Long userId,
        Long beerId
) {
    public static TasteProfileEvent liked(Long userId, Long beerId) {
        return new TasteProfileEvent(UUID.randomUUID(), Instant.now(), TasteProfileEventType.BEER_LIKED, userId, beerId);
    }

    public static TasteProfileEvent unliked(Long userId, Long beerId) {
        return new TasteProfileEvent(UUID.randomUUID(), Instant.now(), TasteProfileEventType.BEER_UNLIKED, userId, beerId);
    }
}

