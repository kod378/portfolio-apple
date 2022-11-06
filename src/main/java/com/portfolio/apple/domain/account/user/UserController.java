package com.portfolio.apple.domain.account.user;

import com.portfolio.apple.domain.item.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.security.Principal;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserAccountService userAccountService;
    private final ItemService itemService;

    @GetMapping({"", "/"})
    public String userIndex(Model model, @PageableDefault Pageable pageable) {
        model.addAttribute("itemDtoPage",itemService.findPageWithResponseDto(pageable));
        return "index";
    }

    //TODO: 네이버 로그인을 새 창으로 수정하면 이 메소드는 필요 없을 수 있음.
    @GetMapping("/redirectReferer")
    public String redirectReferer(HttpServletRequest request, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("userLoginRequestMessage", "로그인이 필요한 서비스입니다.");
        Optional<String> referer = Optional.ofNullable(request.getHeader("Referer"));
        if (request.getHeader("Accept").contains("json")) { //api 요청 시 비로그인 상태이면
            return "redirect:/ApiUnAuthorized";
        }
        return "redirect:" + referer.orElse("/");
    }

    //TODO: 네이버 로그인을 새 창으로 수정하면 이 메소드는 필요 없을 수 있음.
    @GetMapping("/ApiUnAuthorized")
    @ResponseBody
    public ResponseEntity<String> ApiUnAuthorized() {
        return ResponseEntity.status(401).body("ApiUnAuthorized");
    }
}
