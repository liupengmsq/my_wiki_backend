package pengliu.me.backend.demo.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;
import pengliu.me.backend.demo.ResponseDocument;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

// 登陆成功后，没有权限访问对应API的时候的错误处理器
@Component
public class AccessDeniedHandlerImpl implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        ResponseDocument<?> responseDocument = ResponseDocument.failedResponse(new Exception("未授权访问此接口！！"), HttpStatus.UNAUTHORIZED);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String responseDocumentString = objectMapper.writeValueAsString(responseDocument);

        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");
        response.setStatus(HttpStatus.OK.value()); // 错误代码在response body中，这个response header设置为200，以便让前端获取到body中的error信息
        response.getWriter().print(responseDocumentString);
    }
}
