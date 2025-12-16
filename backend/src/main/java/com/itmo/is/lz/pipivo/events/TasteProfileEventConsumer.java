package com.itmo.is.lz.pipivo.events;

import com.itmo.is.lz.pipivo.service.TasteProfileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class TasteProfileEventConsumer {

    private final TasteProfileService tasteProfileService;

    @KafkaListener(topics = "taste-profile.events", groupId = "taste-profile")
    public void handle(TasteProfileEvent event) {
        log.info("Received taste profile event: {}", event);

        if (event.type() == TasteProfileEventType.BEER_LIKED) {
            tasteProfileService.updateTasteProfileByFavourite(event.userId(), event.beerId());
            return;
        }

        if (event.type() == TasteProfileEventType.BEER_UNLIKED) {
            tasteProfileService.onBeerUnliked(event.userId(), event.beerId());
        }
    }
}
