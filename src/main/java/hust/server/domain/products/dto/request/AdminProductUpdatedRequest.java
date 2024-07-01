package hust.server.domain.products.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class AdminProductUpdatedRequest {
    private String userId;
    private Long id;
    private String name;
    private Long categoryId;
    private String description;
    private String img;
    private Integer status;
    private Integer hasSize;
    private Long price;
    private List<AdminSizeRequest> sizeRequestList;
}
