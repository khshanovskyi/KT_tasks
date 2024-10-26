package gof.strategy.services.strategy;

import gof.strategy.domain.EventResourceType;
import gof.strategy.domain.EventType;
import gof.strategy.domain.ScheduledEvent;
import gof.strategy.domain.campaign.Campaign;
import gof.strategy.repository.CampaignRepository;
import gof.strategy.repository.ScheduledEventRepository;
import gof.strategy.utils.Constants;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Optional;

public class CampaignBannerUpdateStrategy extends CampaignUpdateStrategy {


    public CampaignBannerUpdateStrategy(ScheduledEventRepository eventRepository, CampaignRepository campaignRepository) {
        super(eventRepository, campaignRepository);
    }

    @Override
    protected void processImpl(Campaign campaign, ScheduledEvent event) {
        Optional<Object> bannerOptional = Optional.ofNullable(event.getProperties().get(Constants.BANNER));
        var bannerString = (String) bannerOptional.orElseThrow(() -> new RuntimeException("Banner not found"));
        campaign.setBanner(bannerString);
    }

    @Override
    public Pair<EventType, EventResourceType> getType() {
        return Pair.of(EventType.UPDATE_BANNER, EventResourceType.CAMPAIGN);
    }
}
