package io.github.renatoconrado.libraryapi.view.controller;

import io.github.renatoconrado.libraryapi.users.model.User;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import static java.lang.String.format;

public @Controller class ViewController {

    @GetMapping("login")
    public String login() {
        return "login";
    }

    @ResponseBody
    @GetMapping("/")
    private String home(Authentication authentication) {
        if (authentication.getPrincipal() instanceof User user) {
            return format(
                """
                <p>
                    ID      : %s<br/>
                    Username: %s<br/>
                    Email   : %s<br/>
                    Password: %s<br/>
                    Roles   : %s<br/>
                <p/>
                """,
                user.getId(),
                user.getLogin(),
                user.getEmail(),
                user.getPassword(),
                user.getRoles()
            );
        }
        return authentication.getPrincipal().toString().replace(",", ",<br/>");
    }
}
