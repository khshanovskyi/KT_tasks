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

import java.util.Optional;

public class CampaignStatusUpdateStrategy extends AbstractEventStrategy {

    public CampaignStatusUpdateStrategy(final ScheduledEventRepository eventRepository,
                                        final CampaignRepository campaignRepository) {
        super(eventRepository, campaignRepository);
    }

    @Override
    public ScheduledEvent process(ScheduledEvent event) {
        ScheduledEvent eventWithDefaults = super.process(event);
        try {
            Campaign campaign = this.campaignRepository.getCampaignByName(eventWithDefaults.getResourceName())
                    .orElseThrow(() -> new RuntimeException("No campaign present!"));
            var obj = Optional.ofNullable(eventWithDefaults.getProperties().get(Constants.CAMPAIGN_STATUS))
                    .orElseThrow(() -> new RuntimeException("No campaignStatus present!"));
            campaign.setStatus((CampaignStatus) obj);
            this.campaignRepository.save(campaign);
            eventWithDefaults.setStatus(Status.COMPLETED);
        } catch (Exception ex) {
            eventWithDefaults.setStatus(Status.FAILED);
            eventWithDefaults.addErrorMessage(ex.getMessage());
        }
        return this.eventRepository.save(eventWithDefaults);
    }

    @Override
    public Pair<EventType, EventResourceType> getType() {
        return Pair.of(EventType.UPDATE_STATUS, EventResourceType.CAMPAIGN);
    }
}
