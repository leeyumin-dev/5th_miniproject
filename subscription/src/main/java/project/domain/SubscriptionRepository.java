package project.domain;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import project.domain.*;

//<<< PoEAA / Repository
@RepositoryRestResource(
    collectionResourceRel = "subscriptions",
    path = "subscriptions"
)

// JPA를 통해서 DB에서 구독 정보를 조회하거나 저장한다.
public interface SubscriptionRepository extends PagingAndSortingRepository<Subscription, Long> {

    // 사용자가 해당 도서를 구독했는지 여부 확인
    boolean existsByUserIdAndBookId(Long userId, Long bookId);


    // 사용자 + 도서 조합 조회
    // 중복 구독 방지 또는 상세 정보 조회 시 사용
    Optional<Subscription> findByUserIdAndBookId(Long userId, Long bookId);
}
