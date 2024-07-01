package hust.server.app.service;

import hust.server.app.dto.response.BaseResponse;
import hust.server.app.dto.response.Response;
import hust.server.app.dto.response.ResponseList;
import hust.server.infrastructure.enums.MessageCode;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static hust.server.infrastructure.enums.MessageCode.*;

public class ResponseFactory {
    public static <T> ResponseEntity<Response<T>> response(MessageCode messageCode, T data){
        Response<T> response = new Response<>();
        response.setMessageCode(messageCode);
        response.setData(data);
        return ResponseEntity.ok(response);
    }
    public static ResponseEntity<BaseResponse> response(MessageCode messageCode){
        BaseResponse reponse = new BaseResponse(messageCode);
        return ResponseEntity.ok(reponse);
    }
    public static <T> ResponseEntity<Response<T>> response(T data){
        return response(SUCCESS, data);
    }
    public static ResponseEntity<BaseResponse> creationResponse(){
        return response(SUCCESS);
    }
    public static <T> ResponseEntity<ResponseList<T>> response(List<T> data){
        ResponseList<T> response = new ResponseList<>();
        response.setData(data);
        response.setMessageCode(SUCCESS);
        return ResponseEntity.ok(response);
    }
}
