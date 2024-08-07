package gof.strategy.repository;

import gof.strategy.domain.ScheduledEvent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class ScheduledEventRepository {

    private final Map<UUID, ScheduledEvent> scheduledEvents = new HashMap<>();

    public ScheduledEvent save(ScheduledEvent scheduledEvent) {
        return scheduledEvents.put(scheduledEvent.getId(), scheduledEvent);
    }

    public List<ScheduledEvent> getAll() {
        return scheduledEvents.values().stream().toList();
    }

    public Optional<ScheduledEvent> getById(UUID id) {
        return Optional.ofNullable(scheduledEvents.get(id));
    }

}
