package gof.strategy.services;

import exception.ExerciseNotCompletedException;
import gof.strategy.domain.EventResourceType;
import gof.strategy.domain.EventType;
import gof.strategy.domain.ScheduledEvent;
import gof.strategy.services.strategy.CampaignBannerUpdateStrategy;
import gof.strategy.services.strategy.CampaignStatusUpdateStrategy;

public class ScheduledEventProcessor {

    private CampaignBannerUpdateStrategy campaignBannerUpdateStrategy;
    private CampaignStatusUpdateStrategy campaignStatusUpdateStrategy;

    public ScheduledEventProcessor(CampaignBannerUpdateStrategy campaignBannerUpdateStrategy,
                                   CampaignStatusUpdateStrategy campaignStatusUpdateStrategy) {
        this.campaignBannerUpdateStrategy = campaignBannerUpdateStrategy;
        this.campaignStatusUpdateStrategy = campaignStatusUpdateStrategy;
    }

    public void processEvent(ScheduledEvent event) {
        if (event.getEventType() == EventType.UPDATE_STATUS
                && event.getResourceType() == EventResourceType.CAMPAIGN) {
            campaignStatusUpdateStrategy.process(event);
        }
        if (event.getEventType() == EventType.UPDATE_BANNER
                && event.getResourceType() == EventResourceType.CAMPAIGN) {
            campaignBannerUpdateStrategy.process(event);
        }
    }

}
