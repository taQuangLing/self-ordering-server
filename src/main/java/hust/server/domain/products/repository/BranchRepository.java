package hust.server.domain.products.repository;

import hust.server.domain.products.entity.Branch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BranchRepository extends JpaRepository<Branch, Long> {
    Optional<Branch> getByIdAndActive(Long branchId, int i);

    Optional<Branch> getById(Long branchId);

    Optional<Branch> getByCodeAndActive(String code, int i);

    List<Branch> getByCreatedBy(String userId);

    @Query(
            value = "select branches.* from branches where created_by = :userId " +
                    "order by active desc, " +
                    "created_at desc", nativeQuery = true
    )
    List<Branch> getByCreatedByAndSort(String userId);

    Optional<Branch> getByIdAndCreatedBy(Long id, String userId);
}
