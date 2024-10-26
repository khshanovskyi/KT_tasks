package gof.strategy.services.strategy;

import gof.strategy.domain.EventResourceType;
import gof.strategy.domain.EventType;
import gof.strategy.domain.ScheduledEvent;
import gof.strategy.domain.campaign.Campaign;
import gof.strategy.domain.campaign.CampaignStatus;
import gof.strategy.repository.CampaignRepository;
import gof.strategy.repository.ScheduledEventRepository;
import gof.strategy.utils.Constants;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Optional;

public class CampaignStatusUpdateStrategy extends CampaignUpdateStrategy {

    public CampaignStatusUpdateStrategy(ScheduledEventRepository eventRepository, CampaignRepository campaignRepository) {
        super(eventRepository, campaignRepository);
    }

    @Override
    protected void processImpl(Campaign campaign, ScheduledEvent event) {
        Optional<Object> statusOptional = Optional.ofNullable(event.getProperties().get(Constants.CAMPAIGN_STATUS));
        var status = (CampaignStatus) statusOptional.orElseThrow(() -> new RuntimeException("CampaignStatus not found"));
        campaign.setStatus(status);
    }

    @Override
    public Pair<EventType, EventResourceType> getType() {
        return Pair.of(EventType.UPDATE_STATUS, EventResourceType.CAMPAIGN);
    }
}
