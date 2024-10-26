package gof.strategy.services.strategy.impl;

import gof.strategy.domain.EventResourceType;
import gof.strategy.domain.EventType;
import gof.strategy.domain.ScheduledEvent;
import gof.strategy.domain.Status;
import gof.strategy.repository.CampaignRepository;
import gof.strategy.repository.ScheduledEventRepository;
import gof.strategy.services.strategy.AbstractEventStrategy;
import gof.strategy.utils.Constants;
import org.apache.commons.lang3.tuple.Pair;

public class CampaignBannerUpdateStrategy extends AbstractEventStrategy {

  protected final CampaignRepository campaignRepository;

  protected CampaignBannerUpdateStrategy(ScheduledEventRepository eventRepository, CampaignRepository campaignRepository) {
    super(eventRepository);
    this.campaignRepository = campaignRepository;
  }

  @Override
  public ScheduledEvent process(ScheduledEvent event) {
    try {
      var preprocessedEvent = super.process(event);
      var currentEventStatus = preprocessedEvent.getStatus();
      if (currentEventStatus == Status.FAILED || currentEventStatus == Status.TERMINATED) return event;

      var campaignName = preprocessedEvent.getResourceName();
      var campaign = campaignRepository.getCampaignByName(campaignName).orElseThrow(
        () -> new RuntimeException("Cannot find campaign by the name %s".formatted(campaignName)));

      Object newBanner;
      if (preprocessedEvent.getProperties().containsKey(Constants.BANNER))
        newBanner = preprocessedEvent.getProperties().get(Constants.BANNER);
      else throw new RuntimeException("Cannot find campaign by the name %s".formatted(campaignName));
      campaign.setBanner(newBanner.toString());
      campaignRepository.save(campaign);

      preprocessedEvent.setStatus(Status.COMPLETED);
      eventRepository.save(event);
      return event;
    } catch (Exception e) {
      event.setStatus(Status.FAILED);
      eventRepository.save(event);
      return event;
    }
  }

  @Override
  public Pair<EventType, EventResourceType> getType() {
    return Pair.of(Pair.of(EventType.UPDATE_BANNER, EventResourceType.CAMPAIGN));
  }
}
