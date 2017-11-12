package org.tsdes.spring.security.distributedsession.greetings

import org.springframework.context.annotation.Configuration
import org.springframework.core.annotation.Order
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity

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