package hust.server.domain.authen.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GuestTokenRequest {
    String tableNumber;
    Long branchId;
}
