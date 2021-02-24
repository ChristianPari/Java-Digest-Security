package com.christianpari.digest.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.www.DigestAuthenticationEntryPoint;
import org.springframework.security.web.authentication.www.DigestAuthenticationFilter;

@Configuration
@Order(1)
public class AdminSecurityConfig extends WebSecurityConfigurerAdapter {

  // config for digest
  private DigestAuthenticationEntryPoint getDigestEntryPoint() {
    DigestAuthenticationEntryPoint entryPoint = new DigestAuthenticationEntryPoint();

    entryPoint.setRealmName("admin-digest-realm");
    entryPoint.setKey("digest_key");

    return entryPoint;
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return NoOpPasswordEncoder.getInstance();
  }

  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth.inMemoryAuthentication()
      .withUser("TestUser")
      .password(passwordEncoder().encode("secret"))
      .roles("USER")
      .and()
      .withUser("TestAdmin")
      .password(passwordEncoder().encode("admin_secret"))
      .roles("ADMIN");
  }

  @Bean
  public UserDetailsService userDetailsServiceBean() throws Exception {
    return super.userDetailsService();
  }

  private DigestAuthenticationFilter getDigestAuthFilter() throws Exception {
    DigestAuthenticationFilter filter = new DigestAuthenticationFilter();

    filter.setUserDetailsService(userDetailsServiceBean());
    filter.setAuthenticationEntryPoint(getDigestEntryPoint());

    return filter;
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.antMatcher("admin/**")
      .addFilter(getDigestAuthFilter())
      .exceptionHandling()
      .authenticationEntryPoint(getDigestEntryPoint())
      .and()
      .authorizeRequests()
      .antMatchers("/admin/**")
      .hasRole("ADMIN");
  }
}
