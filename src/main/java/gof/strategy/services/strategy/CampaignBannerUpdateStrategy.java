package gof.strategy.services.strategy;

import gof.strategy.domain.EventResourceType;
import gof.strategy.domain.EventType;
import gof.strategy.domain.ScheduledEvent;
import gof.strategy.domain.Status;
import gof.strategy.repository.CampaignRepository;
import gof.strategy.utils.Constants;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.tuple.Pair;

@RequiredArgsConstructor
public class CampaignBannerUpdateStrategy extends AbstractEventStrategy {

    private final CampaignRepository campaignRepository;

    @Override
    public ScheduledEvent process(ScheduledEvent event) {

        try {
            super.process(event);

            var optionalCampaign = campaignRepository.getCampaignByName(event.getResourceName());
            if (optionalCampaign.isEmpty()) {
                throw new RuntimeException("Campaign not found");
            }

            var campaign = optionalCampaign.get();

            Object newBanner = event.getProperties().get(Constants.BANNER);

            if (newBanner == null) {
                throw new RuntimeException("No banner present");
            }
            campaign.setBanner(newBanner.toString());

            campaignRepository.save(campaign);

            event.setStatus(Status.COMPLETED);
        } catch (Exception e) {
            event.setStatus(Status.FAILED);
        }

        return event;
    }

    @Override
    public Pair<EventType, EventResourceType> getType() {
        return Pair.of(EventType.UPDATE_BANNER, EventResourceType.CAMPAIGN);
    }
}
