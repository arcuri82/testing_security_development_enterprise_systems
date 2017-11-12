package org.tsdes.spring.security.distributedsession.zuul

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import javax.sql.DataSource

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
                .antMatchers("/user-service/usersInfoCount").permitAll()
                .antMatchers("/user-service/**").authenticated()
                .antMatchers("/greetings/**").authenticated()
                .anyRequest().denyAll()
                .and()
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