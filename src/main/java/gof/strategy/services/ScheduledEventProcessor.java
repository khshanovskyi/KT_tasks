package gof.strategy.services;

import exception.ExerciseNotCompletedException;
import gof.strategy.domain.ScheduledEvent;

public class ScheduledEventProcessor {

    public void processEvent(ScheduledEvent event) {
        throw new ExerciseNotCompletedException();
    }

}
