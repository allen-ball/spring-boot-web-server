package application;

import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableJpaRepositories @EnableTransactionManagement
@NoArgsConstructor @ToString @Log4j2
public class Launcher {
    public static void main(String[] argv) throws Exception {
        SpringApplication application = new SpringApplication(Launcher.class);

        application.run(argv);
    }
}
