package gof.strategy.services;

import gof.strategy.domain.ScheduledEvent;
import gof.strategy.services.strategy.EventStrategy;
import org.apache.commons.lang3.tuple.Pair;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public class ScheduledEventProcessor {

    private HashMap<Pair, EventStrategy> eventStrategyMap = new HashMap<>();

    public ScheduledEventProcessor(List<EventStrategy> eventStrategies) {
        for (EventStrategy eventStrategy : eventStrategies) {
            eventStrategyMap.put(eventStrategy.getType(), eventStrategy);
        }
    }

    public void processEvent(ScheduledEvent event) {
        Optional.ofNullable(eventStrategyMap.get(Pair.of(event.getEventType(), event.getResourceType())))
                .ifPresent(eventStrategy -> eventStrategy.process(event));
    }

}
