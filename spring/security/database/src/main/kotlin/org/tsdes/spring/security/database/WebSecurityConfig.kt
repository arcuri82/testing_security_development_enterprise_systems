package org.tsdes.spring.security.database

import org.springframework.boot.autoconfigure.security.SecurityProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.annotation.Order
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.crypto.password.PasswordEncoder
import javax.servlet.http.HttpServletResponse
import javax.sql.DataSource
import org.springframework.security.core.userdetails.UserDetailsService




@Configuration
@EnableWebSecurity
class WebSecurityConfig(
        private val dataSource: DataSource,
        private val passwordEncoder: PasswordEncoder
) : WebSecurityConfigurerAdapter() {


    @Bean
    override fun userDetailsServiceBean(): UserDetailsService {
        return super.userDetailsServiceBean()
    }

    override fun configure(http: HttpSecurity) {

        http.httpBasic()
                .and()
                .logout()
                .and()
                //
                .authorizeRequests()
                .antMatchers("/user").authenticated()
                .antMatchers("/signIn").permitAll()
                .antMatchers("/resource").hasRole("USER")
                .anyRequest().denyAll()
                .and()
                /*
                    CSRF protection is on by default.
                    Here for simplicity we deactivate it, as
                    it complicates how we do the HTTP calls.
                    However, it is extremely important when
                    the clients are browser, so
                    in practice it should not be deactivated.
                    Anyway, we ll look at it in the next module.
                  */
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
                /*
                    Note: in BCrypt, the "password" field also contains the salt
                 */
                .passwordEncoder(passwordEncoder)
    }
}