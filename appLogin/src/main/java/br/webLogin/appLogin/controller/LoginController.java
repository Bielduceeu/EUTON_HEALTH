package br.webLogin.appLogin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.webLogin.appLogin.model.Usuario;
import br.webLogin.appLogin.service.UsuarioService;

import org.springframework.validation.BindingResult;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Controlador responsável por gerenciar as requisições de login, cadastro e autenticação.
 * Camada de apresentação: Recebe requisições HTTP, valida entrada e coordena com a camada Service.
 * Não contém lógica de negócio - delega para UsuarioService.
 */
@Controller
public class LoginController {

    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    private UsuarioService usuarioService;

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


    /**
     * GET /cadastro: Exibe o formulário de cadastro
     * Importante: Adiciona um objeto Usuario vazio ao Model para que o Thymeleaf
     * tenha uma referência quando usar th:object="${usuario}" e #fields expressions.
     * Sem isso, o template lança "Neither BindingResult nor plain target object" error.
     * 
     * @param model Model para passar dados ao template
     * @return Nome do template a renderizar (cadastro.html)
     */
    @GetMapping("/cadastro")
    public String cadastro(Model model) {
        model.addAttribute("usuario", new Usuario());
        logger.info("Carregando página de cadastro");
        return "cadastro";
    }
            


    /**
     * POST /cadastroUsuario: Processa o cadastro de um novo usuário
     * Fluxo:
     * 1. Valida se as senhas conferem (validação de negócio)
     * 2. Valida se email já existe (validação de negócio)
     * 3. Se houver erro: Retorna "cadastro" com os objetos Usuario e BindingResult no Model
     *    (Thymeleaf precisa desses objetos para renderizar erros corretamente)
     * 4. Se sucesso: Delega para UsuarioService.cadastrarUsuario() e redireciona para login
     * 
     * Importante sobre BindingResult:
     * - Deve estar como parâmetro LOGO APÓS o @Valid Usuario
     * - Spring o popula automaticamente e o adiciona ao Model
     * - Thymeleaf acessa via ${#fields}
     * 
     * @param usuario Objeto Usuario populado com dados do formulário (validações @NotEmpty aplicadas)
     * @param result BindingResult com erros de validação JSR-303
     * @param confirmSenha Parâmetro adicional não mapeado ao objeto Usuario
     * @param model Model para passar dados ao template em caso de erro
     * @param redirectAttributes Para passar mensagens de sucesso via redirect
     * @return "cadastro" se erro, ou redirect:/login se sucesso
     */
    @PostMapping("/cadastroUsuario")
    public String cadastroUsuario(
        @Valid Usuario usuario,
        BindingResult result,
        @RequestParam("confirmSenha") String confirmSenha,
        Model model,
        RedirectAttributes redirectAttributes
    ) {
        // Validação de negócio: Senhas conferem?
        if (!usuarioService.senhasConferem(usuario.getSenha(), confirmSenha)) {
            result.rejectValue("senha", "senha.mismatch", "As senhas informadas nao coincidem.");
        }

        // Validação de negócio: Email já existe?
        if (usuarioService.emailJaExiste(usuario.getEmail())) {
            result.rejectValue("email", "email.duplicado", "Este email ja esta cadastrado.");
        }

        // Se houver erros: Retorna o formulário com o objeto Usuario no Model
        // Crítico: O objeto usuario DEVE estar no Model para Thymeleaf renderizar erros
        if (result.hasErrors()) {
            model.addAttribute("usuario", usuario);
            logger.warn("Erro ao cadastrar usuário: email={}, erros={}", usuario.getEmail(), result.getErrorCount());
            return "cadastro";  // Thymeleaf acessa usuario e BindingResult do Model
        }

        // Sucesso: Delega para o Service (senha será criptografada lá)
        try {
            usuarioService.cadastrarUsuario(usuario);
            logger.info("Usuário cadastrado com sucesso: email={}", usuario.getEmail());
            redirectAttributes.addAttribute("success", "true");
            return "redirect:/login";
        } catch (Exception e) {
            logger.error("Erro ao salvar usuário: email={}", usuario.getEmail(), e);
            model.addAttribute("usuario", usuario);
            result.reject("global.error", "Erro ao processar cadastro. Tente novamente.");
            return "cadastro";
        }
    }
}
