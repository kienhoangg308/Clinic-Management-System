package com.clinic.admin.security;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean
    public UserDetailsService userDetailsService() {
        return new ClinicUserDetailsService();
    }  //loading user details

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }  //hash passwords

    @Override
    protected void configure(HttpSecurity http) throws Exception{
        http.authorizeRequests()
                .antMatchers("/services/**").hasAnyAuthority("Admin")
                .antMatchers("/staffs/**", "/doctors/**","/customers/**").hasAnyAuthority("Admin", "Staff")
                .antMatchers("/appointments","/appointments/page/**","/appointments/edit/**").hasAnyAuthority("Admin","Staff","Doctor")
                .antMatchers("/appointments/**").hasAnyAuthority("Admin","Staff")
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/login")
                .usernameParameter("email")
                .permitAll()
                .and().logout().permitAll()
                .and()
                .rememberMe()
                .key("AbcDefgHijKlmnOpqrs_1234567890")
                .tokenValiditySeconds(7 * 24 * 60 * 60);

    }

    @Override
    public void configure(org.springframework.security.config.annotation.web.builders.WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/images/**", "/js/**", "/webjars/**");
    }

    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());

        return authProvider;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider());
    }
}
