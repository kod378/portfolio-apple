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
- 기본적으로 SpringSecurity를 활용해서 구현. 
- 관리자는 formLogin을 활용해서 로그인/회원가입 구현. 
- 일반유저는 OAuth2.0을 활용해서 로그인처리(네이버 로그인 연동). 
- 자세한 부분은 /domain/account/ 패키지위치 참조

## 2. 이미지 업로드/불러오기
```java
    public List<ItemFile> uploadAndSaveFiles(MultipartFile representationFile, MultipartFile[] files) throws Exception {
        List<String> uploadedFileNames = uploadFiles(representationFile, files);
        return makeItemFiles(representationFile, files, uploadedFileNames);
    }
    
```
```java
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/uploadImage/**")
                .addResourceLocations("file:///E:/upload/");
    }
```
- MultipartFile 인터페이스를 활용해서 이미지 파일 요청을 받아서 처리
- 프로젝트 외부에 저장한 파일을 불러오기 위해서 핸들러 추가
- 자세한 부분은 ItemFileService.java 참조
- MultipartFile 관련은 블로그를 참조해도 좋을 듯 합니다.

## 3. 주문
```java
    @PostMapping("/orders")
    public String orders(@CurrentUser UserAccount userAccount, OrdersRequestDTO ordersRequestDTO, RedirectAttributes redirectAttributes) {
        try {
            Orders orders = orderService.order(userAccount, ordersRequestDTO);
            redirectAttributes.addAttribute("ordersId", orders.getId());
            return "redirect:/orders/complete";
        } catch (NoShoppingItemException | IllegalStateException e) {
            redirectAttributes.addFlashAttribute("message", e.getMessage());
            return "redirect:/";
        } catch (Exception e) {
            log.error("주문 오류", e);
            e.printStackTrace();
        }
        return "redirect:/";
    }
```
```java
    @Transactional
    public Orders order(UserAccount userAccount, OrdersRequestDTO ordersRequestDTO) throws Exception{
        List<ShoppingItem> findShoppingItems = shoppingItemService.findAllByUserAccountAndIdIn(userAccount, ordersRequestDTO.getCheckShoppingItemIds());
        if (findShoppingItems.isEmpty()) {
            throw new NoShoppingItemException("장바구니에 담긴 상품이 없습니다.");
        }

        checkStock(findShoppingItems);
        final int payment = findShoppingItems.stream().mapToInt(shoppingItem -> shoppingItem.getItem().getPrice() * shoppingItem.getQuantity()).sum()
                + DeliveryFee.DELIVERY_FEE;
        Address address = getAddressFromDTO(ordersRequestDTO);
        Delivery delivery = deliveryService.savePrePareDelivery(address);
        Orders orders = ordersService.saveOrders(userAccount, delivery, payment);
        checkStock(findShoppingItems);
        orderedItemService.saveAllAndChangeStockOfItem(findShoppingItems, orders);
        shoppingItemService.deleteByUserAccountAndIdIn(userAccount, findShoppingItems);
        return orders;
    }
```
- 주문하기 기능이 Orders, Delivery, OrderedItem 3가지 엔티티를 한번에 저장하는 기능이라서 한 트랜잭션 안에서 동작하도록 OrderService에서 각 서비스를 호출하도록 구현

# 기타
- 테스트 코드 확인


