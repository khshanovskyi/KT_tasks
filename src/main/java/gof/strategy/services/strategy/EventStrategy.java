package gof.strategy.services.strategy;

import gof.strategy.domain.ScheduledEvent;

public interface EventStrategy {

    ScheduledEvent process(ScheduledEvent event);

//    Pair<EventType, EventResourceType> getType();

}
