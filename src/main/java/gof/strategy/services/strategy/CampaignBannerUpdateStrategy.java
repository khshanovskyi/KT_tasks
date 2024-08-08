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

public class CampaignBannerUpdateStrategy extends AbstractEventStrategy{

    private CampaignRepository campaignRepository;
    private ScheduledEventRepository scheduledEventRepository;

    public CampaignBannerUpdateStrategy(ScheduledEventRepository scheduledEventRepository,
                                        CampaignRepository campaignRepository) {
        this.scheduledEventRepository = scheduledEventRepository;
        this.campaignRepository = campaignRepository;
    }

    @Override
    public ScheduledEvent process(ScheduledEvent event) {
        super.process(event);
        try {
            Campaign campaign = campaignRepository.getCampaignByName(event.getResourceName())
                    .orElseThrow(() -> new RuntimeException("No campaign present"));
            String banner = (String) event.getProperties().get(Constants.BANNER);
            if (banner == null) {
                throw new RuntimeException("No banner present");
            }
            campaign.setBanner(banner);
            campaignRepository.save(campaign);
        } catch (Exception e) {
            event.setStatus(Status.FAILED);
        }

        event.setStatus(Status.COMPLETED);
        return saveEvent(event);
    }

    @Override
    public ScheduledEvent saveEvent(ScheduledEvent event) {
        return scheduledEventRepository.save(event);
    }

    @Override
    public Pair<EventType, EventResourceType> getType() {
        return Pair.of(EventType.UPDATE_BANNER, EventResourceType.CAMPAIGN);
    }
}
