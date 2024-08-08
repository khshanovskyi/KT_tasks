package gof.strategy.services.strategy;

import gof.strategy.domain.EventResourceType;
import gof.strategy.domain.EventType;
import gof.strategy.domain.ScheduledEvent;
import gof.strategy.domain.Status;
import gof.strategy.domain.campaign.Campaign;
import gof.strategy.repository.CampaignRepository;
import gof.strategy.repository.ScheduledEventRepository;
import gof.strategy.utils.Constants;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Optional;

public class CampaignBannerUpdateStrategy extends AbstractEventStrategy {


    public CampaignBannerUpdateStrategy(final ScheduledEventRepository eventRepository,
                                        final CampaignRepository campaignRepository) {
        super(eventRepository, campaignRepository);
    }

    @Override
    public ScheduledEvent process(ScheduledEvent event) {
        ScheduledEvent eventWithDefaults = super.process(event);
        try {
            Campaign campaign = this.campaignRepository.getCampaignByName(eventWithDefaults.getResourceName())
                    .orElseThrow(() -> new RuntimeException("No campaign present!"));
            var banner = Optional.ofNullable(event.getProperties().get(Constants.BANNER))
                    .orElseThrow(() -> new RuntimeException("No banner present!"));
            campaign.setBanner((String) banner);
            this.campaignRepository.save(campaign);
            eventWithDefaults.setStatus(Status.COMPLETED);
        } catch (Exception ex) {
            eventWithDefaults.setStatus(Status.FAILED);
        }
        return this.eventRepository.save(eventWithDefaults);
    }

    @Override
    public Pair<EventType, EventResourceType> getType() {
        return Pair.of(EventType.UPDATE_BANNER, EventResourceType.CAMPAIGN);
    }
}
