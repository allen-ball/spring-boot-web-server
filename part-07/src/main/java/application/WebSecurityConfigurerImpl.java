package application;

import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import static lombok.AccessLevel.PRIVATE;
import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@NoArgsConstructor(access = PRIVATE) @Log4j2
public abstract class WebSecurityConfigurerImpl extends WebSecurityConfigurerAdapter {
    @Autowired private UserDetailsService userDetailsService = null;
    @Autowired private PasswordEncoder passwordEncoder = null;

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService)
            .passwordEncoder(passwordEncoder);
    }

    @Configuration
    @Order(1)
    @NoArgsConstructor @ToString
    public static class API extends WebSecurityConfigurerImpl {
        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.antMatcher("/api/**")
                .csrf(t -> t.disable())
                .authorizeRequests(t -> t.anyRequest().authenticated())
                .httpBasic(withDefaults());
        }
    }

    @Configuration
    @Order(2)
    @NoArgsConstructor @ToString
    public static class UI extends WebSecurityConfigurerImpl {
        private static final String[] IGNORE = {
            "/css/**", "/js/**", "/images/**", "/webjars/**", "/webjarsjs"
        };

        @Override
        public void configure(WebSecurity web) {
            web.ignoring().antMatchers(IGNORE);
        }

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.antMatcher("/**")
                .authorizeRequests(t -> t.anyRequest().permitAll())
                .formLogin(t -> t.loginPage("/login").permitAll())
                .logout(t -> t.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                              .logoutSuccessUrl("/").permitAll());
        }
    }
}
