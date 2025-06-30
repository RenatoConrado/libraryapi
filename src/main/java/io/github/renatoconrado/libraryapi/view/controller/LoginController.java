package io.github.renatoconrado.libraryapi.view.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

public @Controller class LoginController {

    @GetMapping("/login")
    public String login() {
        return "login";
    }
}
