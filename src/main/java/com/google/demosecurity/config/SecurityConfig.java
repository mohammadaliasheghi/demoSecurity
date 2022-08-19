package com.google.demosecurity.config;

import com.google.demosecurity.service.OAuth2UserService;
import com.google.demosecurity.service.UsersService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final UsersService usersService;
    private final OAuth2UserService oAuth2UserService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests()
                .antMatchers("/", "/login", "error")
                .permitAll()
//                .antMatchers("/user/**", "/signUp/**").hasAnyAuthority("USER", "ADMIN")
//                .antMatchers("/admin/**").hasAuthority("ADMIN")
                .anyRequest()
                .authenticated()
                .and()
                .formLogin()
                .loginPage("/login")
                .usernameParameter("email")
//                .defaultSuccessUrl("/", true)
                .successHandler(new LoginSuccessHandler())

                .and()
                .oauth2Login()
                .loginPage("/oauth2Login")
                .authorizationEndpoint()
                .baseUri("/login/oauth2/")
                .and()
                .redirectionEndpoint()
                .baseUri("/login/callBack")
                .and()
                .userInfoEndpoint()
                .userService(oAuth2UserService)
                .and()

                .and()
                .rememberMe()
                .rememberMeCookieName("remember")
                .tokenValiditySeconds(24 * 60 * 60)
                .rememberMeParameter("remember_me")
                .and()
                .exceptionHandling()
                .accessDeniedPage("/error")
                .and()
                .logout()
//                .logoutUrl("/logout")
                .logoutSuccessUrl("/")
                .deleteCookies("remember", "remember-me");
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(usersService);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }
}
