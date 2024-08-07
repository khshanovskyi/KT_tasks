package gof.strategy.services.strategy;

import gof.strategy.domain.EventResourceType;
import gof.strategy.domain.EventType;
import gof.strategy.domain.ScheduledEvent;
import org.apache.commons.lang3.tuple.Pair;

public interface EventStrategy {

    ScheduledEvent process(ScheduledEvent event);

    Pair<EventType, EventResourceType> getType();

}
