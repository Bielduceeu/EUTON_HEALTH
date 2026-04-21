package br.webLogin.appLogin.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

/*EM geral, esta classe representa a entidade "Usuario" que será mapeada para uma tabela no banco de
//  dados. Ela possui campos para armazenar informações do usuário, como nome, email e senha,
//  além de um campo id que é gerado automaticamente pelo banco de dados. A anotação @Data do Lombok 
// é usada para gerar automaticamente os métodos getters, setters, equals, hashCode e toString, simplificando
//  o código da classe. As anotações de validação (@NotEmpty) garantem que os campos nome, email e senha não
//  sejam deixados em branco ao criar ou atualizar um usuário.*/
@Entity
@Data
public class Usuario {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)  /* Este @ significa que o valor do campo id será gerado automaticamente pelo banco de dados */
        private long id;

        @NotEmpty
        private String nome;
        
        @NotEmpty
        private String email;   

        @NotEmpty
        private String senha;

        public String getNome() {
                return nome;
        }

        public void setNome(String nome) {
                this.nome = nome;
        }
        
        public String getEmail() {
                return email;
        }

        public void setEmail(String email) {
                this.email = email;
        }


        public String getSenha() {
                return senha;
        }
        public void setSenha(String senha) {
                this.senha = senha;
        }

        public long getId() {
                return id;
        }

        public void setId(long id) {
                this.id = id;
        }
   }
