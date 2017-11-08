package org.tsdes.spring.security.database

import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.crypto.password.PasswordEncoder
import javax.sql.DataSource


@Configuration
@EnableWebSecurity
class WebSecurityConfig(
        private val dataSource: DataSource,
        private val passwordEncoder: PasswordEncoder
) : WebSecurityConfigurerAdapter() {



    override fun configure(http: HttpSecurity) {

        http.authorizeRequests()
                .antMatchers("/login", "/signIn").permitAll()
                .antMatchers("/resource", "/logout").hasRole("USER")
                .anyRequest().denyAll()
                .and()
                // handling of login
                .formLogin()
                .loginPage("/login")
                .usernameParameter("the_user")
                .passwordParameter("the_password")
                .and()
                // logout
                .logout()
                .logoutUrl("/logout")
                .and()
                // Needed when client is a browser
                //TODO
                .csrf().disable()
    }


    override fun configure(auth: AuthenticationManagerBuilder) {

        auth.jdbcAuthentication()
                .dataSource(dataSource)
                .usersByUsernameQuery("""
                     SELECT username, password, enabled
                     FROM users
                     WHERE username=?
                     """)
                .authoritiesByUsernameQuery("""
                     SELECT x.username, y.roles
                     FROM users x, user_entity_roles y
                     WHERE x.username=? and y.user_entity_username=x.username
                     """)
                .passwordEncoder(passwordEncoder)
    }
}