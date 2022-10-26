package com.portfolio.apple.domain.account.user;

import com.portfolio.apple.domain.item.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
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
    public String userIndex(Model model, @PageableDefault Pageable pageable, @CurrentUser UserAccount userAccount) {
        model.addAttribute("itemDtoPage",itemService.findPageWithResponseDto(pageable));
        if(userAccount != null) {
            model.addAttribute("userName", userAccount.getName());
        }

        return "index";
    }

    @GetMapping("/redirectReferer")
    public String redirectReferer(HttpServletRequest request, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("userLoginRequestMessage", "로그인이 필요한 서비스입니다.");
        Optional<String> referer = Optional.ofNullable(request.getHeader("Referer"));
        return "redirect:" + referer.orElse("/");
    }

}
