package com.itmo.is.lz.pipivo.events;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class KafkaTasteProfileEventPublisher implements TasteProfileEventPublisher {

    private static final String TOPIC = "taste-profile.events";
    private final KafkaTemplate<String, TasteProfileEvent> kafkaTemplate;

    @Override
    public void publish(TasteProfileEvent event) {
        kafkaTemplate.send(TOPIC, String.valueOf(event.userId()), event);
    }
}

