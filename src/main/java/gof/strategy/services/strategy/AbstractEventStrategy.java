package gof.strategy.services.strategy;

import gof.strategy.domain.ScheduledEvent;
import gof.strategy.domain.Status;
import gof.strategy.repository.ScheduledEventRepository;

public abstract class AbstractEventStrategy implements EventStrategy {

    protected ScheduledEventRepository scheduledEventRepository;

    public AbstractEventStrategy(ScheduledEventRepository scheduledEventRepository) {
        this.scheduledEventRepository = scheduledEventRepository;
    }

    @Override
    public ScheduledEvent process(ScheduledEvent event) {
        if (event.getStatus() == Status.FAILED && event.getVersion() > 10) {
            event.setStatus(Status.TERMINATED);
        } else {
            event.setStatus(Status.ONGOING);
        }
        return scheduledEventRepository.save(event);
    }
}
