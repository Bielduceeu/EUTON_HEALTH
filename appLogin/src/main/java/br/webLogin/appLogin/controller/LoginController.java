package br.webLogin.appLogin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.beans.factory.annotation.Autowired;

import br.webLogin.appLogin.repository.UsuarioRepository;
import br.webLogin.appLogin.model.Usuario;

import org.springframework.validation.BindingResult;
import jakarta.validation.Valid;

@Controller
public class LoginController {

    @Autowired
    private UsuarioRepository ur;


    @GetMapping("/login")
    public String login() {
        return "login";  // Retorna o template login.html
    }

    @GetMapping("/cadastroUsuario")
    public String cadastro() {
        return "cadastro";  // Retorna o template register.html
    }

    @PostMapping("/cadastroUsuario")
    public String cadastroUsuario(@Valid Usuario usuario, BindingResult result) {
        
        if(result.hasErrors()) {
            return "register";
        }
    
        ur.save(usuario);
        return "redirect:/login";
    }
}