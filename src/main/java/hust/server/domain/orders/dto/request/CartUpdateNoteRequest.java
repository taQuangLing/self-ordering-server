package hust.server.domain.orders.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CartUpdateNoteRequest {
    private Long id;
    private String note;
}
