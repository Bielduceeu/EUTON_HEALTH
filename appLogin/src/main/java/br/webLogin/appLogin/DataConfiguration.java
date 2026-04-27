package br.webLogin.appLogin;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;//o springframework significa que estamos usando a biblioteca Spring Framework, que é um framework popular para desenvolvimento de aplicações Java. O DriverManagerDataSource é uma classe fornecida pelo Spring que facilita a configuração 
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;        





@Configuration  /* Este @ significa que esta classe contém configurações para o Spring */
    public class DataConfiguration {

    @Bean/* Este @ significa que este método retorna um bean que será gerenciado pelo Spring */
    public DataSource dataSource(){
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://localhost:3306/applogin?useTimezone=true&serverTimezone=UTC"); //a URL de conexão com o banco de dados MySQL, onde "localhost" é o endereço do servidor de banco de dados (neste caso, o próprio computador), "3306" é a porta padrão do MySQL e "appLogin" é o nome do banco de dados que será utilizado.
        dataSource.setUsername("root"); //Normalmente o usuário é root, mas pode variar dependendo da configuração do MySQL
        dataSource.setPassword("12344321");
        return dataSource; //o retorno existe para que o Spring possa usar essa configuração para se conectar ao banco de dados MySQL.
    }
    
    @Bean /* @Bean dentro do código significa que este método retorna um bean */
    public JpaVendorAdapter jpaVendorAdapter(){ //este método serve para configurar o adaptador JPA (Java Persistence API) para o Hibernate, que é a implementação de JPA utilizada neste projeto. O adaptador é responsável por fornecer informações sobre o banco de dados e as configurações específicas do Hibernate.
        HibernateJpaVendorAdapter adapter = new HibernateJpaVendorAdapter();
        adapter.setDatabase(Database.MYSQL); // este processo de setar serve para informar ao Hibernate que o banco de dados utilizado é o MySQL, permitindo que ele otimize as operações de acordo com as características específicas desse banco de dados.
        adapter.setShowSql(true); //serve para exibir as consultas SQL geradas pelo Hibernate no console, o que pode ser útil para depuração e análise do desempenho.
        adapter.setGenerateDdl(true); //serve para indicar que o Hibernate deve gerar automaticamente as tabelas e o esquema do banco de dados com base nas entidades definidas na aplicação. Isso facilita o desenvolvimento, pois não é necessário criar manualmente as tabelas no banco de dados.
        adapter.setDatabasePlatform("org.hibernate.dialect.MySQLDialect"); //serve para especificar o dialeto do banco de dados que o Hibernate deve usar. O dialeto é uma classe que contém as regras específicas para gerar as consultas SQL corretas para o banco de dados em questão. No caso, "org.hibernate.dialect.MySQLDialect" indica que o Hibernate deve usar o dialeto específico para o MySQL, garantindo que as consultas geradas sejam compatíveis com esse banco de dados.
        adapter.setPrepareConnection(true); //serve para indicar que o Hibernate deve preparar a conexão com o banco de dados antes de executar as operações. Isso pode melhorar o desempenho, pois permite que o Hibernate otimize as conexões e reutilize conexões existentes quando possível.    
        return adapter;
    }



}
