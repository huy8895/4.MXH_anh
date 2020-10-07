package com.xem_vn.config;

import com.xem_vn.service.IAppUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class AppSecConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    public IAppUserService appUserService;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService((UserDetailsService) appUserService)
                .passwordEncoder(NoOpPasswordEncoder.getInstance());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/","/home","/create-account","/post/detail/*").permitAll()
                .antMatchers("/admin/**").hasAnyRole("ADMIN")
                .antMatchers("/account/**").hasAnyRole("USER","ADMIN")
                .antMatchers("/post/**").hasAnyRole("USER","ADMIN")
                .antMatchers("/**").permitAll()
                .and()
                .authorizeRequests().antMatchers("/**").permitAll()
                .and()
                .formLogin()
                .and()
                .logout().logoutRequestMatcher(new AntPathRequestMatcher("/logout")).logoutSuccessUrl("/")
                .and().exceptionHandling()
                .accessDeniedPage("/Access_Denied")
        ;
        http.csrf().disable();
    }
}
