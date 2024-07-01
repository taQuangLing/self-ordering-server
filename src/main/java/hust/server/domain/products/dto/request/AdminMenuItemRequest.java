package hust.server.domain.products.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminMenuItemRequest {
    private Long id;
    private Boolean active;
}
