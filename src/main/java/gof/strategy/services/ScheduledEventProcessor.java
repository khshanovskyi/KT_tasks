package gof.strategy.services;

import exception.ExerciseNotCompletedException;
import gof.strategy.domain.EventType;
import gof.strategy.domain.ScheduledEvent;
import gof.strategy.repository.CampaignRepository;
import gof.strategy.repository.ScheduledEventRepository;
import gof.strategy.services.strategy.CampaignBannerUpdateStrategy;
import gof.strategy.services.strategy.CampaignStatusUpdateStrategy;
import gof.strategy.services.strategy.EventStrategy;

public class ScheduledEventProcessor {

    private EventStrategy campaignBannerUpdateStrategy;
    private EventStrategy campaignStatusUpdateStrategy;

    public ScheduledEventProcessor(final ScheduledEventRepository eventRepository,
                                   final CampaignRepository campaignRepository) {
        this.campaignBannerUpdateStrategy = new CampaignBannerUpdateStrategy(eventRepository, campaignRepository);
        this.campaignStatusUpdateStrategy = new CampaignStatusUpdateStrategy(eventRepository, campaignRepository);
    }
    public void processEvent(ScheduledEvent event) {
        if (event.getEventType() == EventType.UPDATE_BANNER) {
            this.campaignBannerUpdateStrategy.process(event);
        } else {
            this.campaignStatusUpdateStrategy.process(event);
        }
    }

}
