package org.ssu.standings.event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class ContestUpdatesEventProducer {
    private ApplicationEventPublisher publisher;

    @Autowired
    public ContestUpdatesEventProducer(ApplicationEventPublisher publisher) {
        this.publisher = publisher;
    }

    public void publishEvent(ContestUpdates updates) {
        publisher.publishEvent(updates);
    }
}
