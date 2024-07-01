package hust.server.domain.products.entity;

import hust.server.domain.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Builder
@Table(name = "menu")
@AllArgsConstructor
@NoArgsConstructor
public class Menu extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "branch_id")
    private Long branchId;

    @Column(name = "summary")
    private String summary;

    @Column(name = "active")
    private Integer active;

    @Column(name = "slug")
    private String slug;

    @Column(name = "created_by")
    private String createdBy;

    @OneToMany
    @JoinColumn(name = "menu_id")
    private List<MenuItem> menuItemList;
}
