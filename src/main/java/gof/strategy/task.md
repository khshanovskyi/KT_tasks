You need to implement Strategy pattern https://refactoring.guru/design-patterns/strategy.

We have `EventStrategy` interface with method `void process(Order order)`:

1. You need to implement EventStrategy based on the `EventType + EventResourceType`:
    - `EventType#UPDATE_STATUS` + `EventResourceType#CAMPAIGN` -> `CampaignStatusUpdateStrategy`
    - `EventType#UPDATE_BANNER` + `EventResourceType#CAMPAIGN` -> `CampaignBannerUpdateStrategy`
    - 
2. Implement distributor
