package hust.server.domain.products.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminSizeRequest {
    private Long id;
    private String size;
    private Long price;
    private Integer isDefault;
}
