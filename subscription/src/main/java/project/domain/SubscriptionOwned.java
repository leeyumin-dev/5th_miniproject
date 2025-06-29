package project.domain;

import java.time.LocalDate;
import java.util.*;
import lombok.*;
import project.domain.*;
import project.infra.AbstractEvent;

//<<< DDD / Domain Event
@Data
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SubscriptionOwned extends AbstractEvent {

    //private Long id;
    private Long userId;
    private Integer point;
    private Long bookId;

    public SubscriptionOwned(Subscription aggregate) {
        super(aggregate);
    }

//    public SubscriptionOwned() {
//        super();
//    }
}
//>>> DDD / Domain Event
