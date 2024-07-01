package hust.server.app.exception;

import hust.server.infrastructure.enums.MessageCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ApiException extends RuntimeException{
    private int code;
    private String message;
    private String description;
    private Object data;
    private Exception ex;

    public ApiException(int code, String message, String description) {
        this.code = code;
        this.message = message;
        this.description = description;
    }
    public ApiException(MessageCode messageCode){
        this.code = messageCode.getCode();
        this.message = messageCode.getMessage();
        this.description = messageCode.getDescription();
    }
    public ApiException(Exception ex, MessageCode messageCode){
        this.ex = ex;
        this.code = messageCode.getCode();
        this.message = messageCode.getMessage();
        this.description = messageCode.getDescription();
    }
    public ApiException(MessageCode messageCode, Object data){
        this.code = messageCode.getCode();
        this.message = messageCode.getMessage();
        this.description = messageCode.getDescription();
        this.data = data;
    }
    public ApiException(Exception ex, MessageCode messageCode, Object data){
        this.ex = ex;
        this.code = messageCode.getCode();
        this.message = messageCode.getMessage();
        this.description = messageCode.getDescription();
        this.data = data;
    }
    public void setMessageCode(Exception ex, MessageCode messageCode){
        this.ex = ex;
        this.code = messageCode.getCode();
        this.message = messageCode.getMessage();
        this.description = messageCode.getDescription();
    }
}
