package project.infra;

import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.MessageChannel;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.domain.*;

//<<< Clean Arch / Inbound Adaptor

@RestController
@RequestMapping(value="/api/v1/subscriptions")
@Transactional
public class SubscriptionController {

    @Autowired
    SubscriptionRepository subscriptionRepository;


    // Kafka로 메시지를 보낼 채널 (application.yml의 event-out과 바인딩됨)
    private MessageChannel eventOut;

    public SubscriptionController(SubscriptionRepository subscriptionRepository,
                                  @Qualifier("event-out") MessageChannel eventOut) {
        this.subscriptionRepository = subscriptionRepository;
        this.eventOut = eventOut;
    }


    // BookViewed 이벤트를 수신하고 Kafka로 발행하는 엔드포인트
    @PostMapping("/view")
    public String viewBook(@RequestBody BookViewed bookViewed) {
        eventOut.send(
                MessageBuilder
                        .withPayload(bookViewed)
                        .setHeader("type", "BookViewed")
                        .build());
        return "✅ BookViewed event sent to Kafka.";
    }


    @PostMapping("/test-point")
    public void testPointUpdate(@RequestBody PointUpdated event) {
        Subscription.subscriptionAdd(event);
    }





}
//>>> Clean Arch / Inbound Adaptor
