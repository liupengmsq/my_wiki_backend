package pengliu.me.backend.demo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import pengliu.me.backend.demo.filter.JwtTokenVerifyFilter;
import pengliu.me.backend.demo.handler.AccessDeniedHandlerImpl;
import pengliu.me.backend.demo.handler.LoginFailHandlerImpl;
import pengliu.me.backend.demo.security.ApplicationUserDetailService;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class ApplicationSecurityConfig {
    @Autowired
    private ApplicationUserDetailService applicationUserDetailService;

    @Autowired
    private JwtConfig jwtConfig;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10);
    }

    @Autowired
    private LoginFailHandlerImpl loginFailHandler;

    @Autowired
    private AccessDeniedHandlerImpl accessDeniedHandler;

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfiguration) throws Exception {
        return authConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // 关闭csrf保护功能
                .csrf().disable()
                // 添加一个CorsFilter，它会使用我们的pengliu.me.backend.demo.config.CorsConfig配置的内容验证http request是否可以跨域
                .cors()
                .and()
                // 不通过Session获取SecurityContext
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                // 加入验证客户端传来的token的正确性的filter
                .addFilterBefore(new JwtTokenVerifyFilter(jwtConfig), UsernamePasswordAuthenticationFilter.class)
                .authorizeRequests()
                // 允许这些路径的不受限制的访问
                .antMatchers("/api/user/login").permitAll()
                .antMatchers(HttpMethod.GET,  "/api/nav/**", "/api/wiki/**").permitAll()
                // 除上面的url以外的所有请求都需要认证
                .anyRequest().authenticated();

        // 配置登陆失败与授权失败的错误处理器
        http.exceptionHandling()
                .authenticationEntryPoint(this.loginFailHandler) // 登陆失败的处理器
                .accessDeniedHandler(this.accessDeniedHandler); // 登陆成功后，没权限访问的处理器
        return http.build();
    }

    public static void main(String[] args) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        System.out.println(passwordEncoder.encode("1234"));
    }
}
