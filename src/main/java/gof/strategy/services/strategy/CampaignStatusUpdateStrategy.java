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

    private CampaignRepository campaignRepository;

    public CampaignStatusUpdateStrategy(ScheduledEventRepository scheduledEventRepository,
                                        CampaignRepository campaignRepository) {
        super(scheduledEventRepository);
        this.campaignRepository = campaignRepository;
    }

    @Override
    public ScheduledEvent process(ScheduledEvent event) {
        super.process(event);

        try {
            Campaign campaign = campaignRepository.getCampaignByName(event.getResourceName())
                    .orElseThrow(() -> new RuntimeException("No campaign present"));
            CampaignStatus campaignStatus = (CampaignStatus) event.getProperties().get(Constants.CAMPAIGN_STATUS);
            if (campaignStatus == null) {
                throw new RuntimeException();
            }
            campaign.setStatus(campaignStatus);
            campaignRepository.save(campaign);
        } catch (Exception e) {
            event.setStatus(Status.FAILED);
        }
        event.setStatus(Status.COMPLETED);
        return scheduledEventRepository.save(event);
    }

    @Override
    public Pair<EventType, EventResourceType> getType() {
        return Pair.of(EventType.UPDATE_STATUS, EventResourceType.CAMPAIGN);
    }
}
