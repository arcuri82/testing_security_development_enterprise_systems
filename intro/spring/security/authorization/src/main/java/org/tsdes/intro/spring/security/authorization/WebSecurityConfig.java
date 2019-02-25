package org.tsdes.intro.spring.security.authorization;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.sql.DataSource;

/**
 * Created by arcuri82 on 13-Dec-17.
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Bean
    @Override
    public UserDetailsService userDetailsServiceBean() throws Exception {
        return super.userDetailsServiceBean();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception{
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) {

        /*
            In this method, we specify the authorization rules, ie the Access Control Policy
         */

        try {
            /*
                By default, Spring Security enables CSRF Tokens (used to protect from CSRF attacks),
                which we would have to manually handle in each POST form.
                This is critical for Spring MVC applications, but arguably not for REST APIs
                based on JSON.
                However, JSF has its own CSRF Token mechanism based on JSF-Views.
                So, we just disable the Spring Security one.
             */
            http.csrf().disable();

            /*
                Authorization rules are checked 1 at a time, starting from the top.
                Matching is based on the resource path in the HTTP request, and can
                user regex for it.
                We want to allow anyone to access the homepage and the login/signup/logout.
                All other pages are denied unless the user is authenticated.
                Note: login and logout pages are handled specially by Spring Security.
             */

            http.authorizeRequests()
                    .antMatchers("/").permitAll()
                    .antMatchers("/index.*").permitAll()
                    .antMatchers("/signup.*").permitAll()
                    .antMatchers("/javax.faces.resource/**").permitAll()
                    //whitelisting: everything not explicitly allowed above gets denied
                    .anyRequest().authenticated()
                    .and()
                    /*
                        Here we tell Spring Security that login is going to be done with a HTML form,
                        which will encode the username/password using x-www-form-urlencoded
                     */
                    .formLogin()
                    // here, we use our custom page to handle the login
                    .loginPage("/login.jsf")
                    .permitAll()
                    /*
                        in case of failed login, stay on same page,
                        but add to the URL the query param "?error=true"
                     */
                    .failureUrl("/login.jsf?error=true")
                    //if login is a success, do a 302 redirect to homepage
                    .defaultSuccessUrl("/index.jsf?faces-redirect=true")
                    .and()
                    /*
                        we do not have a page for Logout. Spring Security will automatically
                        create an endpoint which handles POST on /logout.
                        To call it from JSF (or any HTML page), we can just do <form> submission
                        toward it
                      */
                    .logout()
                    //on logout, do a 302 redirect to homepage
                    .logoutSuccessUrl("/index.jsf?faces-redirect=true");
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) {

        /*
            Here, we need to tell Spring Security how to access the SQL database
            to check the username and the hashed password when trying to authenticate
            a user.
         */

        try {
            auth.jdbcAuthentication()
                    .dataSource(dataSource)
                    .usersByUsernameQuery(
                            "SELECT username, password, enabled " +
                                    "FROM users " +
                                    "WHERE username = ?"
                    )
                    .authoritiesByUsernameQuery(
                            "SELECT x.username, y.roles " +
                                    "FROM users x, user_entity_roles y " +
                                    "WHERE x.username = ? and y.user_entity_username = x.username "
                    )
                    /*
                        Note: in BCrypt, the "password" field also contains the salt
                     */
                    .passwordEncoder(passwordEncoder);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
