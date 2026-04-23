package br.webLogin.appLogin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.webLogin.appLogin.model.Usuario;
import br.webLogin.appLogin.repository.UsuarioRepository;

import org.springframework.validation.BindingResult;
import org.springframework.security.crypto.password.PasswordEncoder;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
public class LoginController {

    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    private UsuarioRepository ur;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/")
    public String root() {
        return "redirect:/index";
    }

    @GetMapping("/index")
    public String index() {
        return "index";
    }

    @GetMapping("/login")
    public String login() {
        return "login";  // Retorna o template login.html
    }


    @GetMapping("/cadastro")
    public String cadastro(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "cadastro";
    }


    @PostMapping("/cadastroUsuario")//cadastroUsuario significa que o form de cadastro tem action="/cadastroUsuario" entao isso fará com que o metodo cadastroUsuario seja chamado quando o form for submetido
    public String cadastroUsuario(
        @Valid Usuario usuario,
        BindingResult result,
        @RequestParam("confirmSenha") String confirmSenha,
        Model model,
        RedirectAttributes redirectAttributes
    ) {
        if (!usuario.getSenha().equals(confirmSenha)) {
            result.rejectValue("senha", "senha.mismatch", "As senhas informadas nao coincidem.");
        }

        if (ur.findByEmail(usuario.getEmail()) != null) {
            result.rejectValue("email", "email.duplicado", "Este email ja esta cadastrado.");
        }

        if (result.hasErrors()) {
            model.addAttribute("usuario", usuario);
            return "cadastro";
        }

        usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
        ur.save(usuario);
        redirectAttributes.addAttribute("success", "true");
        return "redirect:/login";
    }

}
