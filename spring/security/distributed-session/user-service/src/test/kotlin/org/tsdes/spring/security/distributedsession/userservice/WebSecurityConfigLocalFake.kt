package org.tsdes.spring.security.distributedsession.userservice

import org.springframework.boot.autoconfigure.security.SecurityProperties
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.core.annotation.Order
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter

@Configuration
@EnableWebSecurity
@Order(1)
class WebSecurityConfigLocalFake : WebSecurityConfig() {


    override fun configure(auth: AuthenticationManagerBuilder) {

        auth.inMemoryAuthentication()
                .withUser("foo").password("123").roles("USER").and()
                .withUser("bar").password("123").roles("USER").and()
                .withUser("admin").password("admin").roles("ADMIN", "USER")
    }
}