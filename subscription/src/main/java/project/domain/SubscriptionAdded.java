package project.domain;

import java.time.LocalDate;
import java.util.*;
import lombok.*;
import project.domain.*;
import project.infra.AbstractEvent;

//<<< DDD / Domain Event
@Data
@ToString
public class SubscriptionAdded extends AbstractEvent { // ReadModel 생성을 위해 추가 필드 필요

    //private Long id;
    private Long userId;
    private Integer point; // 아마 여기는 안쓸듯
    private Long bookId;

    // 확장 시 도서 메타정보도 포함 가능:
    private String bookTitle;
    private String authorId;
    private String category;
    private String bookSummary;
    private String bookCoverImage;
    private String bookContent;

    public SubscriptionOwned(Subscription aggregate) {
        super(aggregate);
    }

    public SubscriptionOwned() {
        super();
    }
}
//>>> DDD / Domain Event
