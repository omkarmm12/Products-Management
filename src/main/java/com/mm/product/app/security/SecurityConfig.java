package com.mm.product.app.security;

import com.mm.product.app.securityservice.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Bean
 public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
     http
             .csrf().disable()
             .authorizeRequests()
             .antMatchers("/user/add","/user/getall").permitAll()
             .antMatchers("/product/addall","/product/add").hasRole("ADMIN")
             .anyRequest().authenticated()
             .and()
             .httpBasic()
             .and()
             .formLogin();
     return http.build();
 }
 @Bean
 public AuthenticationManager authenticationManager(){
     DaoAuthenticationProvider provider=new DaoAuthenticationProvider();
     provider.setPasswordEncoder(passwordEncoder());
     provider.setUserDetailsService(customUserDetailsService);
     return new ProviderManager(provider);
 }
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

//    @Bean
//    public InMemoryUserDetailsManager manager(){
//        UserDetails user= User.builder().username("name").password(passwordEncoder().encode("password"))
//                .roles("USER","ADMIN").build();
//        return new InMemoryUserDetailsManager(user);
//    }

}
