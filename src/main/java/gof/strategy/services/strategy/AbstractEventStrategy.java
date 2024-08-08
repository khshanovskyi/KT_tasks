package gof.strategy.services.strategy;

import gof.strategy.domain.ScheduledEvent;
import gof.strategy.domain.Status;
import gof.strategy.repository.CampaignRepository;
import gof.strategy.repository.ScheduledEventRepository;

public abstract class AbstractEventStrategy implements EventStrategy {

    protected final ScheduledEventRepository eventRepository;
    protected final CampaignRepository campaignRepository;

    public AbstractEventStrategy(final ScheduledEventRepository eventRepository, final CampaignRepository campaignRepository) {
        this.eventRepository = eventRepository;
        this.campaignRepository = campaignRepository;
    }

    @Override
    public ScheduledEvent process(final ScheduledEvent event) {
        if (event.getStatus() == Status.FAILED && event.getVersion() > 10) {
            event.setStatus(Status.TERMINATED);
        } else {
            event.setStatus(Status.ONGOING);
        }
        return this.eventRepository.save(event);
    }
}
