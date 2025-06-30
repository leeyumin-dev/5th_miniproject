package project.infra;

import java.util.List;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import project.domain.*;

@RepositoryRestResource(
    collectionResourceRel = "subscriptionLists",
    path = "subscriptionLists"
)
public interface SubscriptionListRepository
    extends PagingAndSortingRepository<SubscriptionList, Long> {

    // 사용자의 구독 목록 조회
    // 유저의 구독 목록을 가져올 때 사용
    List<Subscription> findByUserId(Long userId);
}
