package exception;

public class ExerciseNotCompletedException extends RuntimeException {

    public ExerciseNotCompletedException() {
        super("Exercise not completed. Please provide an implementation.");
    }

}
