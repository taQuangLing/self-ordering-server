package hust.server.domain.products.entity;

import hust.server.domain.products.dto.response.AdminMenuItemResponse;
import hust.server.domain.products.dto.response.AdminMenuResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@Builder
@Table(name = "menu_item")
@AllArgsConstructor
@NoArgsConstructor
public class MenuItem {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "product_id")
    private Long productId;

    @Column(name = "active")
    private Integer active;

    public AdminMenuItemResponse toAdminMenuItemResponse(){
        return AdminMenuItemResponse.builder()
                .id(id)
                .active(active == 1)
                .build();
    }
}
