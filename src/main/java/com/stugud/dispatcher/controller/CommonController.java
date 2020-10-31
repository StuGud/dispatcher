package com.stugud.dispatcher.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Created By Gud on 2020/11/1 12:26 上午
 */
@Controller
public class CommonController {

    @GetMapping("/loginSuccess")
    public String login(Authentication authentication){
        if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))){
            return "redirect:admin/tasks";
        }else{
            return "redirect:employee/tasks";
        }
    }
}
