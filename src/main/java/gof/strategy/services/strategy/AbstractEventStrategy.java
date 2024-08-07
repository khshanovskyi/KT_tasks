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
        if (Status.FAILED.equals(event.getStatus()) && event.getVersion() > 10) {
            event.setStatus(Status.EXCLUDED);
        } else {
            event.setStatus(Status.ONGOING);
        }

        return eventRepository.save(event);
    }
}
