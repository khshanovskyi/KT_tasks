package gof.strategy;

import gof.strategy.domain.ScheduledEvent;
import gof.strategy.domain.campaign.Campaign;
import gof.strategy.domain.campaign.CampaignStatus;
import gof.strategy.repository.CampaignRepository;
import gof.strategy.services.CampaignService;
import gof.strategy.repository.ScheduledEventRepository;
import gof.strategy.services.ScheduledEventContext;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Random;

public class Application {

    public static void main(String[] args) {
        var random = new Random();

        var campaignRepository = new CampaignRepository();
        var scheduledEventRepository = new ScheduledEventRepository();
        var campaignService = new CampaignService(scheduledEventRepository);

        List<Campaign> campaigns = campaignRepository.getAll();

        System.out.println("======================================");
        campaigns.forEach(System.out::println);
        System.out.println("======================================\n\n");

        //Configuration
        campaignService.updateStatus(campaigns.get(random.nextInt(campaigns.size())), CampaignStatus.ONGOING);
        campaignService.updateStatus(campaigns.get(random.nextInt(campaigns.size())), CampaignStatus.FINISHED);
        campaignService.scheduleUpdateBanner(campaigns.get(random.nextInt(campaigns.size())), Instant.now().plus(random.nextInt(1000), ChronoUnit.SECONDS));

        //Since this point you need to provide your implementation
        ScheduledEventContext scheduledEventContext = new ScheduledEventContext();
        for (ScheduledEvent scheduledEvent : scheduledEventRepository.getAll()) {
            scheduledEventContext.processEvent(scheduledEvent);
        }

        System.out.println("======================================");
        campaigns.forEach(System.out::println);
        System.out.println("======================================");
    }
}
