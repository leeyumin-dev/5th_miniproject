package project.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import project.PointApplication;
import project.domain.PointUpdated;

@Entity
@Table(name = "Point_table")
@Data
public class Point {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long Id;

    private Long userId;

    private Date changeDate;

    private Integer changePoint;

    private Long pointSum;

    private String reason;

    public static PointRepository repository() {
        return PointApplication.applicationContext.getBean(PointRepository.class);
    }

    // 포인트 차감: 도서 접근 거부 시
public static void pointBalanceChange(BookAccessDenied bookAccessDenied) {
    Long userId = bookAccessDenied.getUserId();
    Long bookId = bookAccessDenied.getBookId();
    int requiredPoint = 500;

    // 가장 최근 포인트 합계 조회
    Long currentSum = 0L;
    Point latest = repository().findTopByUserIdOrderByChangeDateDesc(userId); // native 쿼리 기준
    if (latest != null) {
        currentSum = latest.getPointSum();
    }

    // 차감 가능할 경우
    if (currentSum >= requiredPoint) {
        Point point = new Point();
        point.setUserId(userId);
        point.setChangeDate(new Date());
        point.setChangePoint(-requiredPoint);
        point.setPointSum(currentSum - requiredPoint);
        point.setReason("책 접근 시 포인트 차감");

        repository().save(point);

        PointMinus pointMinus = new PointMinus(userId, bookId);
        pointMinus.publishAfterCommit();

    } else {
        System.out.println("포인트 부족: userId=" + userId + ", 현재 잔액=" + currentSum);
        // PointNotEnough 이벤트 발행 같은 것도 여기에 작성 가능
    }
}
public static void pointBalanceChange(UserRegistered userRegistered) {
    Long userId = userRegistered.getUserId();
    Boolean isKt = userRegistered.getIsKtMember();

    // 로그: 수신 이벤트 정보
    System.out.println("🔥 [PointService] UserRegistered 이벤트 수신 - userId=" + userId + ", isKtMember=" + isKt);

    // 지급 포인트 결정
    int grantPoint = Boolean.TRUE.equals(isKt) ? 1500 : 1000;
    String reason = Boolean.TRUE.equals(isKt) ? "KT 회원 보너스" : "Welcome Bonus";

    
    // 중복 지급 방지: 가장 최근 지급 내역의 reason을 체크
    Point latest = repository().findTopByUserIdOrderByChangeDateDesc(userId);
    if (latest != null && reason.equals(latest.getReason())) {
        System.out.println("⚠️ 이미 '" + reason + "' 지급됨 - 중복 방지로 건너뜀");
        return;
    }

    Long currentSum = latest != null ? latest.getPointSum() : 0L;


    // 새 포인트 기록 생성
    Point point = new Point();
    point.setUserId(userId);
    point.setChangeDate(new Date());
    point.setChangePoint(grantPoint);
    point.setPointSum(currentSum + grantPoint);
    point.setReason(reason);

    repository().save(point);

    // 로그: 지급 결과 출력
    System.out.println("✅ [PointService] 포인트 지급 완료 - userId=" + userId +
        ", 지급=" + grantPoint + ", 총합=" + point.getPointSum() +
        ", reason=\"" + reason + "\"");

    // 후속 이벤트 발행
    PointUpdated pointGranted = new PointUpdated(point);
    pointGranted.publishAfterCommit();
}

}