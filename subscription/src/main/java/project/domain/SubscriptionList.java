package project.domain;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import javax.persistence.*;
import lombok.Data;

//<<< EDA / CQRS
@Entity
@Table(name = "SubscriptionList_table")
@Data
public class SubscriptionList { //DTO

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;

    private Long userId;
    private Long bookId;

    private String bookTitle;
    private String authorId;
    private String category;
    private String bookCoverImage;
    private String bookSummary;
    private String bookContent;
}
