package pengliu.me.backend.demo.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import pengliu.me.backend.demo.ResponseDocument;
import pengliu.me.backend.demo.error.WikiException;

@ControllerAdvice
public class MyExceptionAdvice extends ResponseEntityExceptionHandler {
    @ResponseBody
    @ExceptionHandler( WikiException.class)
    public ResponseEntity<Object> generalExceptionHandler(Exception ex) {
        ResponseDocument<?> responseDocument = ResponseDocument.failedResponse(ex, HttpStatus.INTERNAL_SERVER_ERROR);
        return new ResponseEntity<>(responseDocument, responseDocument.getHttpStatus());
    }
}
