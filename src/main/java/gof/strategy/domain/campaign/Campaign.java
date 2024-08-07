package gof.strategy.domain.campaign;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Campaign {

    private Long id;

    private String name;

    /**
     * When Campaign should move to {@link CampaignStatus#ONGOING}
     */
    private Instant startDate;

    /**
     * When Campaign should move to {@link CampaignStatus#FINISHED}
     */
    private Instant endDate;

    @Builder.Default
    private CampaignStatus status = CampaignStatus.NEW;

    /**
     * Some random string;
     */
    private String banner;

}
