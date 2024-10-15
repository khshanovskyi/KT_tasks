package gof.strategy.services.strategy;

import gof.strategy.domain.ScheduledEvent;
import gof.strategy.domain.Status;
import gof.strategy.repository.ScheduledEventRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class AbstractEventStrategy implements EventStrategy {
    protected final ScheduledEventRepository eventRepository;

    @Override
    public ScheduledEvent process(ScheduledEvent event) {
        if (isProcessed(event)) {
            ignore(event);
        } else if (toBeTerminated(event)) {
            terminate(event);
        } else {
            startProcessing(event);
            try {
                executeEventJob(event);
                complete(event);
            } catch (Exception ex) {
                markAsFailed(event, ex.getMessage());
            }
        }
        return event;
    }

    protected boolean isProcessed(final ScheduledEvent event) {
        return event.getStatus() == Status.COMPLETED
               || event.getStatus() == Status.TERMINATED
               || event.getStatus() == Status.EXCLUDED;
    }

    protected void ignore(final ScheduledEvent event) {
        // Really do nothing, ignore event. Maybe log in sub-strategy if needed
    }

    protected boolean toBeTerminated(final ScheduledEvent event) {
        return event.getStatus() == Status.FAILED
               && event.getVersion() > 10;
    }

    protected void terminate(final ScheduledEvent event) {
        event.setStatus(Status.TERMINATED);
        eventRepository.save(event);
    }

    protected void startProcessing(final ScheduledEvent event) {
        event.setStatus(Status.ONGOING);
        eventRepository.save(event);
    }

    protected abstract void executeEventJob(ScheduledEvent event);

    protected void complete(final ScheduledEvent event) {
        event.setStatus(Status.COMPLETED);
        eventRepository.save(event);
    }

    protected void markAsFailed(final ScheduledEvent event, final String errorMsg) {
        event.setStatus(Status.FAILED);
        event.addErrorMessage(errorMsg);
        eventRepository.save(event);
    }
}
