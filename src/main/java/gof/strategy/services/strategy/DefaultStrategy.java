package gof.strategy.services.strategy;

import gof.strategy.domain.EventResourceType;
import gof.strategy.domain.EventType;
import gof.strategy.domain.ScheduledEvent;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

public class DefaultStrategy implements EventStrategy {

    @Override
    public ScheduledEvent process(ScheduledEvent event) {
        throw new RuntimeException("Missing strategy for " + Pair.ofNonNull(event.getEventType(), event.getResourceType()));
    }

    @Override
    public Pair<EventType, EventResourceType> getType() {
        return ImmutablePair.nullPair();
    }
}
