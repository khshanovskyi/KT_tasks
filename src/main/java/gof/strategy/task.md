# Task: Implement Strategy and Template Method Patterns
- Strategy pattern https://refactoring.guru/design-patterns/strategy
- Template Method https://refactoring.guru/design-patterns/template-method

## Domain Model:
You are working on a system that processes scheduled events for campaigns. Each event can have different types and resource types, and the processing logic can vary based on these types. The system should be designed using the Strategy and Template Method patterns to allow flexible and reusable processing strategies.

## Objective:
***Implement the Strategy and Template Method patterns from scratch. You will create the following components:***
1. EventStrategy interface
2. AbstractEventStrategy abstract class (for the Template Method pattern)
3. Concrete strategy classes: CampaignBannerUpdateStrategy and CampaignStatusUpdateStrategy
4. ScheduledEventProcessor class to distribute events to the appropriate strategy

## Steps:
1. Take a look at the EventStrategy interface methods. You will need to implement these methods in the concrete strategy classes.
   - `ScheduledEvent process(ScheduledEvent event)`
   - `Pair<EventType, EventResourceType> getType()`
2. Create the `AbstractEventStrategy` abstract class that implements the `EventStrategy` interface. This class should provide a default implementation for the `process` method.
   This class with override the `process` will be the implementation if the Template Method pattern and will:
   - Check if the `Event#Status == Status#FAILED` and `Event#version > 10` -> set status to `Status#TERMINATED` and save the event
   - otherwise, call set `Event#Status` to `Status#ONGOING` and save the event
3. Implement the `CampaignBannerUpdateStrategy`:
   - method `process` should update the banner for the campaign
      - call `super.process(event)` to apply the default processing logic
      - get `Campaign` via `CampaignRepository` if not found -> throw `RuntimeException` with message that no campaign present
      - get the new `Banner:String` from the `Event#properties` (use `Constants#BANNER`) and set it to the `Campaign#banner`, if no banner present -> throw `RuntimeException` with message that no banner present
      - save the `Campaign` via `CampaignRepository`
      - set the `Event#status` to `Status#COMPLETED`
      - **in case if any Exceptions were intercepted set the `Event#status` to `Status#FAILED`**
      - save the `Event` via `EventRepository` and return it
   - method `getType` should return `Pair.of(EventType.UPDATE_BANNER, EventResourceType.CAMPAIGN)`
4. Implement the `CampaignStatusUpdateStrategy`:
   - method `process` should update the status for the campaign
      - call `super.process(event)` to apply the default processing logic
      - get `Campaign` via `CampaignRepository` if not found -> throw `RuntimeException` with message that no campaign present
      - get the new `campaignStatus:CampaignStatus` from the `Event#properties` (use `Constants#CAMPAIGN_STATUS`) and set it to the `Campaign#campaignStatus`, if no banner present -> throw `RuntimeException` with message that no campaignStatus present
      - save the `Campaign` via `CampaignRepository`
      - set the `Event#status` to `Status#COMPLETED`
      - **in case if any Exceptions were intercepted set the `Event#status` to `Status#FAILED`**
      - save the `Event` via `EventRepository` and return it
   - method `getType` should return `Pair.of(EventType.UPDATE_STATUS, EventResourceType.CAMPAIGN)`
5. Implement the `ScheduledEventProcessor` class:
   - add a method to register strategies (or you can do that in the constructor)
   - provide realization for the `process` method that will distribute events to the appropriate strategy based on the event type and resource type
6. Don't forget to refactor the `Application` class to use in the `ScheduledEventProcessor` Strategies that you have created