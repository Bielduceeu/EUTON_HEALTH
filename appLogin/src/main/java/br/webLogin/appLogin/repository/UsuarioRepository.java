package br.webLogin.appLogin.repository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import br.webLogin.appLogin.model.Usuario;

@Repository
public interface UsuarioRepository extends CrudRepository<Usuario, Long> {
    
    Usuario findById(long id);
    Usuario findByEmail(String email);
}
