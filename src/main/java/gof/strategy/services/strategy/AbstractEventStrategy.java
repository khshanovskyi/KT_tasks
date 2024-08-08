package gof.strategy.services.strategy;

import gof.strategy.domain.ScheduledEvent;
import gof.strategy.domain.Status;
import gof.strategy.repository.CampaignRepository;
import gof.strategy.repository.ScheduledEventRepository;

public abstract class AbstractEventStrategy implements EventStrategy {

    private static final int MAX_VERSION = 10;

    protected final ScheduledEventRepository eventRepository;
    protected final CampaignRepository campaignRepository;

    public AbstractEventStrategy(final ScheduledEventRepository eventRepository, final CampaignRepository campaignRepository) {
        this.eventRepository = eventRepository;
        this.campaignRepository = campaignRepository;
    }

    @Override
    public ScheduledEvent process(final ScheduledEvent event) {
        if (isFailed(event)) {
            event.setStatus(Status.TERMINATED);
        } else {
            event.setStatus(Status.ONGOING);
        }
        return this.eventRepository.save(event);
    }

    private boolean isFailed(final ScheduledEvent event) {
        return event.getStatus() == Status.FAILED && event.getVersion() > MAX_VERSION;
    }
}
