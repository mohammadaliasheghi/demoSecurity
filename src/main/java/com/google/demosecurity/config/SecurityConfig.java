package com.google.demosecurity.config;

import com.google.demosecurity.jwt.JwtFilter;
import com.google.demosecurity.service.OAuth2UserService;
import com.google.demosecurity.service.UsersService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final UsersService usersService;
    private final OAuth2UserService oAuth2UserService;
    private final JwtFilter jwtFilter;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests()
                .antMatchers("/", "/login", "error", "/jwt/login")
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

                //using for oAuth google or other provider
//                .and()
//                .oauth2Login()
//                .loginPage("/oauth2Login")
//                .authorizationEndpoint()
//                .baseUri("/login/oauth2/")
//                .and()
//                .redirectionEndpoint()
//                .baseUri("/login/callBack")
//                .and()
//                .userInfoEndpoint()
//                .userService(oAuth2UserService)
//                .and()
//                .successHandler(new LoginSuccessHandler())

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
                .deleteCookies("remember", "remember-me")
                //using for validate token
                //important : if using jwt and restFull api then session going to state less from state full
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(usersService);
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }
}
