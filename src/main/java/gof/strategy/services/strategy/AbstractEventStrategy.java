package gof.strategy.services.strategy;

import gof.strategy.domain.ScheduledEvent;
import gof.strategy.domain.Status;
import gof.strategy.repository.ScheduledEventRepository;

public abstract class AbstractEventStrategy implements EventStrategy {

    protected final ScheduledEventRepository eventRepository;

    protected AbstractEventStrategy(ScheduledEventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    protected abstract ScheduledEvent processImpl(ScheduledEvent scheduledEvent);

    @Override
    public ScheduledEvent process(ScheduledEvent event) {
        ScheduledEvent preProcessedEvent = preProcess(event);
        ScheduledEvent processedEvent = null;
        try {
            processedEvent = processImpl(preProcessedEvent);
        }catch (Exception exception){
            event.setStatus(Status.FAILED);
        }
        return processedEvent;
    }

    protected final ScheduledEvent preProcess(ScheduledEvent event) {
        try {
            if (event.getStatus().equals(Status.FAILED) && event.getVersion() > 10) {
                event.setStatus(Status.TERMINATED);
            } else {
                event.setStatus(Status.ONGOING);
            }
        } catch (Exception e) {
            // NPE getStatus() from client
            System.out.println(e);
        }

        return eventRepository.save(event);
    }
}
