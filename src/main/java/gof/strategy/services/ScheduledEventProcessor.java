package gof.strategy.services;

import gof.strategy.domain.EventResourceType;
import gof.strategy.domain.EventType;
import gof.strategy.domain.ScheduledEvent;
import gof.strategy.services.strategy.DefaultStrategy;
import gof.strategy.services.strategy.EventStrategy;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ScheduledEventProcessor {

    private static final EventStrategy DEFAULT_STRATEGY = new DefaultStrategy();

    private final Map<Pair<EventType, EventResourceType>, EventStrategy> strategies;

    public ScheduledEventProcessor(final List<? extends EventStrategy> strategies) {
        this.strategies = strategies.stream()
                .collect(Collectors.toMap(EventStrategy::getType, Function.identity()));
    }

    public void processEvent(ScheduledEvent event) {
        final var strategyType = Pair.of(event.getEventType(), event.getResourceType());

        strategies.getOrDefault(strategyType, DEFAULT_STRATEGY)
                .process(event);
    }

}
