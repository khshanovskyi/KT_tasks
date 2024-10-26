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

public class CampaignStatusUpdateStrategy extends AbstractEventStrategy{

    private final CampaignRepository campaignRepository;

    public CampaignStatusUpdateStrategy(ScheduledEventRepository scheduledEventRepository,CampaignRepository campaignRepository) {
        super(scheduledEventRepository);
        this.campaignRepository = campaignRepository;
    }

    @Override
    protected ScheduledEvent processImpl(ScheduledEvent scheduledEvent) {
        try {
            Campaign campaign = campaignRepository.getCampaignByName(scheduledEvent.getResourceName())
                    .orElseThrow(() -> new RuntimeException("Campaign not found"));
            Optional.ofNullable(scheduledEvent.getProperties().get(Constants.CAMPAIGN_STATUS))
                    .map(String::valueOf)
                    .ifPresentOrElse(
                            banner -> {
                                campaign.setBanner(banner);
                                campaignRepository.save(campaign);
                            },
                            () -> new RuntimeException("no campaignStatus present")
                    );
            scheduledEvent.setStatus(Status.COMPLETED);
        } catch (Exception exception) {
            scheduledEvent.setStatus(Status.FAILED);
        }
        return eventRepository.save(scheduledEvent);
    }

    @Override
    public Pair<EventType, EventResourceType> getType() {
        return Pair.of(EventType.UPDATE_STATUS, EventResourceType.CAMPAIGN);
    }

}
