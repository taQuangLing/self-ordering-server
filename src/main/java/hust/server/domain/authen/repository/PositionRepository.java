package hust.server.domain.authen.repository;

import hust.server.domain.authen.entities.Position;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PositionRepository extends JpaRepository<Position, Long> {
    Optional<Position> getById(Long id);

    List<Position> getByCreatedBy(String userId);
}
