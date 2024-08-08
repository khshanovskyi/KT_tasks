package gof.strategy.services;

import gof.strategy.domain.EventResourceType;
import gof.strategy.domain.EventType;
import gof.strategy.domain.ScheduledEvent;
import gof.strategy.services.strategy.EventStrategy;
import org.apache.commons.lang3.tuple.Pair;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class ScheduledEventProcessor {

    private final Map<Pair<EventType, EventResourceType>, EventStrategy> eventStrategyMap = new HashMap<>();

    public ScheduledEventProcessor(final List<EventStrategy> strategies) {
        registerStrategies(strategies);
    }

    public void processEvent(ScheduledEvent event) {
        Optional.ofNullable(eventStrategyMap.get(Pair.of(event.getEventType(), event.getResourceType())))
                .ifPresentOrElse(
                        eventStrategy -> eventStrategy.process(event),
                        () -> {
                            throw new RuntimeException("Cannot find a strategy for eventType: %s, eventResourceType: %s".formatted(event.getEventType(), event.getResourceType()));
                        });
    }

    private void registerStrategies(final List<EventStrategy> strategies) {
        strategies.forEach(strategy -> eventStrategyMap.put(strategy.getType(), strategy));
    }
}
