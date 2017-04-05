package org.springframework.samples.petclinic.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

//@EnableWebSecurity
public class InMemoryHttpBasicConfigurerAdapter extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/pets/**/edit").hasRole("EDIT")
                .antMatchers("/owners/**/edit").hasRole("EDIT")
                .antMatchers("/pets/new").hasRole("ADD")
                .antMatchers("/owners/new").hasRole("ADD")
                .anyRequest().authenticated()
                .and()
                .httpBasic()
                .and()
                .csrf().disable();
    }

    @Autowired
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser("Bill").password("Bill").roles("USER")
                .and()
                .withUser("Stephen").password("Stephen").roles("EDIT", "USER")
                .and()
                .withUser("Samantha").password("Samantha").roles("ADD", "EDIT", "USER");
    }
}
