package hust.server.domain.products.repository;

import hust.server.domain.products.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    List<Category> getByActive(Integer active);

    Optional<Category> getById(Long categoryId);

    List<Category> getByCreatedBy(String userId);
}
