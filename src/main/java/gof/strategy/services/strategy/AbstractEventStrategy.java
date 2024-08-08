package gof.strategy.services.strategy;

import gof.strategy.domain.EventResourceType;
import gof.strategy.domain.EventType;
import gof.strategy.domain.ScheduledEvent;
import gof.strategy.domain.Status;
import org.apache.commons.lang3.tuple.Pair;

public abstract class AbstractEventStrategy implements EventStrategy {
    @Override
    public ScheduledEvent process(ScheduledEvent event) {
        if (event.getStatus() == Status.FAILED && event.getVersion()>10) {
            event.setStatus(Status.TERMINATED);
        }

        else {
            event.setStatus(Status.ONGOING);
        }

        return event;
    }
}
