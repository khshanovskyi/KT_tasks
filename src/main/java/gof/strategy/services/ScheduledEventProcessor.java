package gof.strategy.services;

import gof.strategy.domain.EventResourceType;
import gof.strategy.domain.EventType;
import gof.strategy.domain.ScheduledEvent;
import gof.strategy.services.strategy.EventStrategy;
import org.apache.commons.lang3.tuple.Pair;

import java.util.HashMap;
import java.util.Map;

public class ScheduledEventProcessor {

    private final Map<Pair<EventType, EventResourceType>, EventStrategy> strategies = new HashMap<>();

    public void registerStrategy(EventStrategy strategy) {
        strategies.put(strategy.getType(), strategy);
    }


    public void processEvent(ScheduledEvent event) {
        EventStrategy strategy = strategies.get(Pair.of(event.getEventType(), event.getResourceType()));
        if (strategy == null) {
            throw new RuntimeException("No strategy found for event type and resource type");
        }
        strategy.process(event);
    }
}
