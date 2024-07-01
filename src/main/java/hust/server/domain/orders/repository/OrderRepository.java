package hust.server.domain.orders.repository;

import hust.server.domain.orders.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> getByUserId(String userId);

    Optional<Order> getById(Long id);

    List<Order> getByBranchId(Long id);

    @Query(value = "" +
            "select orders.* from orders, branches " +
            "where " +
                "branches.id = orders.branch_id and " +
                "branches.created_by = :userId " +
            "order by created_at desc",
            nativeQuery = true
    )
    List<Order> getByCreatedBy(String userId);
}
