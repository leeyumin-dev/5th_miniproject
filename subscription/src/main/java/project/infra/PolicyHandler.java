package project.infra;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import javax.naming.NameParser;
import javax.naming.NameParser;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import project.config.kafka.KafkaProcessor;
import project.domain.*;

//<<< Clean Arch / Inbound Adaptor
@Service
@Transactional
public class PolicyHandler {

    @Autowired
    SubscriptionRepository subscriptionRepository;

    @StreamListener(KafkaProcessor.INPUT)
    public void whatever(@Payload String eventString) {}

    // 1. 도서 열람 요청 수신 -> 구독 여부 확인
    // 도서 열람 요청됨 events를 수신해서 -> Subscription의 subscriptionCheck() 으로 넘긴다.
    @StreamListener(
        value = KafkaProcessor.INPUT,
        condition = "headers['type']=='BookViewed'"
    )
    public void wheneverBookViewed_SubscriptionCheck(@Payload BookViewed bookViewed) {
        BookViewed event = bookViewed;
        System.out.println(
            "\n\n##### listener SubscriptionCheck : " + bookViewed + "\n\n"
        );

        // Sample Logic //
        // SubscriptionOwned 또는 SubscriptionNotOwned 이벤트 발행
        Subscription.subscriptionCheck(event);
    }

    // 2. 포인트 업데이트 수신 -> 구독 추가
    @StreamListener(
        value = KafkaProcessor.INPUT,
        condition = "headers['type']=='PointUpdated'"
    )
    public void wheneverPointUpdated_SubscriptionAdd(
        @Payload PointUpdated pointUpdated
    ) {
        PointUpdated event = pointUpdated;
        System.out.println(
            "\n\n##### listener SubscriptionAdd : " + pointUpdated + "\n\n"
        );

        // Sample Logic //
        // -> 구독 정보 저장 -> SubscriptionAdded 이벤트 발행 -> readmodel에 반영
        Subscription.subscriptionAdd(event);
    }
}
//>>> Clean Arch / Inbound Adaptor
