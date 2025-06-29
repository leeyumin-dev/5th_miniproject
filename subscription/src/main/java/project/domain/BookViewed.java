package project.domain;

import java.util.*;
import lombok.*;
import project.domain.*;
import project.infra.AbstractEvent;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class BookViewed extends AbstractEvent { // 도서 열람 요청됨 event

    private Long id;
    private Long userId;
    private Long bookId;
}
