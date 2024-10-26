package gof.strategy.services.strategy;

import gof.strategy.domain.EventResourceType;
import gof.strategy.domain.EventType;
import gof.strategy.domain.ScheduledEvent;
import gof.strategy.domain.Status;
import gof.strategy.domain.campaign.Campaign;
import gof.strategy.domain.campaign.CampaignStatus;
import gof.strategy.repository.CampaignRepository;
import gof.strategy.repository.ScheduledEventRepository;
import gof.strategy.utils.Constants;
import org.apache.commons.lang3.tuple.Pair;

public class CampaignStatusUpdateStrategy extends AbstractEventStrategy {
    private final CampaignRepository campaignRepository;
    private final ScheduledEventRepository scheduledEventRepository;

    public CampaignStatusUpdateStrategy(CampaignRepository campaignRepository, ScheduledEventRepository scheduledEventRepository) {
        this.campaignRepository = campaignRepository;
        this.scheduledEventRepository = scheduledEventRepository;
    }

    @Override
    public ScheduledEvent process(ScheduledEvent event) {
        super.process(event);
        try {
            Campaign campaign = campaignRepository.getCampaignByName(event.getResourceName())
                    .orElseThrow(() -> new RuntimeException("Campaign does not exist"));
            CampaignStatus campaignStatus = (CampaignStatus) event.getProperties().get(Constants.CAMPAIGN_STATUS);
            if (campaignStatus == null) {
                throw new RuntimeException("Campaign status is not present");
            }
            campaign.setStatus(campaignStatus);
            campaignRepository.save(campaign);
            event.setStatus(Status.COMPLETED);
        } catch (RuntimeException e) {
            event.setStatus(Status.FAILED);
        } finally {
            saveEvent(event);
        }
        return event;
    }

    @Override
    protected void saveEvent(ScheduledEvent event) {
        scheduledEventRepository.save(event);
    }

    @Override
    public Pair<EventType, EventResourceType> getType() {
        return Pair.of(EventType.UPDATE_STATUS, EventResourceType.CAMPAIGN);
    }
}
