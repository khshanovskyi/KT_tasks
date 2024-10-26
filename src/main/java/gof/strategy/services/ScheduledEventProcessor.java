package gof.strategy.services;

import gof.strategy.domain.ScheduledEvent;
import gof.strategy.services.strategy.EventStrategy;

import java.util.List;

public class ScheduledEventProcessor {

    private final List<EventStrategy> eventStrategies;

    public ScheduledEventProcessor(List<EventStrategy> eventStrategies) {
        this.eventStrategies = eventStrategies;
    }

    public void processEvent(ScheduledEvent event) {
        EventStrategy strategy = getEventStrategy(event);
        strategy.process(event);
    }

    protected EventStrategy getEventStrategy(ScheduledEvent event) {
        return eventStrategies.stream()
                .filter(strategy -> {
                    var typePair = strategy.getType();
                    return typePair.getLeft().equals(event.getEventType())
                            && typePair.getRight().equals(event.getResourceType());
                })
                .findFirst()
                .orElseThrow();
    }

}
