package hust.server.app.exception;

import hust.server.app.dto.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalControllerExceptionHandler{
    private static final Logger LOGGER = LogManager.getLogger(GlobalControllerExceptionHandler.class);

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<?> handleConversion(Exception ex) {
        Response response = new Response();
        response.setMessage(ex.getMessage());
        if (ex instanceof ApiException) {
            ApiException apiException = (ApiException) ex;
            LOGGER.error("ERROR: ", apiException.getEx());
            response.setCode(apiException.getCode());
            response.setMessage(apiException.getMessage());
            response.setDescription(apiException.getDescription());
            response.setData(apiException.getData());
        } else if (ex instanceof InternalAuthenticationServiceException){
            ApiException apiException = (ApiException) ex.getCause();
            LOGGER.error("ERROR: ", apiException.getEx());
            response.setCode(apiException.getCode());
            response.setMessage(apiException.getMessage());
            response.setDescription(apiException.getDescription());
            response.setData(apiException.getData());
        }
        else {
            LOGGER.error("ERROR: ", ex);
            response.setCode(500);
            response.setMessage("System Error !");
            response.setDescription("Hệ thống đang bảo trì");
        }
        LOGGER.warn(response);
        return ResponseEntity.ok(response);
    }
}
