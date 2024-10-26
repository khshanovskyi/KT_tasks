package gof.strategy.services.strategy;

import gof.strategy.domain.ScheduledEvent;
import gof.strategy.domain.Status;
import gof.strategy.repository.ScheduledEventRepository;

public abstract class AbstractEventStrategy implements EventStrategy {

    private final ScheduledEventRepository eventRepository;

    public AbstractEventStrategy(ScheduledEventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    /**
     * Perform event logic
     */
    protected abstract void processImpl(ScheduledEvent event);

    @Override
    public ScheduledEvent process(ScheduledEvent event) {
        if (event.getStatus() == Status.FAILED && event.getVersion() > 10) {
            return terminateEvent(event);
        }
        return handleEvent(event);
    }

    private ScheduledEvent terminateEvent(ScheduledEvent event) {
        event.setStatus(Status.TERMINATED);
        eventRepository.save(event);
        return event;
    }

    private ScheduledEvent handleEvent(ScheduledEvent event) {
        try {
            event.setStatus(Status.ONGOING);
            processImpl(event);
        } catch (Exception e) {
            handleException(event, e);
        }
        eventRepository.save(event);
        return event;
    }

    private void handleException(ScheduledEvent event, Exception e) {
        event.setStatus(Status.FAILED);
        eventRepository.save(event);
        System.err.println("Failed to handle event. Error: " + e.getMessage());
    }
}
