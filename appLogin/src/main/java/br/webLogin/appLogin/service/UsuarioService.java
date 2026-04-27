package br.webLogin.appLogin.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import br.webLogin.appLogin.model.Usuario;
import br.webLogin.appLogin.repository.UsuarioRepository;

/**
 * Camada Service: Contém toda a lógica de negócio relacionada a Usuários.
 * Responsabilidades:
 * - Validar regras de negócio (ex: email já existe)
 * - Criptografar senhas
 * - Coordenar operações com o Repository
 * - Centralizar a lógica para reutilização em diferentes Controllers
 */
@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Busca um usuário pelo email.
     * Usado principalmente na autenticação e validação de email duplicado.
     * 
     * @param email Email do usuário
     * @return Usuario ou null se não encontrado
     */
    public Usuario buscarPorEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }

    /**
     * Cadastra um novo usuário no sistema.
     * Responsabilidades:
     * - Verifica se email já existe (chamador deve validar antes)
     * - Criptografa a senha
     * - Salva no banco de dados
     * 
     * @param usuario Objeto Usuario com dados de cadastro (senha em texto plano)
     * @return Usuario salvo com ID gerado
     */
    public Usuario cadastrarUsuario(Usuario usuario) {
        // Criptografa a senha antes de salvar
        usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
        return usuarioRepository.save(usuario);
    }

    /**
     * Valida se o email já está registrado no sistema.
     * 
     * @param email Email a validar
     * @return true se email já existe, false caso contrário
     */
    public boolean emailJaExiste(String email) {
        return usuarioRepository.findByEmail(email) != null;
    }

    /**
     * Valida se as senhas conferem (usado durante cadastro).
     * 
     * @param senha Primeira senha
     * @param confirmSenha Confirmação da senha
     * @return true se são iguais
     */
    public boolean senhasConferem(String senha, String confirmSenha) {
        return senha != null && senha.equals(confirmSenha);
    }
}
