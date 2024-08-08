package gof.strategy.services.strategy;

import gof.strategy.domain.EventResourceType;
import gof.strategy.domain.EventType;
import gof.strategy.domain.ScheduledEvent;
import gof.strategy.domain.Status;
import gof.strategy.repository.CampaignRepository;
import gof.strategy.utils.Constants;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Optional;

public class CampaignBannerUpdateStrategy extends AbstractEventStrategy {

    private final CampaignRepository repository = new CampaignRepository();

    @Override
    public ScheduledEvent process(ScheduledEvent event) {
        final var processed = super.process(event);
        final var campaign = repository.getCampaignByName(event.getResourceName())
                .orElseThrow(() -> new RuntimeException("No campaign present for event: " + event.getId()));
        final var newBanner = Optional.ofNullable(event.getProperties().get(Constants.BANNER))
                .map(String.class::cast)
                .orElseThrow(() -> new RuntimeException("No banner present for event: " + event.getId()));
        campaign.setBanner(newBanner);
        repository.save(campaign);
        processed.setStatus(Status.COMPLETED);
        // TODO catch exception and save event with failed status
        return processed;
    }

    @Override
    public Pair<EventType, EventResourceType> getType() {
        return Pair.of(EventType.UPDATE_BANNER, EventResourceType.CAMPAIGN);
    }


}
