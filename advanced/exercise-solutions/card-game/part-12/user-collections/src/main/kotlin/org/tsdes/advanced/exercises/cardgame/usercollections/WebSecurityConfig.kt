package org.tsdes.advanced.exercises.cardgame.usercollections

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.UserDetails

@Configuration
@EnableWebSecurity
class WebSecurityConfig: WebSecurityConfigurerAdapter() {


    override fun configure(http: HttpSecurity) {

        http
//                .httpBasic()
//                .and()
                .authorizeRequests()
                .antMatchers("/swagger*/**", "/v3/api-docs", "/actuator/**").permitAll()
                .antMatchers("/api/user-collections/{id}")
                .access("hasRole('USER') and @userSecurity.checkId(authentication, #id)")
                .anyRequest().denyAll()
                .and()
                .csrf().disable()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.NEVER)
    }

    @Bean
    fun userSecurity() : UserSecurity {
        return UserSecurity()
    }
}

class UserSecurity{

    fun checkId(authentication: Authentication, id: String) : Boolean{

        if(authentication.principal !is UserDetails){
            return false
        }

        val current = (authentication.principal as UserDetails).username

        return current == id
    }
}


