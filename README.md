### 1.新用户注册
代码未实现新用户注册的功能。需要去数据库application_user中添加新的用户。其中的enabled字段需要是1，password字段需要使用如下方法加密原始明文密码：
```java
  BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
  String encoded = passwordEncoder.encode("1234");
  System.out.println(encoded);
```
如上，其中的"1234"就是原始的明文密码。加密后的密码在存放在encoded字符串中。 

### 2. 登陆接口
此登陆接口不受访问限制，任何人都可以访问。配置在这里： pengliu.me.backend.demo.config.ApplicationSecurityConfig.securityFilterChain
前端会通过这个API提交用户名与密码。
此API会去调用loginService.login方法验证用户名与密码是否与数据库中的相同，如果相同，就生成jwtToken（这个token就是一个加密字符串，
将用户名与其对应的权限信息加密成为一个字符串发给前端，这样每次前端发送请求过来的时候都可以附带这个jwtToken在request header中，后端会通过自定义
的filter `pengliu.me.backend.demo.filter.JwtTokenVerifyFilter` 解析jwtToken，获取权限信息，来鉴定其中附带的权限能否访问当前想要访问的
接口。）

- Java class: `pengliu.me.backend.demo.security.LoginController`
- URL: POST `/api/user/login`

### 3. 鉴权过滤器filter
登陆成功后，会返回给前端一个jwtToken，其中包含了用户名与其的权限信息，之后用户请求到后端接口的header中可以包含这个jwtToken，后端使用自定义的
filter `pengliu.me.backend.demo.filter.JwtTokenVerifyFilter` 将其从header中拿出来，并将用户名与权限信息放入
到对象`org.springframework.security.core.Authentication`中，将这个对象放入到全局的上下文对象中，如下：
`SecurityContextHolder.getContext().setAuthentication(authentication);`，之后就是spring security自己的事情了，它会拿到你的
authentication对象来帮你鉴权。

### 4. 登陆失败与鉴权失败（没权限访问）的错误处理
当出现登陆失败的时候，使用的是`pengliu.me.backend.demo.handler.LoginFailHandlerImpl`来订制返回给前端response。
当登陆成功，但是用户没有权限访问某个接口的时候（鉴权失败），使用的是`pengliu.me.backend.demo.handler.AccessDeniedHandlerImpl`
来订制返回给前端的response。
