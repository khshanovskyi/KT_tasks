package gof.strategy.services;

import gof.strategy.domain.EventResourceType;
import gof.strategy.domain.EventType;
import gof.strategy.domain.ScheduledEvent;
import gof.strategy.services.strategy.EventStrategy;
import org.apache.commons.lang3.tuple.Pair;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ScheduledEventContext {

    private final Map<Pair<EventType, EventResourceType>, EventStrategy> mapStrategies;

    public ScheduledEventContext(List<EventStrategy> strategies) {
        this.mapStrategies = new HashMap<>();
        strategies.forEach(strategy -> mapStrategies.put(strategy.getType(), strategy));
    }

    public void processEvent(ScheduledEvent event) {
        mapStrategies.get(Pair.of(event.getEventType(), event.getResourceType())).process(event);
    }

}
