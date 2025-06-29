package project.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.persistence.*;
import lombok.Data;
import project.SubscriptionApplication;
import project.domain.SubscriptionNotOwned;
import project.domain.SubscriptionOwned;

@Entity
@Table(name = "Subscription_table")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
//<<< DDD / Aggregate Root
public class Subscription {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long userId;

    private Integer point;

    private Long bookId;

    @PostPersist
    public void onPostPersist() {
        SubscriptionOwned subscriptionOwned = new SubscriptionOwned(this);
        subscriptionOwned.publishAfterCommit();

        SubscriptionNotOwned subscriptionNotOwned = new SubscriptionNotOwned(
            this
        );
        subscriptionNotOwned.publishAfterCommit();
    }

    public static SubscriptionRepository repository() {
        SubscriptionRepository subscriptionRepository = SubscriptionApplication.applicationContext.getBean(
            SubscriptionRepository.class
        );
        return subscriptionRepository;
    }

    //<<< Clean Arch / Port Method
    // 도서 열람 요청됨(events) -> 구독 여부 확인 policy (도서 열람 요청 시 호출)
    public static void subscriptionCheck(BookViewed bookViewed) {

        SubscriptionRepository repo = repository();

        // Example 2:  finding and process
        boolean hasSubscription = checkSubscription(bookViewed.getUserId());
        boolean bookSubscribed = repo.existsByUserIdAndBookId(bookViewed.getUserId(), bookViewed.getBookId());
        //implement business logic here:

        if(hasSubscription || bookSubscribed){
            SubscriptionOwned ownedEvent = SubscriptionOwned.builder()
                    .userId(bookViewed.getUserId())
                    .bookId(bookViewed.getBookId())
                    .build();
            ownedEvent.publishAfterCommit(); // 커밋 이후에 발행 명령

        } else {
            // 구독x, 정액구독권x -> 포인트 차감 필요
            SubscriptionNotOwned notOwnedEvent = SubscriptionNotOwned.builder()
                    .userId(bookViewed.getUserId())
                    .bookId(bookViewed.getBookId())
                    .build();
            notOwnedEvent.publishAfterCommit(); // 커밋 이후에 발행 명령

        }
        /** Example 1:  new item 
        Subscription subscription = new Subscription();
        repository().save(subscription);

        */

        /** Example 2:  finding and process
        

        repository().findById(bookViewed.get???()).ifPresent(subscription->{
            
            subscription // do something
            repository().save(subscription);


         });
        */

    }

    //>>> Clean Arch / Port Method
    //<<< Clean Arch / Port Method
    // 포인트 차감 후 -> 구독 추가 policy
    public static void subscriptionAdd(PointUpdated pointUpdated) {
        //implement business logic here:

        // Example 1:  new item
        Subscription subscription = Subscription.builder()
                .userId(pointUpdated.getUserId())
                .bookId(pointUpdated.getBookId())
                .build();
        repository().save(subscription);

        // 구독 추가됨(events) 발행 => read model을 위해서
        SubscriptionAdded addedEvent = SubscriptionAdded.builder()
                .userId(pointUpdated.getUserId())
                .bookId(pointUpdated.getBookId())
                .build();
        addedEvent.publishAfterCommit();


        /** Example 1:  new item 
        Subscription subscription = new Subscription();
        repository().save(subscription);

        */

        /** Example 2:  finding and process
        

        repository().findById(pointUpdated.get???()).ifPresent(subscription->{
            
            subscription // do something
            repository().save(subscription);


         });
        */

    }
    //>>> Clean Arch / Port Method

    // 정액 구독권 보유 여부 확인을 위한 함수
    private static boolean checkSubscription(Long userId) {
        // TODO: membership 도메인과 연동 시 구현
        return false;
    }

}
//>>> DDD / Aggregate Root
