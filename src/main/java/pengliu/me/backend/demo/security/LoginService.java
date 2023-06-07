package pengliu.me.backend.demo.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import pengliu.me.backend.demo.config.JwtConfig;

import java.time.LocalDate;
import java.util.Date;

@Service
public class LoginService {
    @Autowired
    private JwtConfig jwtConfig;

    @Autowired
    private AuthenticationManager authenticationManager;

    public String login(LoginInfo loginInfo) {
        // #1 使用AuthenticationManager验证从前端传递来的username与password，它会去使用UserDetailService接口的实现类来获取数据库中
        //    对应username的password，并与前端传递来的password进行对比验证。
        //    如果验证成功，返回的是一个新的Authentication对象，它附带着这个user的权限信息GrantedAuthority（此权限信息是从UserDetailService接口获取来的，
        //    也就是从数据库查询来的，本示例是从数据库的role字段获取到的用户属于哪个角色，从角色的对应Enum获取到的对应的权限对象的list。）
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                loginInfo.getUserName(),
                loginInfo.getPassword());
        Authentication authenticationResult = authenticationManager.authenticate(authentication);
        if (authenticationResult == null) {
            throw new RuntimeException("用户登陆失败！！");
        }

        // #2 认证通过后，把用户的名字username与权限信息GrantedAuthority封装成jwt token的string返回给前端，这样前端在使用jwt token请求
        //    接口的时候，后端解析token中的权限信息判断是否有权限访问对应的接口
        return Jwts.builder()
                .setSubject(authenticationResult.getName()) // 这里是用户名
                .claim("authorities", authenticationResult.getAuthorities()) //这里是权限信息
                .setIssuedAt(new Date()) //token生成时间
                .setExpiration((java.sql.Date.valueOf(LocalDate.now()
                        .plusDays(jwtConfig.getTokenExpirationAfterDays())))) //token过期时间
                .signWith(Keys.hmacShaKeyFor(jwtConfig.getSecretKey().getBytes()))
                .compact();
    }
}
