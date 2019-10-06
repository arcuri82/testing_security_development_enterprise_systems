package org.tsdes.advanced.security.session

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.password.PasswordEncoder
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

    @Bean
    override fun authenticationManagerBean() : AuthenticationManager {
        return super.authenticationManagerBean()
    }

    override fun configure(http: HttpSecurity) {

        http.httpBasic()
                .and()
                .logout()
                .and()
                //
                .authorizeRequests()
                .antMatchers("/user").authenticated()
                .antMatchers("/signUp").permitAll()
                .antMatchers("/resource").hasRole("USER")
                .anyRequest().denyAll()
                .and()
                /*
                    CSRF protection is on by default.
                    Here for simplicity we deactivate it, as
                    it complicates how we do the HTTP calls.
                    In general, in a JSON-based app with endpoints
                    following the HTTP semantics (eg, GETs do not
                    have side-effects), then CSRF would not be so important,
                    as covered by CORS (could add SameSite for extra protection).
                    However, note that "/signUp" here would require CSRF protection,
                    as using the format "application/x-www-form-urlencoded".
                  */
                .csrf().disable()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
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