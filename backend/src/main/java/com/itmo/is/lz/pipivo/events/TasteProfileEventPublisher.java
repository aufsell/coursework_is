package com.itmo.is.lz.pipivo.events;

public interface TasteProfileEventPublisher {
    void publish(TasteProfileEvent event);
}
