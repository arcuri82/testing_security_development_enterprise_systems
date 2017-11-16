package org.tsdes.spring.security.distributedsession.userservice

import org.springframework.context.annotation.Configuration
import org.springframework.core.annotation.Order
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity


/*
  When testing this service in isolation, we would not have Zuul up and running.
  So we override the default security configuration to use an in-memory
  set of users/passwords.
 */
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