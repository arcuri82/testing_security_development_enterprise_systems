package org.tsdes.spring.security.database

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder


@Configuration
@EnableWebSecurity
class WebSecurityConfig : WebSecurityConfigurerAdapter() {

    /**
     * Make sure we use BCrypt to encode/hash the password.
     * To override the default encoding, we need to provide
     * a bean with name passwordEncoder
     */
    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }


    override fun configure(http: HttpSecurity) {

        http.authorizeRequests()
                .antMatchers("/").permitAll()
                .anyRequest().denyAll()
                .and()
                .httpBasic()
    }


    override fun configure(auth: AuthenticationManagerBuilder) {

        auth.inMemoryAuthentication()
                .withUser("foo").password("123456").roles("USER").and()
                .withUser("admin").password("bar").roles("ADMIN", "USER")
    }
}