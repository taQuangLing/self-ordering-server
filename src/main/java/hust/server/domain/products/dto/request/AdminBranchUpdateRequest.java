package hust.server.domain.products.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AdminBranchUpdateRequest {
    private Long id;
    private String createdBy;
    private String name;
    private String address;
    private String city;
    private String town;
    private Integer status;
    private String logo;
    private List<AdminMenuItemRequest> menuItemRes;
}
