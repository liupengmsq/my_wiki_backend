package pengliu.me.backend.demo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Collections;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseDocument<T> {
    @JsonProperty("Success")
    private Boolean success = false;

    @JsonProperty("Errors")
    private List<String> errors;

    @JsonProperty("Result")
    private T result;

    public ResponseDocument() {

    }

    public ResponseDocument(T result) {
        setSuccess(true);
        setResult(result);
    }

    public ResponseDocument(Exception e) {
        setSuccess(false);
        setErrors(Collections.singletonList(e.getMessage()));
    }

    public static <T> ResponseDocument<T> successResponse(T t) {
        return new ResponseDocument<>(t);
    }

    public static <T> ResponseDocument<T> failedResponse(Exception e) {
        return new ResponseDocument<>(e);
    }

    public static ResponseDocument<?> emptyFailedResponse(List<String> errors) {
        ResponseDocument<?> response = new ResponseDocument<>();
        response.setSuccess(false);
        response.setErrors(errors);
        return  response;
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
}
