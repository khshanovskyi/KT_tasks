package gof.strategy.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

/**
 * Universal representation of scheduled event. <br>
 * The main purpose is to contains all required information for further scheduling some scheduled jobs from such events.
 * <br>
 * This approach allow us to have one table where we can persist all scheduled events, but on top of that it has the
 * drawback that we need to control `consistency` state on the Application level instead of DB layer.
 * <br>
 * <b>Pay attention that this approach violates 3rd `DB Normalization` rule</b>
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode(of = {"id", "eventType", "resourceType", "resourceName", "status"})
public class ScheduledEvent {

    private UUID id;
    private EventType eventType;
    private EventResourceType resourceType;

    /**
     * Related to {@link EventResourceType} where `eventResourceType` is the representation of entity and `resourceName`
     * is unique name of such entity:
     * <pre>
     *     Example 1:
     *          Entity: Campaign
     *          EventResourceType: CAMPAIGN
     *          `resourceName` = Campaign.getName()
     *     Example 2:
     *          Entity: Survey
     *          EventResourceType: SURVEY
     *          `resourceName` = Survey.getName()
     * </pre>
     */
    private String resourceName;

    /**
     * Representation of JSON with required properties for Scheduled job configuration:
     * <pre>
     *     Example 1: {}
     *     Example 2:
     *          {
     *              "campaignStatus": "FINISHED"
     *          }
     * </pre>
     */
    private Map<String, Object> properties = new HashMap<>();
    private Instant scheduledAt;
    private Status status = Status.PENDING;

    /**
     * In case if job failed then we add Exception message to the `errorMessages` for further review
     */
    @Builder.Default
    private String[] errorMessages = new String[0];

    public void addErrorMessage(final String message) {
        if (Objects.nonNull(message)) {
            if (errorMessages == null) {
                errorMessages = new String[1];
                errorMessages[0] = message;
            } else {
                String[] newErrorMessages = new String[errorMessages.length + 1];
                System.arraycopy(errorMessages, 0, newErrorMessages, 0, errorMessages.length);
                newErrorMessages[errorMessages.length] = message;
                errorMessages = newErrorMessages;
            }
        }
    }
}
