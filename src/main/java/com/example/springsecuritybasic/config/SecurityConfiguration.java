package com.example.springsecuritybasic.config;

import com.example.springsecuritybasic.filter.AuthoritiesLoggingAfterFilter;
import com.example.springsecuritybasic.filter.AuthoritiesLoggingAtFilter;
import com.example.springsecuritybasic.filter.JWTTokenGeneratorFilter;
import com.example.springsecuritybasic.filter.RequestValidatorBeforeFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import javax.servlet.http.HttpServletRequest;

import java.util.Arrays;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
public class SecurityConfiguration {

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http.sessionManagement()
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and()
        .cors()
        .configurationSource(
            new CorsConfigurationSource() {
              @Override
              public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
                CorsConfiguration corsConfiguration = new CorsConfiguration();
                corsConfiguration.setExposedHeaders(Arrays.asList(HttpHeaders.AUTHORIZATION));
                return corsConfiguration;
              }
            })
        .and()
        .authorizeHttpRequests()
        .antMatchers("/not-secured")
        .permitAll()
        .antMatchers("/secured")
        .authenticated()
        .antMatchers("/read-only")
        .hasAuthority("READ")
        .antMatchers("/read-write")
        .hasAuthority("WRITE")
        .antMatchers("/user-role")
        .hasRole("USER")
        .antMatchers("/admin-role")
        .hasRole("ADMIN")
        .antMatchers("/login")
        .hasAuthority("READ")
        .and()
        .csrf()
        .disable()
        .addFilterBefore(new RequestValidatorBeforeFilter(), BasicAuthenticationFilter.class)
        .addFilterAfter(new AuthoritiesLoggingAfterFilter(), BasicAuthenticationFilter.class)
        .addFilterAfter(new JWTTokenGeneratorFilter(), BasicAuthenticationFilter.class)
        .addFilterAt(new AuthoritiesLoggingAtFilter(), BasicAuthenticationFilter.class)
        .httpBasic(withDefaults());
    return http.build();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
}
