package gof.strategy.services.strategy;

import gof.strategy.domain.ScheduledEvent;
import gof.strategy.domain.Status;
import gof.strategy.repository.ScheduledEventRepository;

public abstract class AbstractEventStrategy implements EventStrategy {

  protected final ScheduledEventRepository eventRepository;

  protected AbstractEventStrategy(ScheduledEventRepository eventRepository) {
    this.eventRepository = eventRepository;
  }

  @Override
  public ScheduledEvent process(ScheduledEvent event) {
    if (shouldEventBeTerminated(event)) event.setStatus(Status.TERMINATED);
    else event.setStatus(Status.ONGOING);

    eventRepository.save(event);

    return event;
  }

  protected boolean shouldEventBeTerminated(ScheduledEvent event) {
    return event.getStatus() == Status.FAILED && event.getVersion() > 10;
  }

}
