package gof.strategy.repository;

import gof.strategy.domain.ScheduledEvent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ScheduledEventRepository {

    private final Map<UUID, ScheduledEvent> scheduledEvents = new HashMap<>();

    public void add(ScheduledEvent scheduledEvent) {
        scheduledEvents.put(scheduledEvent.getId(), scheduledEvent);
    }

    public List<ScheduledEvent> getAll() {
        return scheduledEvents.values().stream().toList();
    }

    public ScheduledEvent update(ScheduledEvent event) {
        scheduledEvents.put(event.getId(), event);
        return event;
    }
}
