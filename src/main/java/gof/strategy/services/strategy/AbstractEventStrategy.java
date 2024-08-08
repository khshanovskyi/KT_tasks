package gof.strategy.services.strategy;

import gof.strategy.domain.ScheduledEvent;
import gof.strategy.domain.Status;
import gof.strategy.repository.ScheduledEventRepository;

public abstract class AbstractEventStrategy implements EventStrategy {

    private static final int PROCESSING_FAILURE_THRESHOLD = 10;

    protected final ScheduledEventRepository eventRepository = new ScheduledEventRepository();

    @Override
    public ScheduledEvent process(ScheduledEvent event) {
        event.setStatus(getNextStatus(event));
        eventRepository.save(event);
        return event;
    }

    private static Status getNextStatus(ScheduledEvent event) {
        return isEventFailedTooManyTimes(event)
                ? Status.TERMINATED
                : Status.ONGOING;
    }

    private static boolean isEventFailedTooManyTimes(ScheduledEvent event) {
        return event.getStatus() == Status.FAILED
               && event.getVersion() > PROCESSING_FAILURE_THRESHOLD;
    }
}
