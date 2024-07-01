package hust.server.domain.products.repository;

import hust.server.domain.products.entity.MenuItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository

public interface MenuItemRepository extends JpaRepository<MenuItem, Long> {

    @Modifying
    @Query(value = "update menu_item set active = :active where id = :id ", nativeQuery = true)
    void updateActive(Long id, Integer active);
}
