package gof.strategy.services.strategy;

import gof.strategy.domain.ScheduledEvent;
import gof.strategy.domain.campaign.Campaign;
import gof.strategy.repository.CampaignRepository;
import gof.strategy.repository.ScheduledEventRepository;

public abstract class CampaignUpdateStrategy extends AbstractEventStrategy {

    private final CampaignRepository campaignRepository;

    public CampaignUpdateStrategy(ScheduledEventRepository eventRepository, CampaignRepository campaignRepository) {
        super(eventRepository);
        this.campaignRepository = campaignRepository;
    }

    protected abstract void processImpl(Campaign campaign, ScheduledEvent event);

    @Override
    protected void processImpl(ScheduledEvent event) {
        String name = event.getResourceName();
        Campaign campaign = campaignRepository.getCampaignByName(name)
                .orElseThrow(() -> new RuntimeException("Could not find campaign with name: " + name));

        processImpl(campaign, event);

        campaignRepository.save(campaign);
    }
}
