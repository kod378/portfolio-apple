# portfolio-apple
토이프로젝트 사과 판매 사이트

## 1. 토이 프로젝트 목적
퇴사 후 스프링부트와 JPA를 공부했습니다. 공부했다는 증거가 필요했기에 토이프로젝트를 구상했습니다.
프로젝트를 진행하면서 생긴 문제는 블로그에 기록했습니다.(https://considerate-firefly.tistory.com/)

## 2. DB구조(클릭 시 잘 보임)
![apple_DB6](https://user-images.githubusercontent.com/37237755/202852359-ff827e82-25fd-4512-a5eb-888571a75fc8.png)
개발DB는 H2 사용했습니다.

## 3. 사용한 라이브러리(개별 설정한 라이브러리)
      - MapStruct(https://mapstruct.org/)
      - QueryDSL(http://querydsl.com/)

# 기능 정리
## 1. 로그인/회원가입
```java
    @Bean
    @Order(1)
    public SecurityFilterChain AdminFilterChain(HttpSecurity http) throws Exception {
        return http.antMatcher("/admin/**")
                .csrf().disable()
                .authorizeRequests(
                        authorizeRequests -> authorizeRequests
                                .antMatchers("/admin/login", "/admin/join").permitAll()
                                .antMatchers("/admin/**").hasRole(Role.ADMIN.name())
                                .anyRequest().authenticated()
                ).formLogin(
                        formLogin -> formLogin
                                .usernameParameter("accountId")
                                .loginPage("/admin/login").permitAll()
                                .defaultSuccessUrl("/admin", true)
                ).build();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf().disable()
                    .headers().frameOptions().disable()
                .and()
                    .authorizeRequests()
                    .antMatchers("/", "/h2-console/**", "/uploadImage/**", "/item/**", "/ApiUnAuthorized", "/exception").permitAll()
                    .antMatchers("/api/**").hasRole(Role.USER.name())
                    .anyRequest().authenticated()
                .and()
                    .oauth2Login()
                    .loginPage("/redirectReferer").permitAll()
                    .defaultSuccessUrl("/", true)
                    .userInfoEndpoint()
                    .userService(customOAuth2UserService)
                    .and()
                .and()
                .logout().logoutSuccessUrl("/")
                .and().build();
    }
```
자세한 부분은 SecurityConfig.java 참조
기본적으로 SpringSecurity를 활용해서 구현
관리자는 formLogin을 활용해서 로그인/회원가입 구현
일반유저는 OAuth2.0을 활용해서 로그인처리(네이버 로그인 연동)


