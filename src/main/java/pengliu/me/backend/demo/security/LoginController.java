package pengliu.me.backend.demo.security;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pengliu.me.backend.demo.ResponseDocument;
import pengliu.me.backend.demo.config.JwtConfig;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class LoginController {
    @Autowired
    private LoginService loginService;

    @Autowired
    private JwtConfig jwtConfig;

    @Autowired
    private ModelMapper modelMapper;

    @PostMapping("/user/login")
    public ResponseEntity<String> login(@RequestBody LoginDTO loginDTO) throws JsonProcessingException {
        // 转换DTO对象
        LoginInfo loginInfo = modelMapper.map(loginDTO, LoginInfo.class);

        // 将返回的jwtToken放入response header中，返回给前端
        String jwtToken = loginService.login(loginInfo);

        Map<String, String> jwtTokenHeader = new HashMap<>();
        jwtTokenHeader.put(jwtConfig.getAuthorizationHeader(), jwtConfig.getTokenPrefix() + jwtToken);

        // 设置response header为：Authorization: Bearer $JWTToken
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set(jwtConfig.getAuthorizationHeader(), jwtTokenHeader.get(jwtConfig.getAuthorizationHeader()));
        responseHeaders.setContentType(MediaType.APPLICATION_JSON);

        // 同时将JWT token也放入ResponseDocument对象中，将其序列化为String并放入到ResponseEntity中返回
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        ResponseDocument<?> responseDocument = ResponseDocument.successResponse(jwtTokenHeader);
        String responseDocumentString = objectMapper.writeValueAsString(responseDocument);

        return new ResponseEntity<>(responseDocumentString, responseHeaders, HttpStatus.OK);
    }
}
