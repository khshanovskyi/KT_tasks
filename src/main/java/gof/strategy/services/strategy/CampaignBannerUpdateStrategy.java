package gof.strategy.services.strategy;

import gof.strategy.domain.EventResourceType;
import gof.strategy.domain.EventType;
import gof.strategy.domain.ScheduledEvent;
import gof.strategy.domain.campaign.Campaign;
import gof.strategy.repository.CampaignRepository;
import gof.strategy.repository.ScheduledEventRepository;
import gof.strategy.utils.Constants;
import org.apache.commons.lang3.tuple.Pair;

public class CampaignBannerUpdateStrategy extends AbstractEventStrategy {
    private final CampaignRepository campaignRepository;

    public CampaignBannerUpdateStrategy(ScheduledEventRepository repository, CampaignRepository campaignRepository) {
        super(repository);
        this.campaignRepository = campaignRepository;
    }

    @Override
    protected void executeEventJob(final ScheduledEvent event) {
        final var campaign = getCampaignOrThrow(event.getResourceName());
        final var newBanner = getBannerOrThrow(event);
        updateBanner(campaign, newBanner);
    }

    private Campaign getCampaignOrThrow(final String name) {
        return campaignRepository.getCampaignByName(name)
                .orElseThrow(() -> new RuntimeException("No campaign present"));
    }

    private String getBannerOrThrow(final ScheduledEvent event) {
        final var banner = event.getProperties().get(Constants.BANNER);
        if (banner == null) {
            throw new RuntimeException("No banner present");
        }
        return (String) banner;
    }

    private void updateBanner(final Campaign campaign, final String newBanner) {
        campaign.setBanner(newBanner);
        campaignRepository.save(campaign);
    }

    @Override
    public Pair<EventType, EventResourceType> getType() {
        return Pair.ofNonNull(EventType.UPDATE_BANNER, EventResourceType.CAMPAIGN);
    }
}
