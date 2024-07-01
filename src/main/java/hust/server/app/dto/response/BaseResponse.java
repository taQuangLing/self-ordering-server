package hust.server.app.dto.response;

import hust.server.infrastructure.enums.MessageCode;
import lombok.*;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BaseResponse {
    private int code;
    private String message;
    private String description;

    public BaseResponse(MessageCode messageCode){
        this.code = messageCode.getCode();
        this.message = messageCode.getMessage();
        this.description = messageCode.getDescription();
    }
    public void setMessageCode(MessageCode messageCode){
        this.code = messageCode.getCode();
        this.message = messageCode.getMessage();
        this.description = messageCode.getDescription();
    }
}
