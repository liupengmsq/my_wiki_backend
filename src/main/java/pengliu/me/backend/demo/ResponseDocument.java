package pengliu.me.backend.demo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.http.HttpStatus;

import java.util.Collections;
import java.util.List;

import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseDocument<T> {
    @JsonProperty("Success")
    private Boolean success = false;

    @JsonProperty("Errors")
    private List<String> errors;

    @JsonProperty("Result")
    private T result;

    @JsonProperty("LocalDateTime")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "uuuu-MM-dd'T'HH:mm:ss.SSS")
    private LocalDateTime localDateTime;

    @JsonProperty("HttpStatus")
    private HttpStatus httpStatus;

    public ResponseDocument() {
        setHttpStatus(HttpStatus.OK);
        setLocalDateTime(LocalDateTime.now());
    }

    public ResponseDocument(T result) {
        this();
        setSuccess(true);
        setResult(result);
    }

    public ResponseDocument(Exception e) {
        this(e, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public ResponseDocument(Exception e, HttpStatus httpStatus) {
        this();
        setSuccess(false);
        setHttpStatus(httpStatus);
        setErrors(Collections.singletonList(e.getMessage()));
    }

    public static <T> ResponseDocument<T> successResponse(T t) {
        return new ResponseDocument<>(t);
    }

    public static ResponseDocument<?> emptySuccessResponse() {
        ResponseDocument<?> response = new ResponseDocument<>();
        response.setSuccess(true);
        return response;
    }

    public static <T> ResponseDocument<T> failedResponse(Exception e) {
        return new ResponseDocument<>(e);
    }

    public static <T> ResponseDocument<T> failedResponse(Exception e, HttpStatus httpStatus) {
        return new ResponseDocument<>(e, httpStatus);
    }

    public static ResponseDocument<?> emptyFailedResponse(List<String> errors) {
        return emptyFailedResponse(errors, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public static ResponseDocument<?> emptyFailedResponse(List<String> errors, HttpStatus httpStatus) {
        ResponseDocument<?> response = new ResponseDocument<>();
        response.setSuccess(false);
        response.setErrors(errors);
        response.setHttpStatus(httpStatus);
        return response;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public List<String> getErrors() {
        return errors;
    }

    public void setErrors(List<String> errors) {
        this.errors = errors;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }

    public LocalDateTime getLocalDateTime() {
        return localDateTime;
    }

    public void setLocalDateTime(LocalDateTime localDateTime) {
        this.localDateTime = localDateTime;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public void setHttpStatus(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }
}
