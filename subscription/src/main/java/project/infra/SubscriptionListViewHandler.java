package project.infra;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import project.config.kafka.KafkaProcessor;
import project.domain.*;

@Service
public class SubscriptionListViewHandler {

    //<<< DDD / CQRS
    @Autowired
    private SubscriptionListRepository subscriptionListRepository;
    //>>> DDD / CQRS

    @StreamListener(
            value = KafkaProcessor.INPUT,
            condition = "headers['type']=='SubscriptionAdded'"
    )
    public void wheneverSubsciptionAdded_AddToList(
        @Payload SubscriptionAdded subscriptionAdded
    ){
        try{
            System.out.println("\n##### [View] SubscriptionAdded Event received:"+subscriptionAdded+"\n");

            // view 객체 생성
            SubscriptionList subscriptionList = new SubscriptionList();
            subscriptionList.setUserId(subscriptionAdded.getUserId());
            subscriptionList.setBookId(subscriptionAdded.getBookId());
            subscriptionList.setBookTitle(subscriptionAdded.getBookTitle());
            subscriptionList.setAuthorId(subscriptionAdded.getAuthorId());
            subscriptionList.setCategory(subscriptionAdded.getCategory());
            subscriptionList.setBookCoverImage(subscriptionAdded.getBookCoverImage());
            subscriptionList.setBookSummary(subscriptionAdded.getBookSummary());
            subscriptionList.setBookContent(subscriptionAdded.getBookContent());

            subscriptionListRepository.save(subscriptionList);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
