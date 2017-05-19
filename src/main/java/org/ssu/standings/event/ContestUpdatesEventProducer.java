package org.ssu.standings.event;

import org.springframework.beans.factory.annotation.*;
import org.springframework.context.*;
import org.springframework.stereotype.*;

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
