package org.springframework.samples.petclinic.security;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@EnableWebSecurity
public class JdbcHttpBasicConfigurerAdapter extends WebSecurityConfigurerAdapter {
    private final DataSource dataSource;
    
    @Autowired
    public JdbcHttpBasicConfigurerAdapter(DataSource dataSource) {
        this.dataSource = dataSource;
    }

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
        auth.jdbcAuthentication().dataSource(dataSource);
    }
}
