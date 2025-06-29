package project.domain;

import java.util.*;
import lombok.*;
import project.domain.*;
import project.infra.AbstractEvent;

@Data
@ToString
@Builder
public class PointUpdated extends AbstractEvent { // 포인트 업데이트됨 event

    private Long id;
    private Long userId;
    private Long bookId;
}
