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

public class CampaignStatusUpdateStrategy extends AbstractEventStrategy {
    private final CampaignRepository campaignRepository;

    public CampaignStatusUpdateStrategy(ScheduledEventRepository repository, CampaignRepository campaignRepository) {
        super(repository);
        this.campaignRepository = campaignRepository;
    }

    @Override
    protected void executeEventJob(ScheduledEvent event) {
        final var campaign = getCampaignOrThrow(event.getResourceName());
        final var newCampaignStatus = getCampaignStatusOrThrow(event);
        updateStatus(campaign, newCampaignStatus);
    }

    private Campaign getCampaignOrThrow(final String name) {
        return campaignRepository.getCampaignByName(name)
                .orElseThrow(() -> new RuntimeException("No campaign present"));
    }

    private CampaignStatus getCampaignStatusOrThrow(final ScheduledEvent event) {
        final var status = event.getProperties().get(Constants.CAMPAIGN_STATUS);
        if (status == null) {
            throw new RuntimeException("No status present");
        }
        return (CampaignStatus) status;
    }

    private void updateStatus(final Campaign campaign, final CampaignStatus newStatus) {
        campaign.setStatus(newStatus);
        campaignRepository.save(campaign);
    }

    @Override
    public Pair<EventType, EventResourceType> getType() {
        return Pair.ofNonNull(EventType.UPDATE_STATUS, EventResourceType.CAMPAIGN);
    }
}
