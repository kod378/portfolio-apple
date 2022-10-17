package com.portfolio.apple.domain.account.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
@RequestMapping("admin")
@RequiredArgsConstructor
public class AdminController{

    private final AdminAccountService adminAccountService;

    @GetMapping({"", "/"})
    public String adminIndex(@AuthenticationPrincipal Admin admin ,Model model) {
        return "admin/index";
    }

    @GetMapping("/login")
    public String adminLogin(Model model) {
        return "admin/login";
    }

    @GetMapping("/join")
    public String adminjoin(Model model) {
        model.addAttribute("adminJoinFormDTO", new AdminJoinFormDTO());
        return "admin/join";
    }

    @PostMapping("/join")
    public String adminjoinPost(@Valid AdminJoinFormDTO adminJoinFormDTO, Errors errors, RedirectAttributes redirectAttributes) {
        if (errors.hasErrors() || !adminJoinFormDTO.getPassword().equals(adminJoinFormDTO.getPasswordConfirm())) {
            return "admin/join";
        }
        adminAccountService.saveAdminAccount(adminJoinFormDTO);
        redirectAttributes.addFlashAttribute("message", "회원가입을 성공하였습니다.");
        return "redirect:/admin/login";
    }

    @GetMapping("/TEST")
    public String adminTEST(Model model) {
        return "admin/TEST";
    }

}
