package hust.server.domain.authen.repository;

import hust.server.domain.authen.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    public Optional<User> findByUsername(String username);

    public Optional<User> findByUsernameAndPassword(String username, String password);

    Optional<User> getById(String userId);

    Optional<User> getByIdAndActive(String userId, Integer i);
    @Query(value = "SELECT u.* from users u, branches where " +
            "branches.created_by = :userId and " +
            "branches.id = u.branch_id and " +
            "u.role like \"USER\" " +
            "order by active desc, " +
            "created_at desc", nativeQuery = true)
    List<User> getByAdminUserId(String userId);

    @Query(value = "SELECT COUNT(*) FROM users WHERE branch_id = :branchId", nativeQuery = true)
    int countEmployee(Long branchId);

    @Query(value = "select users.* from users, positions where " +
            "users.branch_id = :id and " +
            "positions.id = users.position_id and " +
            "positions.name = \"Quản lý\" and " +
            "users.active = 1 ", nativeQuery = true)
    List<User> getManagerUser(Long id);

    @Query(value = "select users.* from users, positions where " +
            "users.position_id = positions.id and " +
            "positions.name = \"Quản lý\" and " +
            "users.created_by = :userId and " +
            "users.active = 1 ", nativeQuery = true
    )
    List<User> getManagerUserList(String userId);
}
