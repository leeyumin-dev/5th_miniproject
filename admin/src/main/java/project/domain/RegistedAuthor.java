package project.domain;

import lombok.*;
import project.infra.AbstractEvent;

@Data
@ToString
public class RegistedAuthor extends AbstractEvent {

    private Long id;
    private String authorLoginId;
    private String authorPw;
    private String authorName;
    private String authorInfo;
    private String authorPortfolio;
    private Boolean isActive;

    public RegistedAuthor(Admin aggregate) {
        super(aggregate);
        this.id = aggregate.getId();
        this.authorLoginId = aggregate.getAuthorLoginId();
        this.authorPw = aggregate.getAuthorPw();
        this.authorName = aggregate.getAuthorName();
        this.authorInfo = aggregate.getAuthorInfo();
        this.authorPortfolio = aggregate.getAuthorPortfolio();
        this.isActive = aggregate.getIsActive();
    }

    public RegistedAuthor() {
        super();
    }
}
