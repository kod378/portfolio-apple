package com.portfolio.apple.domain.account.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserAccountService userAccountService;

    @GetMapping({"", "/"})
    public String userIndex() {
        return "index";
    }

    @GetMapping("/redirectReferer")
    public String redirectReferer(HttpServletRequest request, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("userLoginRequestMessage", "로그인이 필요한 서비스입니다.");
        Optional<String> referer = Optional.ofNullable(request.getHeader("Referer"));
        return "redirect:" + referer.orElse("/");
    }

    @GetMapping("/freeList")
    public String freeList() {
        return "user/itemList";
    }

    @GetMapping("/itemList")
    public String itemList() {
        return "user/itemList";
    }

    @GetMapping("/itembuyTest")
    public String itembuyTest(@CurrentUser UserAccount userAccount, Model model) {
        if(userAccount != null) {
            model.addAttribute("userAccount", userAccount);
        }
        return "user/itembuyTest";
    }
}
